package com.github.mikalaid.wenflon.core;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wenflon")
@Getter
@Setter
class WenflonProperties {
  private Map<String, List<String>> conditions;
}

