package uk.ac.cardiff.raptor.ui;

import javax.faces.webapp.FacesServlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = { "uk.ac.cardiff" })
@EnableAutoConfiguration
@EnableScheduling
public class RaptorUiApplication {

	public static void main(final String[] args) {
		SpringApplication.run(RaptorUiApplication.class, args);
	}

	/**
	 * Register the {@link FacesServlet} within the current
	 * {@link org.springframework.boot.web.servlet.ServletContextInitializer},
	 * and hence with the v3 {@link javax.servlet.Servlet} provider. This maps
	 * all URL patterns *.xhtml to the {@link FacesServlet}.
	 *
	 * @return a {@link ServletRegistrationBean} with registered
	 *         {@link FacesServlet}
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		final FacesServlet servlet = new FacesServlet();

		final ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet,
				new String[] { "*.xhtml" });
		servletRegistrationBean.setName("FacesServlet");
		servletRegistrationBean.setLoadOnStartup(1);

		return servletRegistrationBean;
	}

}
