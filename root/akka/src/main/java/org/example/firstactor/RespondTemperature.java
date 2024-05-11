package org.example.firstactor;

import java.util.Optional;

public class RespondTemperature {
    final long requestId;
    final Optional<Double> value;

    public RespondTemperature(long requestId, Optional<Double> value) {
        this.requestId = requestId;
        this.value = value;
    }
}
