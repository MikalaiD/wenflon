package com.yosik.wenflon.spring_tests.bean_creation_one_implementation_to_many_proxies;

import com.yosik.wenflon.*;
import com.yosik.wenflon.spring_tests._common.ClassAsWenflon;
import com.yosik.wenflon.spring_tests._common.Testable;
import com.yosik.wenflon.spring_tests._common.TestableV2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(DynamicProxyProperties.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:bean_creation_one_implementation_to_many_proxies/application-test.properties")
public class BeanCreationTest {

  @Autowired Testable primaryTestable;
  @Autowired
  TestableV2 primaryTestableV2;

  @Autowired
  @Qualifier("testableA")
  Testable testableA;

  @Autowired
  @Qualifier("testableB")
  Testable testableB;
  @Autowired
  @Qualifier("testableB")
  TestableV2 testableBV2;
  @Autowired
  @Qualifier("testableC")
  TestableV2 testableC;

  @Autowired FinalAssembler finalAssembler;

  @Autowired
  DynamicProxyProperties properties;

  @Test()
  void beans_are_created_correctly() {
    Assertions.assertThat(primaryTestable).isNotNull();
    Assertions.assertThat(primaryTestable).isInstanceOf(Proxy.class);
    Assertions.assertThat(primaryTestableV2).isNotNull();
    Assertions.assertThat(primaryTestableV2).isInstanceOf(Proxy.class);
    Assertions.assertThat(primaryTestableV2).isNotEqualTo(primaryTestable);
    Assertions.assertThat(testableA).isNotNull();
    Assertions.assertThat(testableA).isNotInstanceOf(Proxy.class);
    Assertions.assertThat(testableB).isNotNull();
    Assertions.assertThat(testableB).isNotInstanceOf(Proxy.class);
    Assertions.assertThat(testableBV2).isNotNull();
    Assertions.assertThat(testableBV2).isNotInstanceOf(Proxy.class);
    Assertions.assertThat(testableBV2).isEqualTo(testableB);
    Assertions.assertThat(testableC).isNotNull();
    Assertions.assertThat(testableC).isNotInstanceOf(Proxy.class);

    Assertions.assertThat(testableA).isNotEqualTo(testableB);
    Assertions.assertThat(testableA).isNotEqualTo(testableC);
    Assertions.assertThat(testableA).isNotEqualTo(primaryTestable);
    Assertions.assertThat(testableB).isNotEqualTo(primaryTestable);
    Assertions.assertThat(testableBV2).isNotEqualTo(primaryTestableV2);
    Assertions.assertThat(testableC).isNotEqualTo(primaryTestableV2);
    Assertions.assertThat(properties.getConditions()).isNotNull();
    Assertions.assertThat(properties.getConditions().entrySet()).hasSize(3); //todo test what happens if one is missing - the should be false by default, maybe also to log it that no condition is found and default is used.
    Assertions.assertThat(finalAssembler).isNotNull();
  }
}
