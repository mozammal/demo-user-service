package org.example.util;

import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Map;

public class RetryTestProfile implements QuarkusTestProfile {
  @Override
  public Map<String, String> getConfigOverrides() {
    return Map.of("quarkus.test.profile.active", "retry-test");
  }
}
