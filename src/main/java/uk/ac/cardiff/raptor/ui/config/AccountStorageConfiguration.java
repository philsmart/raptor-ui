package uk.ac.cardiff.raptor.ui.config;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.dizitart.no2.Nitrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsondb.JsonDBTemplate;
import uk.ac.cardiff.raptor.ui.model.account.ServiceIDAuthZMapping;
import uk.ac.cardiff.raptor.ui.secure.SAMLUserDetailsServiceImpl;
import uk.ac.cardiff.raptor.ui.store.NitriteServiceMappingsStorage;
import uk.ac.cardiff.raptor.ui.store.ServiceMappingsStorage;

@Configuration
@ConfigurationProperties(prefix = "admin.account.storage")
public class AccountStorageConfiguration {

	private static final Logger log = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

	private String dbFilesLocation;

	public enum StorageType {
		jsonDB, nitrite;

		@Override
		public String toString() {
			return this.name();
		}
	}

	/**
	 * The {@link ServiceMappingsStorage} type to use. Current version only supports
	 * a Nitrate type.
	 */
	private StorageType type = StorageType.nitrite;

	public JsonDBTemplate createJsonTemplateBean() {

		final String baseScanPackage = "uk.ac.cardiff.raptor.ui.model.account";

		final JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(dbFilesLocation, baseScanPackage);
		if (jsonDBTemplate.collectionExists(ServiceIDAuthZMapping.class) == false) {
			jsonDBTemplate.createCollection(ServiceIDAuthZMapping.class);
		}
		return jsonDBTemplate;

	}

	@Bean(name = "serviceMappingsStorage", initMethod = "init", destroyMethod = "destroy")
	public ServiceMappingsStorage createServiceMappinsStorage() {
		if (type == StorageType.nitrite) {
			log.info("Creating Nitrite ServiceMappingsStorage type, for file {}", dbFilesLocation + ".db");
			final NitriteServiceMappingsStorage storage = new NitriteServiceMappingsStorage(createNitriteDb());
			return storage;
		}
		throw new IllegalArgumentException(
				"StorageType defined in the properties file [admin.account.storage.type] should be of one of {"
						+ Arrays.stream(StorageType.values()).map(v -> v.toString()) + "}");
	}

	private Nitrite createNitriteDb() {
		final Nitrite db = Nitrite.builder().compressed().filePath(dbFilesLocation + ".db").openOrCreate();
		return db;
	}

	/**
	 * @return the dbFilesLocation
	 */
	public String getDbFilesLocation() {
		return dbFilesLocation;
	}

	/**
	 * @param dbFilesLocation
	 *            the dbFilesLocation to set
	 */
	public void setDbFilesLocation(final String dbFilesLocation) {
		this.dbFilesLocation = dbFilesLocation;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final String type) {
		boolean exists = false;
		for (final StorageType t : StorageType.values()) {
			if (t.name().equals(type)) {
				exists = true;
			}
		}
		if (exists == false) {
			throw new IllegalArgumentException(
					"StorageType defined in the properties file [admin.account.storage.type] should be of one of {"
							+ Arrays.stream(StorageType.values()).map(v -> v.toString())
									.collect(Collectors.joining(","))
							+ "}");
		}
		this.type = StorageType.valueOf(type);

	}

}
