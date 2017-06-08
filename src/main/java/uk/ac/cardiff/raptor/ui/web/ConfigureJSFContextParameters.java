package uk.ac.cardiff.raptor.ui.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureJSFContextParameters implements ServletContextInitializer {

	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {

		servletContext.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
		servletContext.setInitParameter("javax.faces.PARTIAL_STATE_SAVING_METHOD", "true");
		servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
		servletContext.setInitParameter("facelets.DEVELOPMENT", "true");
		servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");
		servletContext.setInitParameter("primefaces.FONT_AWESOME", "true");
		servletContext.setInitParameter("primefaces.THEME", "raptor");

		servletContext.setInitParameter("org.apache.myfaces.annotation.SCAN_PACKAGES", "uk.ac.cardiff.raptor.ui");

	}

}
