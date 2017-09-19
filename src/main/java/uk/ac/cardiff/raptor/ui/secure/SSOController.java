package uk.ac.cardiff.raptor.ui.secure;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.ac.cardiff.raptor.ui.model.IdpDiscoSelectionTriple;
import uk.ac.cardiff.raptor.ui.service.AutoCompleteIdpSelection;
import uk.ac.cardiff.raptor.ui.service.MetadataHelperService;

@Controller
@RequestMapping("/saml")
public class SSOController {

	// Logger
	private static final Logger log = LoggerFactory.getLogger(SSOController.class);

	@Autowired
	private MetadataManager metadata;

	@Autowired
	private MetadataHelperService metaHelperService;

	@Autowired
	private AutoCompleteIdpSelection discoSelection;

	@RequestMapping(value = "/idpSelection", method = RequestMethod.GET)
	public String idpSelection(final HttpServletRequest request, final Model model) {

		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			log.warn("The current user is already logged in, directing to search page.");
			return "/ui/search.xhtml";
		} else {
			if (isForwarded(request)) {
				final Set<String> idps = metadata.getIDPEntityNames();
				final List<IdpDiscoSelectionTriple> idpsAndLogos = new ArrayList<>();
				for (final String idp : idps) {
					log.trace("Configured Identity Provider for SSO: " + idp);
					try {
						final String logoUrl = metaHelperService.getMduiLogo(idp).orElse("none");
						final String orgName = metaHelperService.getOrgDisplayName(idp).orElse(idp);

						idpsAndLogos.add(new IdpDiscoSelectionTriple(logoUrl, orgName, idp));
					} catch (final MetadataProviderException e) {
						log.warn("Could not lookup logo for entity " + idp);
					}
				}

				discoSelection.setIdps(idpsAndLogos);

				log.debug("Returning from idpSelection SSO Controller: /saml/idpselection");
				return "/saml/idpselection.xhtml";
			} else {
				log.warn("Direct accesses to '/idpSelection' route are not allowed");
				return "redirect:/";
			}
		}
	}

	/*
	 * Checks if an HTTP request has been forwarded by a servlet.
	 */
	private boolean isForwarded(final HttpServletRequest request) {
		if (request.getAttribute("javax.servlet.forward.request_uri") == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return the discoSelection
	 */
	public AutoCompleteIdpSelection getDiscoSelection() {
		return discoSelection;
	}

	/**
	 * @param discoSelection
	 *            the discoSelection to set
	 */
	public void setDiscoSelection(final AutoCompleteIdpSelection discoSelection) {
		this.discoSelection = discoSelection;
	}

}