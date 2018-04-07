package uk.ac.cardiff.raptor.ui.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import uk.ac.cardiff.raptor.ui.model.account.ServiceIDAuthZMapping;

@ManagedBean
@ViewScoped
public class AdminModel {

	/**
	 * Temporary storage of a serviceID to set onto a SAML EntityID
	 */
	private String newServiceId;

	/**
	 * Temporary storage of an {@link AuthSystem} to create a new mapping
	 */
	private String newAuthSystem;

	/**
	 * Temporary storage of an SAML EntityID to create a new mapping;
	 */
	private String newSamlId;

	/**
	 * Temporary storage of the mapping row selected.
	 */
	private ServiceIDAuthZMapping currentMappingRow;

	/**
	 * @return the newServiceId
	 */
	public String getNewServiceId() {
		return newServiceId;
	}

	/**
	 * @param newServiceId
	 *            the newServiceId to set
	 */
	public void setNewServiceId(final String newServiceId) {
		this.newServiceId = newServiceId;
	}

	/**
	 * @return the newAuthSystem
	 */
	public String getNewAuthSystem() {
		return newAuthSystem;
	}

	/**
	 * @param newAuthSystem
	 *            the newAuthSystem to set
	 */
	public void setNewAuthSystem(final String newAuthSystem) {
		this.newAuthSystem = newAuthSystem;
	}

	/**
	 * @return the newSamlId
	 */
	public String getNewSamlId() {
		return newSamlId;
	}

	/**
	 * @param newSamlId
	 *            the newSamlId to set
	 */
	public void setNewSamlId(final String newSamlId) {
		this.newSamlId = newSamlId;
	}

	/**
	 * @return the currentMappingRow
	 */
	public ServiceIDAuthZMapping getCurrentMappingRow() {
		return currentMappingRow;
	}

	/**
	 * @param currentMappingRow the currentMappingRow to set
	 */
	public void setCurrentMappingRow(ServiceIDAuthZMapping currentMappingRow) {
		this.currentMappingRow = currentMappingRow;
	}

}
