package uk.ac.exeter.feele.garminlistener.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            Object json = mapper.readValue(data, Object.class);
            System.out.println("Received data: " + json.toString());
            
        } catch (Exception e) {
            System.out.println("Failed to parse JSON: " + e.getMessage());
        }
    }
}
