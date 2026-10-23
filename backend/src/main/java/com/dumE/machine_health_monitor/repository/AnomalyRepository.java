package com.dumE.machine_health_monitor.repository;

import com.dumE.machine_health_monitor.model.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnomalyRepository extends JpaRepository<Anomaly, Long> {

    List<Anomaly> findTop20ByOrderByDetectedAtDesc();

    @Query("select a.machineId as machineId, count(a) as openCount "
         + "from Anomaly a where a.acknowledged = false group by a.machineId")
    List<MachineAnomalyCount> countOpenByMachine();
}