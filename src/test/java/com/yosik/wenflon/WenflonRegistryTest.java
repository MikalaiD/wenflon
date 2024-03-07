package com.yosik.wenflon;

import com.yosik.wenflon.test_classes.ServiceA;
import com.yosik.wenflon.test_classes.ServiceB;
import com.yosik.wenflon.test_classes.Testable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WenflonRegistryTest {

    WenflonRegistry wenflonRegistry;

    @BeforeEach
    void setUp() {
        wenflonRegistry = new WenflonRegistry();
    }

    @Test
    void simple_registration() {
        wenflonRegistry.registerBehindWenflon(new ServiceA());
        Object wenflon = wenflonRegistry.getWenflon(Testable.class);
        Assertions.assertThat(wenflon).isNotNull();
    }
    @Test
    void registration_of_2_beans_for_same_wenflon() {
        wenflonRegistry.registerBehindWenflon(new ServiceA());
        wenflonRegistry.registerBehindWenflon(new ServiceB());
        var wenflon = wenflonRegistry.getWenflon(Testable.class);
        Assertions.assertThat(wenflon).isNotNull();
        Assertions.assertThat(wenflon).isInstanceOf(WenflonDynamicProxy.class);
        Assertions.assertThat(((WenflonDynamicProxy)wenflon).getLatch()).isEqualTo(2);
    }
}