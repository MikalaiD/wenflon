package io.github.mikalaid.examples.complex_conditions.adapters.rest.prescriptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PrescriptionDto {
     private UUID medicineId;
     private int count;
}
