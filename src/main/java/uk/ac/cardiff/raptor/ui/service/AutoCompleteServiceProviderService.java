package uk.ac.cardiff.raptor.ui.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import uk.ac.cardiff.raptor.ui.jdbc.UiHelperRepository;
import uk.ac.cardiff.raptor.ui.model.SystemSelection;
import uk.ac.cardiff.raptor.ui.model.SystemSelection.SYSTEM;

@Service
public class AutoCompleteServiceProviderService {

	private static final Logger log = LoggerFactory.getLogger(AutoCompleteServiceProviderService.class);

	@Inject
	UiHelperRepository uiRepo;

	@Inject
	private SearchToSqlMapper sqlMapper;

	/**
	 * List of known ServiceProviders (comes from {@link Event#resourceId}).
	 */
	private final Map<SYSTEM, List<String>> serviceProvidersList = new HashMap<SYSTEM, List<String>>();

	/**
	 * List of known departments/schools, comes from {@link Event#school}
	 */
	private final Map<SYSTEM, List<String>> departmentsList = new HashMap<SYSTEM, List<String>>();

	@PostConstruct
	public void init() {
		loadLists();
	}

	@Scheduled(fixedDelay = 3600000)
	public void loadLists() {

		// TODO for loop through all SYSTEM to generate all tables
		for (final SYSTEM system : SYSTEM.values()) {
			final String table = sqlMapper.mapToTableName(system);
			try {
				serviceProvidersList.put(system, uiRepo.findAllServiceProviders(table));

				departmentsList.put(system, uiRepo.findAllDepartments(table));
			} catch (final DataAccessException e) {
				log.warn("Could not generate autocomplete values for [{}]", table, e);
			}
		}
	}

	public List<String> completeServiceProvider(final String query) {

		final FacesContext context = FacesContext.getCurrentInstance();
		final SystemSelection system = context.getApplication().evaluateExpressionGet(context, "#{systemSelection}",
				SystemSelection.class);

		return serviceProvidersList.get(system.getSelected()).stream().filter(x -> x != null)
				.filter(x -> x.contains(query)).collect(Collectors.toList());

	}

	public List<String> completeDepartments(final String query) {

		final String queryLower = query.toLowerCase();

		final FacesContext context = FacesContext.getCurrentInstance();
		final SystemSelection system = context.getApplication().evaluateExpressionGet(context, "#{systemSelection}",
				SystemSelection.class);

		return departmentsList.get(system.getSelected()).stream().filter(x -> x != null)
				.filter(x -> x.toLowerCase().contains(queryLower)).collect(Collectors.toList());

	}

}
