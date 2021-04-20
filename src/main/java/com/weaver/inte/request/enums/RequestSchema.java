package com.weaver.inte.request.enums;

public enum RequestSchema {
	http("http"), https("https");

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	RequestSchema(String name) {
		this.name = name;
	}

}
