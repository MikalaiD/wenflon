package io.github.mikalaid.examples.complex_conditions.application.service.prescriptions;

import io.github.mikalaid.examples.complex_conditions.application.port.out.FillInPrescriptionUseCasePort;
import io.github.mikalaid.examples.complex_conditions.domain.service.VetMedicineProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FillInPrescriptionUseCaseHandler implements FillInPrescriptionUseCasePort {

    private final VetMedicineProvider vetMedicineProvider;

    @Override
    public boolean fillIn(final Command command) {
        return vetMedicineProvider.placeOrder(command.medicineId(), command.count());
    }
}
