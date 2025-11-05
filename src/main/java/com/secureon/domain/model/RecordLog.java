package com.secureon.domain.model;

import com.secureon.domain.model.valueobjects.InputDataSensor;
import com.secureon.domain.services.validate.ValidateSensorStrategyImplements;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "records")
public class RecordLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sensor_id", nullable = false)
    private long sensorId;

    @Column(name = "delivery_id", nullable = false)
    private long deliveryId;

    @Column(name = "gas_value")
    private float gasValue;

    @Column(name = "heart_rate_value", nullable = false)
    private Float heartRateValue;

    @Column(name = "temperature_value", nullable = false)
    private Float temperatureValue;

    @Column(name = "latitude", nullable = false)
    private double latitude;
    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    public String generateDescription(String place, Device sensor, InputDataSensor inputDataSensor) {
        String sensorValidation = ValidateSensorStrategyImplements.validateSensor(inputDataSensor);
        StringBuilder description = new StringBuilder();

        description.append("Device:").append(sensor.getId()).append(" | ");

        if (!"OK".equals(sensorValidation)) {
            description.append("Issues:").append(sensorValidation).append(" | ");
        }

        String shortPlace = place != null && place.length() > 100
                ? place.substring(0, 97) + "..."
                : place;
        description.append("Loc:").append(shortPlace != null ? shortPlace : "N/A");

        String result = description.toString();
        if (result.length() > 255) {
            result = result.substring(0, 252) + "...";
        }

        return result;
    }

}
