package com.apress.prospringmvc.bookstore.domain;

/**
 * Object to hold the search criteria to search books.
 *
 * @author Marten Deinum
 */
public class BookSearchCriteria {

	private String title;
	private Category category;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
