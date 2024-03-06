package com.yosik.wenflon;

import com.yosik.wenflon.test_classes.Notneeded;
import com.yosik.wenflon.test_classes.ServiceA;
import com.yosik.wenflon.test_classes.ServiceB;
import com.yosik.wenflon.test_classes.Testable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WenflonDynamicProxyTest {

    @Test
    void simpleTest() {
        WenflonDynamicProxy<Testable> wenflonDynamicProxy = new WenflonDynamicProxy<>();
        wenflonDynamicProxy.add(new ServiceA()); //tmp
        assertThat( wenflonDynamicProxy.createProxy().test()).isEqualTo( "ServiceA");
        assertThat( wenflonDynamicProxy.createProxy()).isInstanceOf(Testable.class);
    }
    @Test
    void onlyInterfacesCommonToAllImplementationsStay() {
        WenflonDynamicProxy<Testable> wenflonDynamicProxy = new WenflonDynamicProxy<>();
        wenflonDynamicProxy.add(new ServiceB()); //tmp
        wenflonDynamicProxy.add(new ServiceA()); //tmp
        assertThat(wenflonDynamicProxy.createProxy()).isInstanceOf(Testable.class);
        assertThat(wenflonDynamicProxy.createProxy()).isNotInstanceOf(Notneeded.class);
    }
}
