package com.secureon.domain.services.validate;

import com.secureon.domain.model.valueobjects.InputDataSensor;

public interface ValidateSensorStrategy {

    public boolean validateSensorType(InputDataSensor input);
}
