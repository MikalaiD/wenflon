package io.github.mikalaid.examples.complex_conditions.application.port.in;

import io.github.mikalaid.examples.complex_conditions.domain.PetType;

import java.util.UUID;

public interface FillInPrescriptionUseCasePort {
    boolean fillIn(Command command);

    record Command(UUID medicineId, int count){}
}
