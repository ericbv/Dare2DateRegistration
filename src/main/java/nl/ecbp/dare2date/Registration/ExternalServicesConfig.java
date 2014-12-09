package nl.ecbp.dare2date.Registration;

import nl.ecbp.dare2date.Registration.services.postcodecheck.PostcodeCheck;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microtripit.mandrillapp.lutung.MandrillApi;

@Configuration
public class ExternalServicesConfig {
	@Bean
	public PostcodeCheck postcodeCheck(){
		return new PostcodeCheck("7464706b76fa0e24638ba22114372f3c36808393");
	}
	
	
	@Bean
	public MandrillApi mail(){
		return new MandrillApi("RH6Vb1EeBEA4jDG4vFidpw");
	}
	
}
