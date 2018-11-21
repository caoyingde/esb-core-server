package org.esb.common;

public enum Role {

	CORE("core"), PROVIDER("provider"), MONITOR("monitor"), CONSUMER("consumer"), SERVER(
			"server");

	private String text;

	private Role(String text) {
		this.text = text;
	}

	public String text() {
		return this.text;
	}

	public Role custom(String text) {
		return Role.valueOf(text);
	}
}
