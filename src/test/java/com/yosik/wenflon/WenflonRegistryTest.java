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
        Object wenflon = wenflonRegistry.registerBehindWenflon(Testable.class, new ServiceA());
        Assertions.assertThat(wenflon).isNotNull();
    }
    @Test
    void registration_of_2_beans_for_same_wenflon() {
        Object wenflon1 = wenflonRegistry.registerBehindWenflon(Testable.class, new ServiceA());
        Object wenflon2 = wenflonRegistry.registerBehindWenflon(Testable.class, new ServiceB());
        Assertions.assertThat(wenflon1).isNotNull();
        Assertions.assertThat(wenflon2).isNotNull();
        Assertions.assertThat(wenflon1).isEqualTo(wenflon2).isInstanceOf(WenflonDynamicProxyFactory.class);
//        Assertions.assertThat(((WenflonDynamicProxy)wenflon).getLatch()).isEqualTo(2);
    }
}