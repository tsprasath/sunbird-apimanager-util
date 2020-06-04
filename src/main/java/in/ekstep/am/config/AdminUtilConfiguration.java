package in.ekstep.am.config;

import in.ekstep.am.external.AmAdminApi;
import in.ekstep.am.external.AmAdminApiClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@Profile("production")
public class AdminUtilConfiguration {
  @Autowired
  private Environment environment;

  @Bean("am.admin.api.client")
  public OkHttpClient amAdminApiClient() {
    return new OkHttpClient();
  }

  @Bean("am.admin.api.endpoint")
  public String amAdminApiEndpoint() {
    return environment.getProperty("am.admin.api.endpoint");
  }

  @Bean("am.admin.api")
  public AmAdminApi amAdminApi() {
    return new AmAdminApiClient();
  }

  @Bean("default.consumer.group")
  public String defaultConsumerGroup() {
    return environment.getProperty("default.consumer.group");
  }
}
