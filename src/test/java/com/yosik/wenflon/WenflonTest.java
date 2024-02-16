package com.yosik.wenflon;

import com.yosik.wenflon.test_classes.Notneeded;
import com.yosik.wenflon.test_classes.ServiceA;
import com.yosik.wenflon.test_classes.ServiceB;
import com.yosik.wenflon.test_classes.Testable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WenflonTest {

    @Test
    void simpleTest() {
        Wenflon<Testable> wenflon = new Wenflon<>();
        wenflon.add(new ServiceA()); //tmp
        assertThat( wenflon.createProxy().test()).isEqualTo( "ServiceA");
        assertThat( wenflon.createProxy()).isInstanceOf(Testable.class);
    }    @Test
    void onlyInterfacesCommonToAllImplementationsStay() {
        Wenflon<Testable> wenflon = new Wenflon<>();
        wenflon.add(new ServiceB()); //tmp
        wenflon.add(new ServiceA()); //tmp
        assertThat(wenflon.createProxy()).isInstanceOf(Testable.class);
        assertThat(wenflon.createProxy()).isNotInstanceOf(Notneeded.class);
    }
}
