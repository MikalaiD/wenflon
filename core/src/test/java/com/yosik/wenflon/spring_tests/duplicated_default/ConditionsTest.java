package com.yosik.wenflon.spring_tests.duplicated_default;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.yosik.wenflon.FinalAssemblerConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:conditions/application-test_duplicated_default.properties")
class ConditionsTest {

  @Test
  void testBeanCreationException() {
    assertThrows(BeanCreationException.class, () -> {
      try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
        context.register(TestConfig.class, FinalAssemblerConfig.class);
        context.refresh();
      }
    });
  }
}
