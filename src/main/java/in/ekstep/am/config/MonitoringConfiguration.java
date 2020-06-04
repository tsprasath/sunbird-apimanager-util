package in.ekstep.am.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics(proxyTargetClass = true)
public class MonitoringConfiguration extends MetricsConfigurerAdapter {

  @Autowired
  private MetricRegistry metricRegistry;


  @PostConstruct
  public void init() {
    configureReporters(metricRegistry);
  }

  @Override
  public void configureReporters(MetricRegistry metricRegistry) {
    registerReporter(Slf4jReporter.forRegistry(metricRegistry).build()).start(1, TimeUnit.MINUTES);
  }

}