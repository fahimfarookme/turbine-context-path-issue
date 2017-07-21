package me.fahimfarook.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
public class HystrixEurekaClientApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

	public static void main(String[] args) {
		SpringApplication.run(HystrixEurekaClientApplication.class, args);
	}
}

interface Api {

	@RequestMapping("/success")
	String success();

	@RequestMapping("/failure")
	String failure();

	String fallback();
}

@RestController
class Resource implements Api {

	@HystrixCommand(fallbackMethod = "fallback")
	@Override
	public String success() {
		return "Alhamdulillah!!";
	}

	@HystrixCommand(fallbackMethod = "fallback")
	@Override
	public String failure() {
		throw new IllegalStateException("Failure scenario!");
	}

	@Override
	public String fallback() {
		return "Fallbak";
	}
}
