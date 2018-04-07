package uk.ac.cardiff.raptor.ui.model.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cardiff.raptor.ui.model.AuthSystem;

public class ServiceIDAuthZMapping {

	private static final Logger log = LoggerFactory.getLogger(ServiceIDAuthZMapping.class);

	/**
	 * The entityId of the SAML Entity that authenticated the user. Used as a
	 * document ID.
	 */
	private String samlEntityId;

	/**
	 * The mapping between an {@link AuthSystem} and a List of serviceIDs.
	 */
	private Map<String, List<String>> systemToServiceIdMapping;

	public ServiceIDAuthZMapping() {
		super();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param that
	 *            the {@link ServiceIDAuthZMapping} to copy. Can not be null.
	 */
	public ServiceIDAuthZMapping(@Nonnull final ServiceIDAuthZMapping that) {
		Objects.requireNonNull(that);
		samlEntityId = that.samlEntityId;
		systemToServiceIdMapping = that.getSystemToServiceIdMapping().entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> new ArrayList<String>(e.getValue())));
	}

	/**
	 * Constructor when only a single serviceID is needed for construction.
	 * 
	 * @param samlEntityID
	 *            the identifier of the SAML entityID this mapping belongs to
	 * @param System
	 *            the {@link AuthSystem} this serviceId is applicable for
	 * @param serviceId
	 *            the serviceId to add to the System for the samlEntityID
	 */
	public ServiceIDAuthZMapping(final String samlEntityID, final AuthSystem System, final String serviceId) {
		super();
		this.samlEntityId = samlEntityID;
		final Map<String, List<String>> mapping = new HashMap<>();
		mapping.put(System.name(), Arrays.asList(new String[] { serviceId }));
		this.systemToServiceIdMapping = mapping;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ServiceIDAuthZMapping [samlEntityId=");
		builder.append(samlEntityId);
		builder.append(", SystemToServiceIdMapping=");
		builder.append(systemToServiceIdMapping);

		builder.append("]");
		return builder.toString();
	}

	/**
	 * @return the samlEntityId
	 */
	public String getSamlEntityId() {
		return samlEntityId;
	}

	/**
	 * @param samlEntityId
	 *            the samlEntityId to set
	 */
	public void setSamlEntityId(final String samlEntityId) {
		this.samlEntityId = samlEntityId;
	}

	/**
	 * @return the systemToServiceIdMapping
	 */
	public Map<String, List<String>> getSystemToServiceIdMapping() {
		return systemToServiceIdMapping;
	}

	/**
	 * @param systemToServiceIdMapping
	 *            the systemToServiceIdMapping to set
	 */
	public void setSystemToServiceIdMapping(final Map<String, List<String>> systemToServiceIdMapping) {
		this.systemToServiceIdMapping = systemToServiceIdMapping;
	}

}
