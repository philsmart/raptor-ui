package uk.ac.cardiff.raptor.ui.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@SessionScoped
public class SystemSelection {

	private static final Logger log = LoggerFactory.getLogger(SystemSelection.class);

	// TODO not used system web yet, just DashboardScaler
	public enum SYSTEM {
		SHIBBOLETH,

		EZPROXY
	}

	private SYSTEM selected;

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
		selected = SYSTEM.SHIBBOLETH;
		ezproxy = false;
	}

	public void toggleShibboleth() {
		shibboleth = true;
		selected = SYSTEM.SHIBBOLETH;
		ezproxy = false;
	}

	public void toggleEzproxy() {
		shibboleth = false;
		selected = SYSTEM.EZPROXY;
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

	/**
	 * @return the selected
	 */
	public SYSTEM getSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(SYSTEM selected) {
		this.selected = selected;
	}

}
