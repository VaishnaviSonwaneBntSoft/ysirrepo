package io.nimbuspay.trivydemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/trivydemo")
public class Greet {
    private static final Logger logger = LoggerFactory.getLogger(Greet.class);

    @GetMapping("/greet")
    public ResponseEntity<String> greet() {
        System.out.println("Starting");
        logger.info("Starting :: /api/trivydemo/greet");
        testAllocations();
        logger.info("Continue :: /api/trivydemo/greet");
        testAllocations();
        logger.info("Stopping :: /api/trivydemo/greet");
        return ResponseEntity.ok().body("Hello from Aqua Trivy and GC Demo :: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void testAllocations() {
        List<String> sentences = new LinkedList<>();
        for (int i = 0; i < 25; i++) {
            String line = "The number selected is " + i;
            sentences.add(line);
        }
        logger.info(String.join("::", sentences));
    }
}
