import random
import time

import requests

BACKEND_URL = "http://localhost:8080/api/sensors/readings"

MACHINES = ["MACHINE-A1", "MACHINE-B2", "MACHINE-C3"]

# (mean, standard deviation) of normal behavior for each sensor
NORMAL_PROFILE = {
    "TEMPERATURE": (65.0, 2.0),
    "VIBRATION": (3.0, 0.4),
    "RPM": (1500.0, 30.0),
}

ANOMALY_PROBABILITY = 0.05


def generate_reading(sensor_type):
    mean, std_dev = NORMAL_PROFILE[sensor_type]

    if random.random() < ANOMALY_PROBABILITY:
        direction = random.choice([1, -1])
        magnitude = random.uniform(6, 12)
        value = mean + direction * magnitude * std_dev
        print(f"  [simulator] injecting fault for {sensor_type}: {value:.2f}")
        return round(value, 2)

    return round(random.gauss(mean, std_dev), 2)


def send_reading(machine_id, sensor_type, value):
    payload = {
        "machineId": machine_id,
        "sensorType": sensor_type,
        "value": value,
    }
    try:
        response = requests.post(BACKEND_URL, json=payload, timeout=5)
        response.raise_for_status()
        data = response.json()
        flag = "  <-- ANOMALY" if data.get("anomaly") else ""
        print(f"{machine_id:12s} {sensor_type:12s} {value:9.2f}{flag}")
    except requests.exceptions.RequestException as error:
        print(f"  [simulator] could not send reading: {error}")


def main():
    print(f"Sending readings to {BACKEND_URL}. Press Ctrl+C to stop.\n")
    while True:
        for machine_id in MACHINES:
            for sensor_type in NORMAL_PROFILE:
                send_reading(machine_id, sensor_type, generate_reading(sensor_type))
        time.sleep(2)


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\nSimulation stopped.")