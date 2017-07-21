package me.fahimfarook.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.ContextRootAwareInstanceDiscovery;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.netflix.turbine.TurbineProperties;
import org.springframework.stereotype.Component;

import com.netflix.discovery.EurekaClient;

@EnableTurbine
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
public class TurbineApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurbineApplication.class, args);
	}
}

@Component
class LocalContextRootAwareInstanceDiscovery extends ContextRootAwareInstanceDiscovery {

	public LocalContextRootAwareInstanceDiscovery(final TurbineProperties turbineProperties, final EurekaClient eurekaClient) {
		super(turbineProperties, eurekaClient);
	}

}
