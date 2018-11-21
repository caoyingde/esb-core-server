package org.example;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class TestPojo implements Serializable{

	private static final long serialVersionUID = -81889524682510274L;

	private String name;
	
	private int len;
	
	public TestPojo() {
	}

	public TestPojo(String name){
		this.name = name;
		this.len = name.length();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}
	
}
