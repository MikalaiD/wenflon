package io.github.mikalaid.examples.complex_conditions.adapters.rest.orders;

import io.github.mikalaid.examples.complex_conditions.application.port.out.order.VetMedicineOrderProviderPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("healthyPets")
public class HealthyPetsAdapter implements VetMedicineOrderProviderPort {
    @Override
    public boolean placeOrder(final UUID medicineId, final int count) {
        return false;
    }
}
