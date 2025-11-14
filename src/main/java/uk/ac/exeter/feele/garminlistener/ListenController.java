package uk.ac.exeter.feele.garminlistener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("/listen")
    public String listenPost(@RequestBody String jsonBody) {
        int startTime = (int)(System.currentTimeMillis());
        try {
            service.importData(jsonBody);

        } catch (Exception e) {
            System.out.println("Received POST JSON: " + jsonBody + " (Failed to parse JSON: " + e.getMessage() + ")");
        }
        int duration = (int)(System.currentTimeMillis() - startTime);
        System.out.println("Handled request in: " + duration + "ms - ");
        return "OK";
    }

    @DeleteMapping("/deregister")
    public String deregister(@RequestParam Map<String, String> params) {
        int startTime = (int)(System.currentTimeMillis());
        try {
            System.out.println("Received DELETE params: " + params);
            // Not sure we need to do anything here? Garmin is just letting us know, and it will
            // stop sending data. We had the permission of the user to collect the data we have, and
            // I think we can keep it unless they specifically ask us to delete it.
        } catch (Exception e) {
            System.out.println("Failed to handle DELETE: " + e.getMessage());
        }
        int duration = (int)(System.currentTimeMillis() - startTime);
        System.out.println("Handled request in: " + duration + "ms - ");
        return "OK";
    }

    @GetMapping("/permissionChange")
    public String permissionChange(@RequestParam Map<String, String> params) {
        int startTime = (int)(System.currentTimeMillis());
        try {
            System.out.println("Received permissionChange, params: " + params);
            // Not sure we need to do anything here? Garmin is just letting us know, and it will
            // stop sending data. We had the permission of the user to collect the data we have, and
            // I think we can keep it unless they specifically ask us to delete it.
        } catch (Exception e) {
            System.out.println("Failed to handle permissionChange: " + e.getMessage());
        }
        int duration = (int)(System.currentTimeMillis() - startTime);
        System.out.println("Handled request in: " + duration + "ms - ");
        return "OK";
    }
}
