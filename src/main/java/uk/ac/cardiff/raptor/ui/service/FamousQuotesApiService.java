package uk.ac.cardiff.raptor.ui.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import uk.ac.cardiff.raptor.ui.model.quote.QuoteResponse;

/**
 * A service to support the retrieval of famous quotes from the
 * https://quotes.rest service. Does so once a day at 6am.
 * 
 * @author philsmart
 *
 */
@Service
@ConfigurationProperties(prefix = "quotes.service")
public class FamousQuotesApiService {

	private static final Logger log = LoggerFactory.getLogger(FamousQuotesApiService.class);

	/**
	 * If the API should be accessed and the {@code lookupQuote} should be updated.
	 * Defaults to false. Also used by the UI to decide whether to show the quote.
	 */
	private boolean enabled = false;

	private String getQuoteEndpoint;

	private RestTemplate rest;

	private String lookupQuote;

	private String defaultQuote = "I propose to consider the question, 'Can machines think? A.M.Turing";

	@PostConstruct
	public void init() {
		rest = new RestTemplate();
	}

	@Scheduled(cron = "0 0 6 * * *")
	// @Scheduled(initialDelay = 100, fixedDelay = 5000)
	public void resolveQuote() {
		if (enabled) {
			try {
				log.debug("Looking up quote of the day using GET {}", getQuoteEndpoint);
				final QuoteResponse response = rest.getForObject(getQuoteEndpoint, QuoteResponse.class);
				log.debug("Has a response from the quote API, [{}]", response);

				if (response != null && response.getContents().getQuotes() != null
						&& response.getContents().getQuotes().size() > 0) {

					lookupQuote = response.getContents().getQuotes().get(0).getQuote() + "   ("
							+ response.getContents().getQuotes().get(0).getAuthor() + ")";

				}
			} catch (final RestClientResponseException | ResourceAccessException e) {
				log.error("Could not lookup famous quote API, either default or previous quote will be used", e);
			}

		}
	}

	public String getQuote() {
		if (lookupQuote == null) {
			return defaultQuote;
		}
		return lookupQuote;
	}

	/**
	 * @return the enabled
	 */
	public final boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public final void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the getQuoteEndpoint
	 */
	public final String getGetQuoteEndpoint() {
		return getQuoteEndpoint;
	}

	/**
	 * @param getQuoteEndpoint
	 *            the getQuoteEndpoint to set
	 */
	public final void setGetQuoteEndpoint(final String getQuoteEndpoint) {
		this.getQuoteEndpoint = getQuoteEndpoint;
	}

	/**
	 * @return the defaultQuote
	 */
	public String getDefaultQuote() {
		return defaultQuote;
	}

	/**
	 * @param defaultQuote
	 *            the defaultQuote to set
	 */
	public void setDefaultQuote(final String defaultQuote) {
		this.defaultQuote = defaultQuote;
	}

}
