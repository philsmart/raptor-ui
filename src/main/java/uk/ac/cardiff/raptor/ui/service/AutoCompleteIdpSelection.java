package uk.ac.cardiff.raptor.ui.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import uk.ac.cardiff.raptor.ui.model.IdpDiscoSelectionTriple;

@Service
public class AutoCompleteIdpSelection {

	private static final Logger log = LoggerFactory.getLogger(AutoCompleteIdpSelection.class);

	private List<IdpDiscoSelectionTriple> idps;

	/**
	 * @return the idps
	 */
	public List<IdpDiscoSelectionTriple> getIdps() {
		return idps;
	}

	/**
	 * @param idps
	 *            the idps to set
	 */
	public void setIdps(final List<IdpDiscoSelectionTriple> idps) {
		this.idps = idps;
	}

	public List<IdpDiscoSelectionTriple> completeIdentityProvider(final String query) {

		log.debug("Autocomplete idp disco selection has query [{}]", query);
		final List<IdpDiscoSelectionTriple> filtered = idps.stream()
				.filter(idp -> idp.getOrgName().toLowerCase().contains(query.toLowerCase()))
				.collect(Collectors.toList());
		log.debug("Autocomplete filtered contains {} idps", filtered.size());
		return filtered;
	}

}
