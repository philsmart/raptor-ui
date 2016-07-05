package uk.ac.cardiff.raptor.ui.model;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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

	public String getSearchInputWidth() {
		if (school || user) {
			return "width:200px;";
		} else if (serviceProvider) {
			return "width:400px;";
		}
		return "width:200px;";
	}

	public Search() {

		to = new Date();
		from = DateUtils.getStartOfYear();
		user = true;
	}

	public void toggleUser() {
		user = true;
		school = false;
		serviceProvider = false;
	}

	public void toggleSchool() {
		user = false;
		school = true;
		serviceProvider = false;
	}

	public void toggleServiceProvider() {
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

}
