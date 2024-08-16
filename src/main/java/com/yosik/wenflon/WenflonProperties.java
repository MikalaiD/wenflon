package com.github.mikalaid.wenflon.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
<<<<<<<< HEAD:src/main/java/com/yosik/wenflon/WenflonProperties.java
import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "wenflon") // todo try with yaml
========

@ConfigurationProperties(prefix = "wenflon")
>>>>>>>> develop:core/src/main/java/com/github/mikalaid/wenflon/core/WenflonProperties.java
@Getter
@Setter
class WenflonProperties {
  private Map<String, List<String>> conditions;
}
