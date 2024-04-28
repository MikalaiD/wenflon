package com.yosik.wenflon;

import com.yosik.wenflon.spring_tests.common.ServiceA;
import com.yosik.wenflon.spring_tests.common.Testable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

class WenflonRegistryTest {

    WenflonRegistry wenflonRegistry;
    Supplier<String> pivotProvider;

    @BeforeEach
    void setUp() {
        wenflonRegistry = new WenflonRegistry();
        pivotProvider = () -> "panda";
    }

    @Test
    void simple_registration() {
        wenflonRegistry.createAndRegisterWenflonProxy(Testable.class);
        Object wenflonProxy = wenflonRegistry.putBehindWenflon(Testable.class, new ServiceA());
        Assertions.assertThat(wenflonProxy).isNotNull();
    }
//    @Test
//    void registration_of_2_beans_for_same_wenflon() { //todo cover in spring test
//        //when
//        var wenflon1 = wenflonRegistry.putBehindWenflon(Testable.class, new ServiceA(), a->true);
//        var wenflon2 = wenflonRegistry.putBehindWenflon(Testable.class, new ServiceB(), a->false);
//
//        //then
//        Assertions.assertThat(wenflon1).isNotNull().isInstanceOf(Testable.class);
//        Assertions.assertThat(wenflon2).isNotNull().isInstanceOf(Testable.class);
//        Assertions.assertThat(wenflon1 == wenflon2).isEqualTo(true);
//    }
}