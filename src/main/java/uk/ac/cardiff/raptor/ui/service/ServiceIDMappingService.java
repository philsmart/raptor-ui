package uk.ac.cardiff.raptor.ui.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import uk.ac.cardiff.raptor.ui.model.AuthSystem;
import uk.ac.cardiff.raptor.ui.model.account.ServiceIDAuthZMapping;
import uk.ac.cardiff.raptor.ui.store.ServiceMappingsStorage;

/**
 * <p>
 * Supports operations over the stored set of {@link ServiceIDAuthZMapping}s for
 * the UI.
 * </p>
 * 
 * <p>
 * Add and remove operations are synchronized. Hence, UI operations using this
 * class will be sequential.
 * </p>
 * 
 *
 */
@Service
public class ServiceIDMappingService {

	private static final Logger log = LoggerFactory.getLogger(ServiceIDMappingService.class);

	@Inject
	private ServiceMappingsStorage serviceIDMappings;

	public List<ServiceIDAuthZMapping> getMappings() {
		return serviceIDMappings.findAll();
	}

	public synchronized void saveNewSamlEntityId(final String newSamlEntityId) {
		final ServiceIDAuthZMapping newMapping = new ServiceIDAuthZMapping(newSamlEntityId, AuthSystem.SHIBBOLETH_IDP,
				newSamlEntityId);
		serviceIDMappings.save(newMapping);
	}

	public synchronized void removeSamlEntityIdMapping(final ServiceIDAuthZMapping mapping) {
		serviceIDMappings.remove(mapping);
	}

	/**
	 * Adds a serviceID to the SYSTEM it is applicable for to the existing
	 * {@link ServiceIDAuthZMapping} instance.
	 * 
	 * @param mapping
	 *            the {@link ServiceIDAuthZMapping} to add the
	 *            {@code serviceIDToAdd} to.
	 * @param serviceIDToAdd
	 *            the raptor serviceID to add.
	 */
	public synchronized void addMapping(final ServiceIDAuthZMapping mapping, final AuthSystem system,
			final String serviceIDToAdd) {

		log.info("Adding a new serviceID [{}] to the existing mapping for SAML entityID {}", serviceIDToAdd,
				mapping.getSamlEntityId());

		final Optional<ServiceIDAuthZMapping> foundMapping = serviceIDMappings
				.findBySamlEntityId(mapping.getSamlEntityId());

		log.trace("Found a mapping instance [{}] for samlEntityID {}", foundMapping.isPresent(),
				mapping.getSamlEntityId());

		if (foundMapping.isPresent() == false) {
			log.error("No ServiceIDMapping was found, can not add a mapping to it");
			return;
		}

		final ServiceIDAuthZMapping found = foundMapping.get();

		log.trace("found serviceID mapping to add new serviceID to, {}", found);

		addServiceID(found, system, serviceIDToAdd);

		log.debug("New serviceIDs for {} SAML entityID mapping is [{}]", found.getSamlEntityId(),
				found.getSystemToServiceIdMapping());

		serviceIDMappings.update(found);
	}

	/**
	 * Getter for UI elements to retrieve a simple list of {@link Entry}s.
	 * 
	 * @returnxs
	 */
	public List<Entry<String, List<String>>> getSystemToServiceIdMappingEntryList(final ServiceIDAuthZMapping mapping) {

		return new ArrayList(mapping.getSystemToServiceIdMapping().entrySet());
	}

	/**
	 * Expands the array of {@code serviceIds} to include the new {@code serviceID}.
	 * 
	 * @param mapping
	 *            the {@link ServiceIDAuthZMapping} to add the new authSystem
	 *            serviceID to.
	 * @param serviceID
	 *            the serviceID to add to the {@link AuthSystem} of the
	 *            {@link ServiceIDAuthZMapping}
	 */
	private void addServiceID(final ServiceIDAuthZMapping mapping, final AuthSystem authSystem,
			final String serviceID) {

		if (mapping.getSystemToServiceIdMapping().containsKey(authSystem.name())) {

			mapping.getSystemToServiceIdMapping().get(authSystem.name()).add(serviceID);

		} else {
			log.debug("ServiceID Mapping for [{}] does not contain AuthSystem [{}], adding it first",
					mapping.getSamlEntityId(), authSystem);
			mapping.getSystemToServiceIdMapping().put(authSystem.name(), Arrays.asList(new String[] { serviceID }));
		}

	}

	/**
	 * Removes *any* occurrence of {@code serviceID} {@link String} from the
	 * {@code serviceIds} list for the given {@link AuthSystem}. Removes the
	 * {@link AuthSystem} from the mapping entirely if it has no serviceID mappings.
	 * 
	 * @param serviceID
	 *            the {@link String} serviceID to remove from the the
	 *            {@link AuthSystem} of the {@link ServiceIDAuthZMapping}
	 */
	public void removeServiceID(final ServiceIDAuthZMapping mapping, final AuthSystem authSystem,
			final String serviceID) {

		mapping.getSystemToServiceIdMapping().get(authSystem.name()).remove(serviceID);

		// cleanup authsystem if no mappings
		if (mapping.getSystemToServiceIdMapping().get(authSystem.name()).size() == 0) {
			mapping.getSystemToServiceIdMapping().remove(authSystem.name());
		}

		log.debug("Has removed serviceID [{}], remaining array is {}", serviceID,
				mapping.getSystemToServiceIdMapping().get(authSystem.name()));

	}

	/**
	 * Removes the {@code serviceIDToAdd} from the existing
	 * {@link ServiceIDAuthZMapping} instance.
	 * 
	 * @param mapping
	 *            the {@link ServiceIDAuthZMapping} to remove the
	 *            {@code serviceIDToAdd} from.
	 * @param serviceIDToAdd
	 *            the raptor serviceID to remove.
	 */
	public synchronized void removeMapping(final ServiceIDAuthZMapping mapping, final AuthSystem authSystem,
			final String serviceIDToRemove) {
		log.info("Removing serviceID [{}] from the existing mapping for SAML entityID {}", serviceIDToRemove,
				mapping.getSamlEntityId());

		final Optional<ServiceIDAuthZMapping> foundMapping = serviceIDMappings
				.findBySamlEntityId(mapping.getSamlEntityId());

		log.trace("Found a mapping instance [{}] for samlEntityID {}", foundMapping.isPresent(),
				mapping.getSamlEntityId());

		if (foundMapping.isPresent() == false) {
			log.error("No ServiceIDMapping was found, can not remove a serviceID from it");
			return;
		}

		final ServiceIDAuthZMapping found = foundMapping.get();

		log.trace("found serviceID mapping to remove serviceID from, {}", found);
		removeServiceID(found, authSystem, serviceIDToRemove);

		serviceIDMappings.update(found);
	}

}
