package com.yosik.wenflon;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "wenflon") // todo try with yaml
@Getter
@Setter
public class DynamicProxyProperties {
  private Map<String, List<String>> conditions;
}
