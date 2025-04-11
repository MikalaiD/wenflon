package io.github.mikalaid.examples.complex_conditions.adapters.rest.prescriptions;

import io.github.mikalaid.examples.complex_conditions.domain.PetType;
import lombok.Value;

import java.util.UUID;

@Value
public class PrescriptionDto {
     PetType petType;
     int age;
     UUID medicineId;
     int count;
}
