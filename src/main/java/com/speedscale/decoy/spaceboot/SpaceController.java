package com.speedscale.decoy.spaceboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpaceController {

    static Logger logger = LoggerFactory.getLogger(SpaceController.class);
    
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final SpaceXService spacex;

    public SpaceController(SpaceXService spacex) {
        this.spacex = spacex;
    }

    @GetMapping("/")
    public String home() {
        String rspBody = "{\"spring\": \"is here\"}";
        return rspBody;
    }

    @GetMapping("/healthz")
    public String health() {
        String rspBody = "{\"health\": \"health\"}";
        return rspBody;
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/ship")
    public String ship() {
        String rspBody = "{}";
        try {
            rspBody = spacex.ship();
        } catch (Exception e) {
            logger.error("Exception calling SpaceX", e);
            rspBody = "{\"exception\": \"" + e.getMessage() + "\"}";
        }
        return rspBody;
    }
    
    @GetMapping("/space")
    public String space() {
        String rspBody = "{}";
        try {
            rspBody = spacex.launches();
        } catch (Exception e) {
            logger.error("Exception calling SpaceX", e);
            rspBody = "{\"exception\": \"" + e.getMessage() + "\"}";
        }
        return rspBody;
    }
}
