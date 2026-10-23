package com.dumE.machine_health_monitor.service;

import com.dumE.machine_health_monitor.model.Anomaly;
import com.dumE.machine_health_monitor.model.SensorReading;
import com.dumE.machine_health_monitor.model.SensorType;
import com.dumE.machine_health_monitor.repository.AnomalyRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnomalyDetectionService {

    private static final int WINDOW_SIZE = 20;
    private static final double Z_THRESHOLD = 2.5;

    private static final Map<SensorType, double[]> ABSOLUTE_BOUNDS = new EnumMap<>(SensorType.class);

    static {
        ABSOLUTE_BOUNDS.put(SensorType.TEMPERATURE, new double[]{-10, 120});
        ABSOLUTE_BOUNDS.put(SensorType.VIBRATION, new double[]{0, 15});
        ABSOLUTE_BOUNDS.put(SensorType.RPM, new double[]{0, 5000});
    }

    private final Map<String, Deque<Double>> windows = new ConcurrentHashMap<>();
    private final AnomalyRepository anomalyRepository;

    public AnomalyDetectionService(AnomalyRepository anomalyRepository) {
        this.anomalyRepository = anomalyRepository;
    }

    private String keyFor(String machineId, SensorType sensorType) {
        return machineId + "::" + sensorType;
    }

    public synchronized boolean evaluateAndRecord(SensorReading reading) {
        String key = keyFor(reading.getMachineId(), reading.getSensorType());
        Deque<Double> window = windows.computeIfAbsent(key, k -> new ArrayDeque<>());

        double value = reading.getValue();
        boolean flagged = false;
        String reason = null;
        Double mean = null;
        Double stdDev = null;

        // Check 1: statistical (needs some history first)
        if (window.size() >= 5) {
            mean = mean(window);
            stdDev = stdDev(window, mean);

            if (stdDev > 0) {
                double z = Math.abs(value - mean) / stdDev;
                if (z > Z_THRESHOLD) {
                    flagged = true;
                    reason = String.format(
                            "Value %.2f is %.1f std-dev away from rolling mean %.2f (window=%d)",
                            value, z, mean, window.size());
                }
            }
        }

        // Check 2: hard physical limits (works even with no history)
        double[] bounds = ABSOLUTE_BOUNDS.get(reading.getSensorType());
        if (bounds != null && (value < bounds[0] || value > bounds[1])) {
            flagged = true;
            reason = String.format("Value %.2f is outside physical range [%.1f, %.1f]",
                    value, bounds[0], bounds[1]);
        }

        // Update the window AFTER checking, so a value doesn't influence its own check
        window.addLast(value);
        if (window.size() > WINDOW_SIZE) {
            window.removeFirst();
        }

        if (flagged) {
            anomalyRepository.save(new Anomaly(
                    reading.getMachineId(),
                    reading.getSensorType(),
                    value,
                    mean,
                    stdDev,
                    reason,
                    Instant.now()));
        }

        return flagged;
    }

    private double mean(Deque<Double> values) {
        double sum = 0;
        for (double v : values) {
            sum += v;
        }
        return sum / values.size();
    }

    private double stdDev(Deque<Double> values, double mean) {
        double sumSq = 0;
        for (double v : values) {
            sumSq += Math.pow(v - mean, 2);
        }
        return Math.sqrt(sumSq / values.size());
    }
}