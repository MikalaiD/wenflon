package io.github.mikalaid.examples.complex_conditions.adapters.rest.orders;

import io.github.mikalaid.examples.complex_conditions.application.port.out.order.VetMedicineOrderProviderPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("allPetPharmacy")
@Primary
public class AllPetPharmacyAdapter implements VetMedicineOrderProviderPort {
    @Override
    public boolean placeOrder(final UUID medicineId, final int count) {
        return false;
    }
}
