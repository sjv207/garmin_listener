package uk.ac.exeter.feele.garminlistener.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.exeter.feele.garminlistener.model.GarminUser;
import uk.ac.exeter.feele.garminlistener.model.UserActivity;
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
        // Iterate top-level fields: e.g., dailies, activities, healthSnapshot, pulseox, skinTemp
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
                            logActivityDetails(act);
                        }
                    }
                    break;
                case "dailies":
                    if (value.isArray()) {
                        for (JsonNode daily : value) {
                            String userId = daily.path("userId").asText(null);
                            String date = daily.path("calendarDate").asText(null);
                            int steps = daily.path("steps").asInt(0);
                            int pushes = daily.path("pushes").asInt(0);
                            int avgHr = daily.path("averageHeartRateInBeatsPerMinute").asInt(0);
                            System.out.printf("Dailies user=%s date=%s steps=%d pushes=%d avgHr=%d\n",
                                    userId, date, steps, pushes, avgHr);
                            // TODO: map to entity and persist via userActivityRepository
                        }
                    }
                    break;
                case "healthSnapshot":
                    if (value.isArray()) {
                        for (JsonNode snap : value) {
                            String userId = snap.path("userId").asText(null);
                            String date = snap.path("calendarDate").asText(null);
                            // Summaries is an array of metric summaries
                            for (JsonNode summary : snap.path("summaries")) {
                                String type = summary.path("summaryType").asText("");
                                double avg = summary.path("avgValue").asDouble(Double.NaN);
                                System.out.printf("HealthSnapshot user=%s date=%s type=%s avg=%.2f\n",
                                        userId, date, type, avg);
                            }
                            // TODO: map to UserHealth entity
                        }
                    }
                    break;
                case "pulseox":
                    if (value.isArray()) {
                        for (JsonNode p : value) {
                            String userId = p.path("userId").asText(null);
                            String date = p.path("calendarDate").asText(null);
                            JsonNode samples = p.path("timeOffsetSpo2Values");
                            int sampleCount = samples.isObject() ? samples.size() : 0;
                            System.out.printf("PulseOx user=%s date=%s samples=%d\n", userId, date, sampleCount);
                            // Iterate map entries if needed:
                            // samples.fields().forEachRemaining(s -> { int offset = Integer.parseInt(s.getKey()); int spo2 = s.getValue().asInt(); });
                        }
                    }
                    break;
                case "skinTemp":
                    if (value.isArray()) {
                        for (JsonNode t : value) {
                            String userId = t.path("userId").asText(null);
                            String date = t.path("calendarDate").asText(null);
                            double avgDev = t.path("avgDeviationCelsius").asDouble(Double.NaN);
                            System.out.printf("SkinTemp user=%s date=%s avgDev=%.3f\n", userId, date, avgDev);
                        }
                    }
                    break;
                default:
                    System.out.println("Unknown data type: " + key);
            }
        });
    }

    private void logActivityDetails(JsonNode node) {
        if (node == null || node.isNull()) return;
        String userId = node.path("userId").asText(null);
        String activityId = node.path("activityId").asText(null);
        
        JsonNode summary = node.path("summary");
        String activityType = summary.path("activityType").asText(null);
        long startTime = summary.path("startTimeInSeconds").asLong(0);
        int durationInSeconds = summary.path("durationInSeconds").asInt(0);

        System.out.printf("Activity user=%s, type=%s, start=%d, duration=%d\n",
                userId, activityType, startTime, durationInSeconds);


        // See if this activity already exists, if it does updated it, else create new
        UserActivity activity = userActivityRepository.findByActivityId(activityId);
        if (activity == null) {
            activity = new UserActivity();
            activity.setActivityId(activityId);
        
            GarminUser garminUser = garminUserRepository.findByGarminUserId(userId);
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
}
