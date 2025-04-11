package io.github.mikalaid.examples.complex_conditions.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PetType {
    PANDA("panda"),
    DOG("dog"),
    GUINEA_PIG("guinea pig"),
    CHUPAKABRA ("chupakabra"),
    DUCK("duck");

    @Getter
    private String name;
}
