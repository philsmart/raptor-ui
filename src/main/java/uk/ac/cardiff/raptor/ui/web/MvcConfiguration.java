package uk.ac.cardiff.raptor.ui.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(MvcConfiguration.class);

	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		log.debug("Creating a mapping between / and ui/search.xhtml");
		registry.addViewController("/").setViewName("redirect:/ui/search.xhtml");

	}

}
