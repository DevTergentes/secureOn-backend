package com.secureon.domain.services.validate;

import com.secureon.domain.model.valueobjects.InputDataSensor;

public class ValidateGasSensorType implements ValidateSensorStrategy {

    @Override
    public boolean validateSensorType(InputDataSensor input) {
        Float value = input.gasValue();
        return value != null && value >= 10.0 && value <= 40.0;

    }
}
