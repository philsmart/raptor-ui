package uk.ac.cardiff.raptor.ui.utils;

import javax.faces.context.FacesContext;

public class JsfBeanHelper {

	@SuppressWarnings("unchecked")
	public static <T> T findBean(final String beanName) {
		final FacesContext context = FacesContext.getCurrentInstance();
		return (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
	}

}
