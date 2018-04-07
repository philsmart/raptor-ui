package uk.ac.cardiff.raptor.ui.model.account;

import javax.annotation.Nonnull;

import org.dizitart.no2.objects.Id;

/**
 * Extends a {@link ServiceIDAuthZMapping} object, adding a NitriteID
 * 
 * @author philsmart
 *
 */
public class NitriteIdentifiableMappingObject extends ServiceIDAuthZMapping {

	@Id
	private String nitriteId;

	public NitriteIdentifiableMappingObject(@Nonnull final ServiceIDAuthZMapping mapping) {
		super(mapping);

	}

	/**
	 * No-arg constructor
	 */
	public NitriteIdentifiableMappingObject() {
		super();
	}

	/**
	 * @return the nitriteId
	 */
	public String getNitriteId() {
		return nitriteId;
	}

	/**
	 * @param nitriteId
	 *            the nitriteId to set
	 */
	public void setNitriteId(final String nitriteId) {
		this.nitriteId = nitriteId;
	}

}
