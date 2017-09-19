package uk.ac.cardiff.raptor.ui.model;

public class IdpDiscoSelectionTriple {

	private String entityId;

	private String logoUrl;

	private String orgName;

	public IdpDiscoSelectionTriple(final String logoUrl2, final String orgName2, final String idp) {
		entityId = idp;
		logoUrl = logoUrl2;
		orgName = orgName2;
	}

	public final String getEntityId() {
		return entityId;
	}

	public final void setEntityId(final String entityId) {
		this.entityId = entityId;
	}

	public final String getLogoUrl() {
		return logoUrl;
	}

	public final void setLogoUrl(final String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public final String getOrgName() {
		return orgName;
	}

	public final void setOrgName(final String orgName) {
		this.orgName = orgName;
	}

}
