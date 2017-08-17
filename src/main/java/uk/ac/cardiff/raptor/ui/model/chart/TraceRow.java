package uk.ac.cardiff.raptor.ui.model.chart;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TraceRow {

	private Date eventTime;

	private String principalName;

	private String resourceId;

	private String school;

	private String serviceId;

	private String serviceName;

	private Map<String, Object> fields;

	/**
	 * @return the rows
	 */
	public Map<String, Object> getFields() {
		if (fields == null) {
			fields = new HashMap<String, Object>();
		}
		return fields;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setFields(final Map<String, Object> fields) {
		this.fields = fields;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(final Date eventTime) {
		this.eventTime = eventTime;
	}

	/**
	 * @return the principalName
	 */
	public String getPrincipalName() {
		return principalName;
	}

	/**
	 * @param principalName
	 *            the principalName to set
	 */
	public void setPrincipalName(final String principalName) {
		this.principalName = principalName;
	}

	/**
	 * @return the resourceId
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * @param resourceId
	 *            the resourceId to set
	 */
	public void setResourceId(final String resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("TraceRow [eventTime=");
		builder.append(eventTime);
		builder.append(", principalName=");
		builder.append(principalName);
		builder.append(", resourceId=");
		builder.append(resourceId);
		builder.append(", fields=");
		builder.append(fields);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @return the school
	 */
	public String getSchool() {
		return school;
	}

	/**
	 * @param school
	 *            the school to set
	 */
	public void setSchool(final String school) {
		this.school = school;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId
	 *            the serviceId to set
	 */
	public void setServiceId(final String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
