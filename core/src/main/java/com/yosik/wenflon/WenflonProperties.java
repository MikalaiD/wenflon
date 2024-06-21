package com.yosik.wenflon;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wenflon")
@Getter
@Setter
public class WenflonProperties {
  private Map<String, List<String>> conditions;
}
