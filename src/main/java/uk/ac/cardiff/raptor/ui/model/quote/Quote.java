
package uk.ac.cardiff.raptor.ui.model.quote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "quote", "length", "author", "tags", "category", "date", "permalink", "title", "background",
		"id" })
public class Quote {

	@JsonProperty("quote")
	private String quote;
	@JsonProperty("length")
	private String length;
	@JsonProperty("author")
	private String author;
	@JsonProperty("tags")
	private List<String> tags = null;
	@JsonProperty("category")
	private String category;
	@JsonProperty("date")
	private String date;
	@JsonProperty("permalink")
	private String permalink;
	@JsonProperty("title")
	private String title;
	@JsonProperty("background")
	private String background;
	@JsonProperty("id")
	private String id;
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("quote")
	public String getQuote() {
		return quote;
	}

	@JsonProperty("quote")
	public void setQuote(final String quote) {
		this.quote = quote;
	}

	@JsonProperty("length")
	public String getLength() {
		return length;
	}

	@JsonProperty("length")
	public void setLength(final String length) {
		this.length = length;
	}

	@JsonProperty("author")
	public String getAuthor() {
		return author;
	}

	@JsonProperty("author")
	public void setAuthor(final String author) {
		this.author = author;
	}

	@JsonProperty("tags")
	public List<String> getTags() {
		return tags;
	}

	@JsonProperty("tags")
	public void setTags(final List<String> tags) {
		this.tags = tags;
	}

	@JsonProperty("category")
	public String getCategory() {
		return category;
	}

	@JsonProperty("category")
	public void setCategory(final String category) {
		this.category = category;
	}

	@JsonProperty("date")
	public String getDate() {
		return date;
	}

	@JsonProperty("date")
	public void setDate(final String date) {
		this.date = date;
	}

	@JsonProperty("permalink")
	public String getPermalink() {
		return permalink;
	}

	@JsonProperty("permalink")
	public void setPermalink(final String permalink) {
		this.permalink = permalink;
	}

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(final String title) {
		this.title = title;
	}

	@JsonProperty("background")
	public String getBackground() {
		return background;
	}

	@JsonProperty("background")
	public void setBackground(final String background) {
		this.background = background;
	}

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(final String id) {
		this.id = id;
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
		builder.append("Quote [quote=");
		builder.append(quote);
		builder.append(", length=");
		builder.append(length);
		builder.append(", author=");
		builder.append(author);
		builder.append(", tags=");
		builder.append(tags);
		builder.append(", category=");
		builder.append(category);
		builder.append(", date=");
		builder.append(date);
		builder.append(", permalink=");
		builder.append(permalink);
		builder.append(", title=");
		builder.append(title);
		builder.append(", background=");
		builder.append(background);
		builder.append(", id=");
		builder.append(id);
		builder.append(", additionalProperties=");
		builder.append(additionalProperties);
		builder.append("]");
		return builder.toString();
	}

}
