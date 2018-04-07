package uk.ac.cardiff.raptor.ui.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cardiff.raptor.ui.model.account.NitriteIdentifiableMappingObject;
import uk.ac.cardiff.raptor.ui.model.account.ServiceIDAuthZMapping;

/**
 * A {@link ServiceMappingsStorage} implementation using Nitrite. Nitrate
 * {@link ObjectRepository} is threadsafe, this class delegates all function to
 * the {@link ObjectRepository} and is threadsafe.
 * 
 * <p>
 * {@link ServiceIDAuthZMapping} objects are saved and loaded as a
 * {@link NitriteIdentifiableMappingObject} so they maintain a link to their
 * NitriteId.
 * </p>
 * 
 * @author philsmart
 *
 */
@ThreadSafe
public class NitriteServiceMappingsStorage implements ServiceMappingsStorage {

	private static final Logger log = LoggerFactory.getLogger(NitriteServiceMappingsStorage.class);

	ObjectRepository<NitriteIdentifiableMappingObject> repository;

	private final Nitrite db;

	public NitriteServiceMappingsStorage(@Nonnull final Nitrite db) {
		Objects.requireNonNull(db);
		repository = db.getRepository(NitriteIdentifiableMappingObject.class);
		this.db = db;

	}

	@Override
	public Optional<ServiceIDAuthZMapping> findBySamlEntityId(final String entityID) {

		Objects.requireNonNull(entityID, "entityID must not be null");

		final Cursor<NitriteIdentifiableMappingObject> cursor = repository
				.find(ObjectFilters.eq("samlEntityId", entityID));
		if (cursor.size() == 1) {
			final NitriteIdentifiableMappingObject found = cursor.iterator().next();
			log.debug("Found service mapping for saml entityID [{}] with nitrite id [{}]", found.getSamlEntityId(),
					found.getNitriteId());
			return Optional.of(found);
		}
		log.error("Finding mapping for SAML entityID [{}] failed, result size was {} and needs to be 1", entityID,
				cursor.size());
		return Optional.empty();
	}

	/**
	 * <p>
	 * {@inheritDoc}.
	 * </p>
	 * 
	 * <p>
	 * First checks the input is a {@link NitriteIdentifiableMappingObject} object
	 * type. If so, it removes it from the Nitrite repository. Else it fails,
	 * throwing an {@link IncompatibleServiceMappingException}.
	 * </p>
	 */
	@Override
	public void remove(@Nonnull final ServiceIDAuthZMapping mapping) {

		Objects.requireNonNull(mapping, "mapping object must not be null");

		if (mapping instanceof NitriteIdentifiableMappingObject == false) {
			log.warn(
					"Mapping object was not a NitriteIdentifiableMappingObject and is not suitable for the Nitrite Service Storage. Could not remove mapping [{}]",
					mapping.getSamlEntityId());
			throw new IncompatibleServiceMappingException(
					"Could not remove service mapping [" + mapping.getSamlEntityId()
							+ "], not final a NitriteIdentifiableMappingObject, should final not happen");
		}

		final WriteResult removeResult = repository.remove((NitriteIdentifiableMappingObject) mapping);

		final int numberRemoved = removeResult.getAffectedCount();

		if (numberRemoved != 1) {
			log.warn(
					"Nothing was removed from the repository for saml entityId [{}] and UID [{}], this could be an error. Number removed was [{}]",
					mapping.getSamlEntityId(), ((NitriteIdentifiableMappingObject) mapping).getNitriteId(),
					numberRemoved);
		} else {
			log.info("Mapping for saml entityID [{}] and UID [{}] was succesfully removed from the repository",
					mapping.getSamlEntityId(), ((NitriteIdentifiableMappingObject) mapping).getNitriteId());
		}

	}

	/**
	 * <p>
	 * {@inheritDoc}.
	 * </p>
	 * 
	 * <p>
	 * First converts the input {@link ServiceIDAuthZMapping} to a
	 * {@link NitriteIdentifiableMappingObject} and adds a UUID by calling
	 * {@link NitriteIdentifiableMappingObject#setNitriteId(String)} before saving.
	 * </p>
	 */
	@Override
	public void save(@Nonnull final ServiceIDAuthZMapping mapping) {

		Objects.requireNonNull(mapping, "mapping object must not be null");

		final NitriteIdentifiableMappingObject nitrateMapping = new NitriteIdentifiableMappingObject(mapping);
		// add a uuid
		nitrateMapping.setNitriteId(UUID.randomUUID().toString());

		final WriteResult result = repository.insert(nitrateMapping);
		if (result.getAffectedCount() == 1) {
			log.debug("Mappings saved for SAML entityID [{}]", result.getAffectedCount(), mapping.getSamlEntityId());
		} else {
			log.error("Issue saving mapping for SAML EnitytID [{}]", mapping.getSamlEntityId());
		}

	}

	@Override
	public void update(@Nonnull final ServiceIDAuthZMapping mapping) {

		Objects.requireNonNull(mapping, "mapping object must not be null");

		if (mapping instanceof NitriteIdentifiableMappingObject) {
			log.debug("Updating serviceID mapping [{}]", mapping);
			final WriteResult result = repository.update((NitriteIdentifiableMappingObject) mapping);
			if (result.getAffectedCount() == 1) {
				log.debug("Mappings updated for SAML entityID [{}]", result.getAffectedCount(),
						mapping.getSamlEntityId());
			} else {
				log.error("Issue updating mapping for SAML EnitytID [{}]", mapping.getSamlEntityId());
			}
		} else {
			log.warn(
					"Mapping object was not a NitriteIdentifiableMappingObject and is not suitable for the Nitrite Service Storage");
			throw new IncompatibleServiceMappingException(
					"Could not remove service mapping, not a NitriteIdentifiableMappingObject, should not happen");
		}

	}

	@Override
	public List<ServiceIDAuthZMapping> findAll() {

		final List<ServiceIDAuthZMapping> foundMappings = new ArrayList<>();

		final Cursor<NitriteIdentifiableMappingObject> cursor = repository.find();

		for (final ServiceIDAuthZMapping mapping : cursor) {
			foundMappings.add(mapping);
		}
		return foundMappings;
	}

	@Override
	public void init() {
		// no-op method

	}

	@Override
	public void destroy() {
		if (db.isClosed() == false) {
			log.info("Closing Nitrite database, flushing unsaved changes to disk");
			db.close();
		}

	}

}
