package uk.ac.cardiff.raptor.ui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cardiff.raptor.ui.utils.DateUtils;

@ManagedBean
@ViewScoped
public class Search {

	private static final Logger log = LoggerFactory.getLogger(Search.class);

	private Date from;

	private Date to;

	private String term;

	private boolean user = false;

	private boolean school = false;

	private boolean serviceProvider = false;

	private String type = "resource_id";

	private List<SelectItem> types = new ArrayList<SelectItem>();

	public String getSearchInputWidth() {
		if (school || user) {
			return "width:250px;";
		} else if (serviceProvider) {
			return "width:400px;";
		}
		return "width:250px;";
	}

	public String getSearchInputWidthInt() {
		if (school || user) {
			return "25";
		} else if (serviceProvider) {
			return "45";
		}
		return "30;";
	}

	public Search() {

		to = new Date();
		from = DateUtils.getStartOfYear();
		user = true;
		setType("resource_id");
		initSearchType();
	}

	public void setSearchOn(final String searchOn) {
		if (searchOn == null) {
			return;
		}
		switch (searchOn) {
		case "user":
			toggleUser();
			break;
		case "school":
			toggleSchool();
			break;
		case "resource":
			toggleServiceProvider();
			break;
		}
	}

	public String getSearchOn() {
		return null;
	}

	private void initSearchType() {
		final SelectItemGroup g1 = new SelectItemGroup("Group By");
		g1.setSelectItems(new SelectItem[] { new SelectItem("school", "School"),
				new SelectItem("resource_id", "Service Provider"), new SelectItem("principal_name", "User") });

		types.add(g1);

	}

	public void toggleUser() {
		setType("resource_id");
		user = true;
		school = false;
		serviceProvider = false;
	}

	public void toggleSchool() {
		setType("resource_id");
		user = false;
		school = true;
		serviceProvider = false;
	}

	public void toggleServiceProvider() {
		setType("school");
		user = false;
		school = false;
		serviceProvider = true;
	}

	/**
	 * @return the from
	 */
	public final Date getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public final void setFrom(final Date from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public final Date getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public final void setTo(final Date to) {
		this.to = to;
	}

	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * @param term
	 *            the term to set
	 */
	public void setTerm(final String term) {
		this.term = term;
	}

	/**
	 * @return the users
	 */
	public boolean isUser() {
		return user;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUser(final boolean user) {
		this.user = user;
	}

	/**
	 * @return the school
	 */
	public boolean isSchool() {
		return school;
	}

	/**
	 * @param school
	 *            the school to set
	 */
	public void setSchool(final boolean school) {
		this.school = school;
	}

	/**
	 * @return the serviceProvider
	 */
	public boolean isServiceProvider() {
		return serviceProvider;
	}

	/**
	 * @param serviceProvider
	 *            the serviceProvider to set
	 */
	public void setServiceProvider(final boolean serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Search [from=");
		builder.append(from);
		builder.append(", to=");
		builder.append(to);
		builder.append(", term=");
		builder.append(term);
		builder.append(", user=");
		builder.append(user);
		builder.append(", school=");
		builder.append(school);
		builder.append(", serviceProvider=");
		builder.append(serviceProvider);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * @return the types
	 */
	public List<SelectItem> getTypes() {
		return types;
	}

	/**
	 * @param types
	 *            the types to set
	 */
	public void setTypes(final List<SelectItem> types) {
		this.types = types;
	}

}
