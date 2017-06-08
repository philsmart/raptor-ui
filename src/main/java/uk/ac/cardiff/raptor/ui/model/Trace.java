package uk.ac.cardiff.raptor.ui.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.timeline.TimelineModel;

import uk.ac.cardiff.raptor.ui.model.chart.TraceRows;

@ManagedBean
@ViewScoped
public class Trace {

	private String search;

	private int authDepth = 10;

	private TraceRows searchResult;

	private TimelineModel searchResultsTimeline;

	/**
	 * @return the search
	 */
	public String getSearch() {
		return search;
	}

	/**
	 * @param search
	 *            the search to set
	 */
	public void setSearch(final String search) {
		this.search = search;
	}

	/**
	 * @return the authDepth
	 */
	public int getAuthDepth() {
		return authDepth;
	}

	/**
	 * @param authDepth
	 *            the authDepth to set
	 */
	public void setAuthDepth(final int authDepth) {
		this.authDepth = authDepth;
	}

	/**
	 * @return the searchResult
	 */
	public TraceRows getSearchResult() {
		return searchResult;
	}

	/**
	 * @param searchResult
	 *            the searchResult to set
	 */
	public void setSearchResult(final TraceRows searchResult) {
		this.searchResult = searchResult;
	}

	/**
	 * @return the searchResultsTimeline
	 */
	public TimelineModel getSearchResultsTimeline() {
		return searchResultsTimeline;
	}

	/**
	 * @param searchResultsTimeline the searchResultsTimeline to set
	 */
	public void setSearchResultsTimeline(TimelineModel searchResultsTimeline) {
		this.searchResultsTimeline = searchResultsTimeline;
	}

}
