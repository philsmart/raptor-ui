package uk.ac.cardiff.raptor.ui.model.chart;

public class GroupByResult {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GroupByResult [fieldName=" + fieldName + ", count=" + count + "]";
	}

	public String fieldName;

	private long count;

	/**
	 * @param fieldName
	 * @param count
	 */
	public GroupByResult(final String fieldName, final long count) {
		super();
		this.fieldName = fieldName;
		this.count = count;
	}

	public GroupByResult() {
		super();
	}

	/**
	 * @return the fieldName
	 */
	public final String getFieldName() {
		if (fieldName == null) {
			return "null";
		}
		return fieldName;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 */
	public final void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the count
	 */
	public final long getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public final void setCount(final long count) {
		this.count = count;
	}

}
