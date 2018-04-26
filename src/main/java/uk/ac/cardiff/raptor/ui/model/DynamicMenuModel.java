package uk.ac.cardiff.raptor.ui.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.myfaces.config.annotation.Tomcat7AnnotationLifecycleProvider;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cardiff.raptor.ui.model.account.ServiceIDAuthZMapping;
import uk.ac.cardiff.raptor.ui.secure.RaptorUser;
import uk.ac.cardiff.raptor.ui.secure.SecurityContextHelper;
import uk.ac.cardiff.raptor.ui.utils.JsfBeanHelper;

/**
 * Generates a {@link MenuModel} per user session based on the client (users)
 * {@link ServiceIDAuthZMapping}s.
 * 
 * @author philsmart
 *
 */
@ManagedBean
@SessionScoped
public class DynamicMenuModel {

	private static final Logger log = LoggerFactory.getLogger(DynamicMenuModel.class);

	private MenuModel model;

	public DynamicMenuModel() {
		createMenuModel();
	}

	/**
	 * 
	 * <p>
	 * Create a new {@link MenuModel} for the primefaces view that includes all the
	 * authentication systems the current user (from
	 * {@link SecurityContextHelper#retrieveRaptorUser()} is authorized to use.
	 * </p>
	 * 
	 * <p>
	 * This is run on instantiation of this class. The class is a ManagedBean and is
	 * Sessionscoped, and hence will be created by the
	 * {@link Tomcat7AnnotationLifecycleProvider} for each new user session, and
	 * will persist for that users session.
	 * </p>
	 */
	public void createMenuModel() {
		final Optional<RaptorUser> user = SecurityContextHelper.retrieveRaptorUser();

		final SystemSelection selectedSystem = JsfBeanHelper.findBean("systemSelection");

		if (user.isPresent()) {
			log.info("Creating menu model dynamically for user [{}]", user.get().getUsername());

			model = new DefaultMenuModel();

			final ServiceIDAuthZMapping mapping = user.get().getServiceIdMappings();

			final Map<String, List<String>> mappings = mapping.getSystemToServiceIdMapping();

			for (final Map.Entry<String, List<String>> map : mappings.entrySet()) {
				final DefaultSubMenu submenu = new DefaultSubMenu(map.getKey());

				for (final String serviceId : map.getValue()) {
					final DefaultMenuItem item = new DefaultMenuItem(serviceId);

					item.setIcon("ui-icon-home");
					if (selectedSystem.getSelectedServiceId() != null
							&& selectedSystem.getSelectedServiceId().equals(serviceId)) {
						item.setStyle("background:#99e9ff");
					}

					item.setCommand(
							"#{systemSelection.setSelectedAuthSystem('" + map.getKey() + "','" + serviceId + "')}");

					item.setAjax(false);

					submenu.addElement(item);
				}

				model.addElement(submenu);
			}

		}

	}

	public void refreshMenuModal() {

		final SystemSelection selectedSystem = JsfBeanHelper.findBean("systemSelection");
		if (model != null) {
			for (final MenuElement element : model.getElements()) {
				if (element instanceof DefaultSubMenu) {
					final DefaultSubMenu subElement = new DefaultSubMenu();
					for (final MenuElement itemElement : subElement.getElements()) {
						if (itemElement instanceof DefaultMenuItem) {
							final DefaultMenuItem elem = new DefaultMenuItem();

							if (elem.getValue() != null
									&& elem.getValue().equals(selectedSystem.getSelectedServiceId())) {
								elem.setStyle("background:black;color:white");
							}
						}
					}
				}

			}
		}
	}

	/**
	 * @return the model
	 */
	public MenuModel getModel() {
		// refreshMenuModal();
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(final MenuModel model) {
		this.model = model;
	}
}
