package uk.ac.cardiff.raptor.ui.utils;

import java.util.Base64;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHelper {

	/**
	 * Sets a cookie through the current {@link FacesContext#getCurrentInstance()}
	 * {@link FacesContext}. Sets The value on the existing cookie with name
	 * {@code cookieName} if present, otherwise creates a new cookie.
	 * 
	 * 
	 * @param cookieName
	 *            the name of the cookie.
	 * @param value
	 *            the value to add to the cookie
	 * @param age
	 *            how long the cookie is valid for in seconds.
	 * 
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

		final byte[] encodedBase64 = Base64.getEncoder().encode(value.getBytes());
		final String base64EncodedString = new String(encodedBase64);

		if (cookie != null) {
			cookie.setValue(base64EncodedString);
			cookie.setHttpOnly(true);
			cookie.setSecure(false);
		} else {
			cookie = new Cookie(cookieName, base64EncodedString);
			cookie.setPath(request.getContextPath());
			cookie.setHttpOnly(true);
			cookie.setSecure(false);
		}

		cookie.setMaxAge(age);

		final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.addCookie(cookie);
	}

	public static String decode6Base64EncodedCookie(final String cookieValue) {
		final byte[] base64Decoded = Base64.getDecoder().decode(cookieValue.getBytes());
		return new String(base64Decoded);

	}

}
