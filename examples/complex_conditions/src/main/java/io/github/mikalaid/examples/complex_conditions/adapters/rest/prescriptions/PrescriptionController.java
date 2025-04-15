package io.github.mikalaid.examples.complex_conditions.adapters.rest.prescriptions;

import io.github.mikalaid.examples.complex_conditions.application.port.in.FillInPrescriptionUseCasePort;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final FillInPrescriptionUseCasePort fillInPrescriptionUseCasePort;

    @PostMapping(path = "/fill-in")
    public ResponseEntity<Boolean> fillIn(@RequestBody final PrescriptionDto prescriptionDto) {
        final var command = new FillInPrescriptionUseCasePort.Command(prescriptionDto.getMedicineId(), prescriptionDto.getCount());
        return ResponseEntity.ok(fillInPrescriptionUseCasePort.fillIn(command));
    }
}
