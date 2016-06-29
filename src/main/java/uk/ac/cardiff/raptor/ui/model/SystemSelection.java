package uk.ac.cardiff.raptor.ui.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SystemSelection {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SystemSelection [shibboleth=" + shibboleth + ", ezproxy=" + ezproxy + "]";
	}

	private boolean shibboleth;

	private boolean ezproxy;

	public SystemSelection() {
		shibboleth = true;
		ezproxy = false;
	}

	public void toggleShibboleth() {
		shibboleth = true;
		ezproxy = false;
	}

	public void toggleEzproxy() {
		shibboleth = false;
		ezproxy = true;
	}

	/**
	 * @return the shibboleth
	 */
	public boolean isShibboleth() {
		return shibboleth;
	}

	/**
	 * @param shibboleth
	 *            the shibboleth to set
	 */
	public void setShibboleth(final boolean shibboleth) {
		this.shibboleth = shibboleth;
	}

	/**
	 * @return the ezproxy
	 */
	public boolean isEzproxy() {
		return ezproxy;
	}

	/**
	 * @param ezproxy
	 *            the ezproxy to set
	 */
	public void setEzproxy(final boolean ezproxy) {
		this.ezproxy = ezproxy;
	}

}
