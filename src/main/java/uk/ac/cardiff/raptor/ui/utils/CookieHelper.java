package uk.ac.cardiff.raptor.ui.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHelper {

	/**
	 * Sets a cookie through the current
	 * {@link FacesContext#getCurrentInstance()} {@link FacesContext}. Sets The
	 * value on the existing cookie with name {@code cookieName} if present,
	 * otherwise creates a new cookie.
	 * 
	 * 
	 * @param cookieName
	 *            the name of the cookie.
	 * @param value
	 *            the value to add to the cookie
	 * @param age
	 *            how long the cookie is valid for in seconds.
	 */
	public static void setCookie(final String cookieName, final String value, final int age) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();

		final HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

		Cookie cookie = null;
		final Cookie[] userCookies = request.getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(cookieName)) {
					cookie = userCookies[i];
					break;
				}
			}
		}

		if (cookie != null) {
			cookie.setValue(value);
		} else {
			cookie = new Cookie(cookieName, value);
			cookie.setPath(request.getContextPath());
		}

		cookie.setMaxAge(age);

		final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.addCookie(cookie);
	}

}
