package com.yosik.wenflon;

import com.yosik.wenflon.test_classes.ServiceA;
import com.yosik.wenflon.test_classes.Testable;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class SimpleTest {

    @Test
    void simpleTest() {
        Wenflon<Testable> wenflon = new Wenflon<>();
        wenflon.add(new ServiceA()); //tmp
        assertThat( wenflon.createProxy().test()).isEqualTo( "Testable");
    }
}
