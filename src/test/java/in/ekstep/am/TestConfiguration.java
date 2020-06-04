package in.ekstep.am;

import in.ekstep.am.config.AdminUtilConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfiguration extends AdminUtilConfiguration {

  @Bean("am.admin.api.endpoint")
  public String amAdminApiEndpoint() {
    return "http://localhost:3010";
  }
}
