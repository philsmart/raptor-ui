
package uk.ac.cardiff.raptor.ui.model.quote;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "success", "contents" })
public class QuoteResponse {

	@JsonProperty("success")
	private Success success;
	@JsonProperty("contents")
	private Contents contents;
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("success")
	public Success getSuccess() {
		return success;
	}

	@JsonProperty("success")
	public void setSuccess(final Success success) {
		this.success = success;
	}

	@JsonProperty("contents")
	public Contents getContents() {
		return contents;
	}

	@JsonProperty("contents")
	public void setContents(final Contents contents) {
		this.contents = contents;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(final String name, final Object value) {
		this.additionalProperties.put(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("QuoteResponse [success=");
		builder.append(success);
		builder.append(", contents=");
		builder.append(contents);
		builder.append(", additionalProperties=");
		builder.append(additionalProperties);
		builder.append("]");
		return builder.toString();
	}

}
