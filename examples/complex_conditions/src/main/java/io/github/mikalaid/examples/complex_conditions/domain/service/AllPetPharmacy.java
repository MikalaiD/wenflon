package io.github.mikalaid.examples.complex_conditions.domain.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Primary
public class AllPetPharmacy implements VetMedicineProvider {
    @Override
    public boolean placeOrder(final UUID medicineId, final int count) {
        return false;
    }
}
