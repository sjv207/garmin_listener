package uk.ac.exeter.feele.garminlistener.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.exeter.feele.garminlistener.model.GarminUser;
import uk.ac.exeter.feele.garminlistener.model.UserActivity;
import uk.ac.exeter.feele.garminlistener.model.UserHealth;
import uk.ac.exeter.feele.garminlistener.repository.GarminUserRepository;
import uk.ac.exeter.feele.garminlistener.repository.UserActivityRepository;
import uk.ac.exeter.feele.garminlistener.repository.UserHealthRepository;
import uk.ac.exeter.feele.garminlistener.repository.UserTokenRepository;
import uk.ac.exeter.feele.garminlistener.service.GarminService;

@Service
public class GarminServiceImpl implements GarminService {
    @Autowired
    private GarminUserRepository garminUserRepository;
    @Autowired
    private UserActivityRepository userActivityRepository;
    @Autowired
    private UserHealthRepository userHealthRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;

    @Async
    @Override
    public void importData(String data) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Some Garmin payloads arrive as multiple top-level JSON objects concatenated.
            // Use a streaming parser to handle one or many root documents.
            JsonFactory factory = mapper.getFactory();
            try (JsonParser parser = factory.createParser(data)) {
                while (parser.nextToken() != null) {
                    JsonNode root = mapper.readTree(parser);
                    if (root == null) {
                        break;
                    }
                    processRootNode(root);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to parse JSON: " + e.getMessage());
        }
    }

    private void processRootNode(JsonNode root) {
        // Iterate top-level fields: e.g., dailies, activities, healthSnapshot, pulseox,
        // skinTemp
        root.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            System.out.println("Processing key: " + key);

            switch (key) {
                case "activityDetails":
                    if (value.isArray()) {
                        for (JsonNode act : value) {
                            logActivityDetails(act);
                        }
                    }
                    break;
                case "activities":
                    // Typically an array
                    if (value.isArray()) {
                        for (JsonNode act : value) {
                            logActivity(act);
                        }
                    }
                    break;
                case "dailies": // Dropthrough
                case "healthSnapshot": // Dropthrough
                case "pulseox": // Dropthrough
                case "skinTemp": // Dropthrough
                case "sleeps": // Dropthrough
                case "epochs": // Dropthrough
                case "stressDetails": // Dropthrough
                case "allDayRespiration": // Dropthrough
                case "userMetrics": // Dropthrough
                    if (value.isArray()) {
                        for (JsonNode healthData : value) {
                            String userId = healthData.path("userId").asText(null);
                            String date = healthData.path("calendarDate").asText(null);
                            long startTime = healthData.path("startTimeInSeconds").asLong(0);
                            logHealth(userId, key, date, startTime, healthData.toString());
                            // TODO: map to entity and persist via userActivityRepository
                        }
                    }
                    break;

                default:
                    System.out.println("Unknown data type: " + key);
            }
        });
    }

    private void logActivityDetails(JsonNode node) {
        if (node == null || node.isNull())
            return;
        String userId = node.path("userId").asText(null);
        String activityId = node.path("activityId").asText(null);

        JsonNode summary = node.path("summary");
        String activityType = summary.path("activityType").asText(null);
        long startTime = summary.path("startTimeInSeconds").asLong(0);
        int durationInSeconds = summary.path("durationInSeconds").asInt(0);

        System.out.printf("Activity user=%s, type=%s, start=%d, duration=%d\n",
                userId, activityType, startTime, durationInSeconds);

        createOrUpdateActivity(node, userId, activityId, activityType, startTime, durationInSeconds);
    }

    private void logActivity(JsonNode node) {
        if (node == null || node.isNull())
            return;
        String userId = node.path("userId").asText(null);
        String activityId = node.path("activityId").asText(null);
        String activityType = node.path("activityType").asText(null);
        long startTime = node.path("startTimeInSeconds").asLong(0);
        int durationInSeconds = node.path("durationInSeconds").asInt(0);

        System.out.printf("Activity user=%s, type=%s, start=%d, duration=%d\n",
                userId, activityType, startTime, durationInSeconds);
        createOrUpdateActivity(node, userId, activityId, activityType, startTime, durationInSeconds);
    }

    private void createOrUpdateActivity(JsonNode node, String userId, String activityId,
            String activityType, long startTime, int durationInSeconds) {

        // See if this activity already exists, if it does updated it, else create new
        UserActivity activity = userActivityRepository.findByActivityId(activityId);
        if (activity == null) {
            activity = new UserActivity();
            activity.setActivityId(activityId);

            GarminUser garminUser = garminUserRepository.findByGarminUserId(userId);
            if (garminUser == null) {
                System.out.println("Unknown Garmin user: " + userId);
                return;
            }
            activity.setActivityId(activityId);
            activity.setActivityType(activityType);
            activity.setStartTime(startTime);
            activity.setDurationInSeconds(durationInSeconds);
            activity.setGarminUser(garminUser);
            activity.setJsonData(node.toString());
            userActivityRepository.save(activity);
            System.out.println("Created activity: " + activityId);

        } else {
            // Update existing record if needed
            activity.setActivityType(activityType);
            activity.setStartTime(startTime);
            activity.setDurationInSeconds(durationInSeconds);
            activity.setJsonData(node.toString());
            userActivityRepository.save(activity);
            System.out.println("Updated activity: " + activityId);
        }

    }

    private void logHealth(String userId, String healthType, String date, long startTime, String jsonData) {
        GarminUser garminUser = garminUserRepository.findByGarminUserId(userId);
        if (garminUser == null) {
            System.out.println("Unknown Garmin user: " + userId);
            return;
        }
        UserHealth health = new UserHealth();
        health.setGarminUser(garminUser);
        health.setHealthType(healthType);
        health.setStartTime(startTime);
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = sdf.parse(date);
                health.setCalendarDate(new java.sql.Date(parsedDate.getTime()));
            } catch (ParseException e) {
                System.out.println("Failed to parse date: " + date);
            }
        }
        health.setJsonData(jsonData);
        userHealthRepository.save(health);
        System.out.println("Saved health data for user=" + userId + " date=" + date);
    }
}
