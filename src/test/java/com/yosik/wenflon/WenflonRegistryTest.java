package com.yosik.wenflon;

import com.yosik.wenflon.test_classes.ServiceA;
import com.yosik.wenflon.test_classes.Testable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WenflonRegistryTest {

    WenflonRegistry wenflonRegistry;

    @BeforeEach
    void setUp() {
        wenflonRegistry = new WenflonRegistry();
    }

    @Test
    void preRegister() {
        wenflonRegistry.preRegister(new ServiceA());
        Object wenflon = wenflonRegistry.getWenflon(Testable.class);
        Assertions.assertThat(wenflon).isNotNull();
    }

    @Test
    void getWenflon() {
    }
}