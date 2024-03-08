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
        Object wenflonProxy = wenflonRegistry.registerBehindWenflon(Testable.class, new ServiceA());
        Assertions.assertThat(wenflonProxy).isNotNull();
    }
    @Test
    void registration_of_2_beans_for_same_wenflon() {
        //when
        var wenflon1 = wenflonRegistry.registerBehindWenflon(Testable.class, new ServiceA());
        var wenflon2 = wenflonRegistry.registerBehindWenflon(Testable.class, new ServiceB());

        //then
        Assertions.assertThat(wenflon1).isNotNull().isInstanceOf(Testable.class);
        Assertions.assertThat(wenflon2).isNotNull().isInstanceOf(Testable.class);
        Assertions.assertThat(wenflon1 == wenflon2).isEqualTo(true);
    }
}