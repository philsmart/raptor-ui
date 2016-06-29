package uk.ac.cardiff.raptor.ui;

import javax.faces.webapp.FacesServlet;

import org.eclipse.jdt.internal.compiler.batch.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import uk.ac.cardiff.raptor.ui.web.ConfigureJSFContextParameters;

@Configuration
@ComponentScan(basePackages = { "uk.ac.cardiff" })
@EnableAutoConfiguration
public class RaptorUiApplication extends SpringBootServletInitializer {

	public static void main(final String[] args) {
		SpringApplication.run(RaptorUiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(new Class[] { Main.class, ConfigureJSFContextParameters.class });
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		final FacesServlet servlet = new FacesServlet();
		final ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "*.jsf");
		return servletRegistrationBean;
	}

}
