package com.github.mikalaid.wenflon.spring_tests.bean_creation.nonexisting_provider_declared;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class NonExistentProviderChosenTest {


  @Test
  void testBeanCreationException_ifWenflonSpecifiesNonExistentPivotProvider() {
    assertThatThrownBy(() -> {
      try (AnnotationConfigApplicationContext context =
                   new AnnotationConfigApplicationContext()) {
        context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("test-props", stubTestProperties()));
        context.register(TestConfig.class);
        context.refresh();
      }
    }).isInstanceOf(BeanCreationException.class);
  }

  private static HashMap<String, Object> stubTestProperties() {
    final HashMap<String, Object> properties = new HashMap<>();
    properties.put("wenflon.conditions.testableG", "panda, koala");
    properties.put("wenflon.conditions.testableH", "grizzly, white bear, default");
    return properties;
  }
}
