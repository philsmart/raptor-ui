package uk.ac.cardiff.raptor.ui.secure;

import java.io.IOException;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.RequestContext;

import uk.ac.cardiff.raptor.ui.utils.CookieHelper;

@ManagedBean
@ViewScoped
public class DiscoIdpSelection {

	private static final Logger log = LoggerFactory.getLogger(DiscoIdpSelection.class);

	public static final String IDP_COOKIE_NAME = "raptor_remidp";

	private String selectedIdp;

	private String rememberedIdp;

	/**
	 * On construction, gets the local {@link RequestContext}, and finds the
	 * {@code IDP_COOKIE_NAME} cookie. If present, sets it as the selectedIdp.
	 */
	public DiscoIdpSelection() {

		final Map<String, Object> cookies = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestCookieMap();

		final Object cookie = cookies.get(IDP_COOKIE_NAME);

		if (cookie instanceof Cookie) {
			log.debug("Has found cookie [{}], {}", IDP_COOKIE_NAME, ((Cookie) cookie).getValue());
			final String decodedIdp = CookieHelper.decode6Base64EncodedCookie(((Cookie) cookie).getValue());
			setRememberedIdp(decodedIdp);
		}

	}

	public void doRememberedDiscoLoginRedirect() throws IOException {
		this.selectedIdp = rememberedIdp;
		doDiscoLoginRedirect();
	}

	/**
	 * Performs a redirect to /saml/login?disco=true&idp=<selectedIdp> when called.
	 * Also sets a cookie remembering which IdP was last selected.
	 * 
	 * @throws IOException
	 */
	public void doDiscoLoginRedirect() throws IOException {
		final Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();
		final String idpReturnParam = parameterMap.get("idpDiscoReturnParam");
		log.debug("Doing DISCO redirect for idp [{}] with return Param [{}]", selectedIdp, idpReturnParam);

		if (selectedIdp != null && selectedIdp.equals("") == false) {

			// set cookie for 1 week if does not already exist
			CookieHelper.setCookie(IDP_COOKIE_NAME, selectedIdp, 604800);

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/saml/login?disco=true&" + idpReturnParam + "=" + selectedIdp);
		} else {
			log.warn("No IdP selected, DISCO redirect not performed");
		}

	}

	/**
	 * @return the selectedIdp
	 */
	public String getSelectedIdp() {
		return selectedIdp;
	}

	/**
	 * @param selectedIdp
	 *            the selectedIdp to set
	 */
	public void setSelectedIdp(final String selectedIdp) {
		this.selectedIdp = selectedIdp;
	}

	/**
	 * @return the rememberedIdp
	 */
	public String getRememberedIdp() {
		return rememberedIdp;
	}

	/**
	 * @param rememberedIdp
	 *            the rememberedIdp to set
	 */
	public void setRememberedIdp(final String rememberedIdp) {
		this.rememberedIdp = rememberedIdp;
	}

}
