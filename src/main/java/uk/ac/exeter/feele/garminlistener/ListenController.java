package uk.ac.exeter.feele.garminlistener;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ListenController {

    @GetMapping("/listen")
    public String listenGet(@RequestParam Map<String, String> allParams) {
        System.out.println("Received GET params: " + allParams);
        return "Received GET params: " + allParams;
    }

    @PostMapping("/listen")
    public String listenPost(@RequestBody String jsonBody) {
        System.out.println("Received POST JSON: " + jsonBody);
        return "Received POST JSON: " + jsonBody;
    }
}
