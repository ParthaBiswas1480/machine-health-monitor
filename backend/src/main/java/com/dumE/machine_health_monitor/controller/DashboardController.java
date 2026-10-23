package com.dumE.machine_health_monitor.controller;

import com.dumE.machine_health_monitor.model.Anomaly;
import com.dumE.machine_health_monitor.model.SensorReading;
import com.dumE.machine_health_monitor.model.SensorType;
import com.dumE.machine_health_monitor.repository.AnomalyRepository;
import com.dumE.machine_health_monitor.repository.SensorReadingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final SensorReadingRepository sensorReadingRepository;
    private final AnomalyRepository anomalyRepository;

    public DashboardController(SensorReadingRepository sensorReadingRepository,
                               AnomalyRepository anomalyRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.anomalyRepository = anomalyRepository;
    }

    @GetMapping("/sensors/readings/latest")
    public List<SensorReadingResponse> latest(@RequestParam String machineId,
                                              @RequestParam SensorType sensorType) {
        List<SensorReading> readings = new ArrayList<>(
                sensorReadingRepository.findTop50ByMachineIdAndSensorTypeOrderByTimestampDesc(
                        machineId, sensorType));

        Collections.reverse(readings); // newest-first from the DB -> oldest-first for charting

        return readings.stream().map(SensorReadingResponse::from).toList();
    }

    @GetMapping("/anomalies/recent")
    public List<Anomaly> recentAnomalies() {
        return anomalyRepository.findTop20ByOrderByDetectedAtDesc();
    }

    @PatchMapping("/anomalies/{id}/acknowledge")
    public ResponseEntity<Anomaly> acknowledge(@PathVariable Long id) {
        return anomalyRepository.findById(id)
                .map(anomaly -> {
                    anomaly.acknowledge();
                    return ResponseEntity.ok(anomalyRepository.save(anomaly));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}