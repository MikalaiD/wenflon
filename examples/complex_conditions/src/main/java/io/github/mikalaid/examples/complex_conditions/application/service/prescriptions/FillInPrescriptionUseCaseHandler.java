package io.github.mikalaid.examples.complex_conditions.application.service.prescriptions;

import io.github.mikalaid.examples.complex_conditions.application.port.in.FillInPrescriptionUseCasePort;
import io.github.mikalaid.examples.complex_conditions.application.port.out.order.VetMedicineOrderProviderPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FillInPrescriptionUseCaseHandler implements FillInPrescriptionUseCasePort {

    private final VetMedicineOrderProviderPort vetMedicineOrderProvider;

    @Override
    public boolean fillIn(final Command command) {
        return vetMedicineOrderProvider.placeOrder(command.medicineId(), command.count());
    }
}
