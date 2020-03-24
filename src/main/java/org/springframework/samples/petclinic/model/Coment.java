package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.MappedSuperclass;

import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
public class Coment extends BaseEntity {

    private String title;
    private String description;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate postDate;

    public String getTitle() {
	return this.title;
    }

    public void setTitle(final String title) {
	this.title = title;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(final String description) {
	this.description = description;
    }

    public LocalDate getPostDate() {
	return this.postDate;
    }

    public void setPostDate(final LocalDate postDate) {
	this.postDate = postDate;
    }

}
