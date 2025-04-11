package io.github.mikalaid.examples.complex_conditions.domain.service;

import io.github.mikalaid.wenflon.core.Wenflon;

import java.util.UUID;

//@Wenflon //FINISHED HERE add pivot providers, add their bean names and actually try to get rid of them (names) (this will close one todo)
//todo test if package-private will work after everything else is working
public interface VetMedicineProvider {
    boolean placeOrder(UUID medicineId, int count);
}
