package com.dumE.machine_health_monitor.controller;

import com.dumE.machine_health_monitor.model.SensorReading;
import com.dumE.machine_health_monitor.repository.SensorReadingRepository;
import com.dumE.machine_health_monitor.service.AnomalyDetectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/sensors")
@CrossOrigin(origins = "*")
public class SensorController {

    private final SensorReadingRepository sensorReadingRepository;
    private final AnomalyDetectionService anomalyDetectionService;

    public SensorController(SensorReadingRepository sensorReadingRepository,
                            AnomalyDetectionService anomalyDetectionService) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.anomalyDetectionService = anomalyDetectionService;
    }

    @PostMapping("/readings")
    public ResponseEntity<SensorReadingResponse> ingest(@Valid @RequestBody SensorReadingRequest request) {
        SensorReading reading = new SensorReading(
                request.machineId(),
                request.sensorType(),
                request.value(),
                Instant.now());

        boolean isAnomaly = anomalyDetectionService.evaluateAndRecord(reading);
        reading.setAnomaly(isAnomaly);

        SensorReading saved = sensorReadingRepository.save(reading);

        return ResponseEntity.status(HttpStatus.CREATED).body(SensorReadingResponse.from(saved));
    }
}