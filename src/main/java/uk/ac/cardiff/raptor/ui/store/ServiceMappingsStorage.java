/**
 * 
 */
package uk.ac.cardiff.raptor.ui.store;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import uk.ac.cardiff.raptor.ui.common.InitialiseDestroyComponent;
import uk.ac.cardiff.raptor.ui.model.account.ServiceIDAuthZMapping;

/**
 * 
 * <p>
 * Interface to support the storage and retrieval of serviceID mappings.
 * </p>
 * 
 * <p>
 * How the mappings are persisted is up to the implementation. But it must be
 * non-volotile.
 * </p>
 * 
 * <p>
 * The SAML entityID is used as the identifier for the mapping
 * </p>
 * 
 * @author philsmart
 *
 */
public interface ServiceMappingsStorage extends InitialiseDestroyComponent {

	/**
	 * Find the {@link ServiceIDAuthZMapping} from the SAML entity identifier.
	 * 
	 * @param entityID
	 *            the identifier of the SAML entity
	 * @return the {@link ServiceIDAuthZMapping} if present, an empty
	 *         {@link Optional} otherwise.
	 */
	Optional<ServiceIDAuthZMapping> findBySamlEntityId(@Nonnull String entityID);

	/**
	 * Insert a new {@link ServiceIDAuthZMapping}.
	 * 
	 * @param mapping
	 *            the {@link ServiceIDAuthZMapping} to add/save.
	 */
	void save(@Nonnull ServiceIDAuthZMapping mapping);

	/**
	 * Update the {@link ServiceIDAuthZMapping}.
	 * 
	 * @param mapping
	 *            the {@link ServiceIDAuthZMapping} to update.
	 */
	void update(@Nonnull ServiceIDAuthZMapping mapping);

	/**
	 * Removes a {@link ServiceIDAuthZMapping}
	 * 
	 * @param mapping
	 *            the {@link ServiceIDAuthZMapping} to remove.
	 */
	void remove(@Nonnull ServiceIDAuthZMapping mapping);

	/**
	 * Return all the {@link ServiceIDAuthZMapping}s. Will never be an empty list.
	 * 
	 * @return a {@link List} of found {@link ServiceIDAuthZMapping}s, will *never*
	 *         be null.
	 */
	@Nonnull
	List<ServiceIDAuthZMapping> findAll();

}
