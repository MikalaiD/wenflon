package com.yosik.wenflon.spring_tests.bean_creation.duplicated_default;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yosik.wenflon.Config;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class DuplicatedDefaultsTest {


  @Test
  void testBeanCreationException_ifFinalAssemblerDiscoversMultipleDefaultConditions() {
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
    properties.put("wenflon.conditions.testableA", "panda, koala");
    properties.put("wenflon.conditions.testableB", "grizzly, white bear, default");
    properties.put("wenflon.conditions.testableC", "default");
    return properties;
  }
}
