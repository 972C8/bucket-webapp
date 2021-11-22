/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
public class Avatar {

	@Id
	@GeneratedValue
	private Long id;
	@NotEmpty(message = "Please provide a username.")
	private String username;
	@Email(message = "Please provide a valid e-mail.")
	@NotEmpty(message = "Please provide an e-mail.")
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // only create object property from JSON
	@NotEmpty(message = "Please provide a password.")
	private String password;

	//TODO: remove role?
	@JsonIgnore
	private String role = "USER";

	@Transient // will not be stored in DB
	//stored as "true" or "false"
	private String remember;

	//TODO: Add bucketItems
	/*
	@OneToMany(mappedBy = "avatar")
	@JsonIgnore
	private List<Customer> bucketItems;
	*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		String transientPassword = this.password;
		this.password = null;
		return transientPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*
	public List<Customer> getBucketListItems() {
		return bucketItems;
	}

	public void setBucketListItems(List<Customer> customers) {
		this.bucketItems = customers;
	}
	*/

	public String getRemember() {
		return remember;
	}

	public void setRemember(String remember) {
		this.remember = remember;
	}

	public String getRole() {
		return role;
	}
}
