package uk.ac.exeter.feele.garminlistener;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ListenController {

    @GetMapping("/listen")
    public String listen(@RequestParam Map<String, String> allParams) {
        System.out.println("Received params: " + allParams);
        return "Received params: " + allParams;
    }
}
