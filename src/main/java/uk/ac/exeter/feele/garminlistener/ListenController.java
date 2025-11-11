package uk.ac.exeter.feele.garminlistener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import uk.ac.exeter.feele.garminlistener.service.GarminService;

@RestController
public class ListenController {
    @Autowired
    private GarminService service;

    @GetMapping("/listen")
    public String listenGet(@RequestParam Map<String, String> allParams) {
        System.out.println("Received GET params: " + allParams);
        return "Received GET params: " + allParams;
    }

    @PostMapping("/listen")
    public String listenPost(@RequestBody String jsonBody) {
        try {
            service.importData(jsonBody);

        } catch (Exception e) {
            System.out.println("Received POST JSON: " + jsonBody + " (Failed to parse JSON: " + e.getMessage() + ")");
        }
        return "Received POST JSON: " + jsonBody;
    }
}
