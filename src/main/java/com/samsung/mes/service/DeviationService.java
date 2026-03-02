package com.samsung.mes.service;

import java.util.Random;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.samsung.mes.dto.DeviationDTO;

@Service
public class DeviationService {

    private final Random random = new Random();

    public DeviationDTO simulateDeviation() {
        String[] parameters = {"Temperature", "pH", "Dissolved Oxygen"};
        String parameter = parameters[random.nextInt(parameters.length)];

        // Determine severity based on weighted random
        int rand = random.nextInt(100);
        String severity;
        if (rand < 75) {
            severity = "MINOR";
        } else if (rand < 95) { // 75 + 20
            severity = "MAJOR";
        } else { // Remaining 5
            severity = "CRITICAL";
        }

        double limitValue = 0.0;
        double recordedValue = 0.0;

        // Define base values and deviation logic per parameter
        if ("Temperature".equals(parameter)) {
            limitValue = 37.0; // Example limit for cell culture
            // Deviation logic: higher severity means larger deviation
            double deviation = getDeviationAmount(severity, 0.5, 2.0, 5.0);
            recordedValue = limitValue + deviation * (random.nextBoolean() ? 1 : -1);
        } else if ("pH".equals(parameter)) {
            limitValue = 7.2;
            double deviation = getDeviationAmount(severity, 0.1, 0.5, 1.0);
            recordedValue = limitValue + deviation * (random.nextBoolean() ? 1 : -1);
        } else if ("Dissolved Oxygen".equals(parameter)) {
            limitValue = 50.0; // % saturation
            double deviation = getDeviationAmount(severity, 5.0, 15.0, 30.0);
            recordedValue = limitValue + deviation * (random.nextBoolean() ? 1 : -1);
        }

        // Round recordedValue to 2 decimal places
        recordedValue = Math.round(recordedValue * 100.0) / 100.0;

        return DeviationDTO.builder()
                .batchId(UUID.randomUUID().toString())
                .parameter(parameter)
                .recordedValue(recordedValue)
                .limitValue(limitValue)
                .severity(severity)
                .status("OPEN")
                .isClosed(false)
                .build();
    }

    private double getDeviationAmount(String severity, double minorBase, double majorBase, double criticalBase) {
        double noise = random.nextDouble() * minorBase; // add some noise
        switch (severity) {
            case "MINOR":
                return minorBase + noise;
            case "MAJOR":
                return majorBase + noise;
            case "CRITICAL":
                return criticalBase + noise;
            default:
                return minorBase;
        }
    }
}
