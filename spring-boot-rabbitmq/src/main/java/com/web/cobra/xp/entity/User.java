package com.web.cobra.xp.entity;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 9193668712127900025L;
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
	}
}
