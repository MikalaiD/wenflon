package io.github.mikalaid.examples.complex_conditions.application.port.out.order;

import io.github.mikalaid.wenflon.core.Wenflon;

import java.util.UUID;

@Wenflon(pivotProviderBeanNames = {"chipInfoProvider", "petClassificatorProvider"})
//todo to get rid of pivotProviderBeanNames - technically, if this is provided in the
// properties then we can search by name and attach pivot provider to wenflon case
//todo test if package-private will work after everything else is working
public interface VetMedicineOrderProviderPort {
    boolean placeOrder(UUID medicineId, int count);
}
