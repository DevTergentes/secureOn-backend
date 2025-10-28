package com.secureon.domain.services.validate;

import com.secureon.domain.model.valueobjects.InputDataSensor;

public class ValidateCoordinatesSensorType implements ValidateSensorStrategy {

    @Override
    public boolean validateSensorType(InputDataSensor input) {
        double latitude = input.latitude();
        double longitude = input.longitude();
        // Valida que ambos no sean null

//        boolean isValidLatitude = latitude >= -90.0 && latitude <= 90.0;
//        boolean isValidLongitude = longitude >= -180.0 && longitude <= 180.0;
//        return isValidLatitude && isValidLongitude;
        return true;
    }
}
