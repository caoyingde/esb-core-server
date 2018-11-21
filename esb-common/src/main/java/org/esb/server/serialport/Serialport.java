package org.esb.server.serialport;

import gnu.io.SerialPort;

public class Serialport {
	
	private String portId;
	
	private String portName;
	
	private int expirationTime = 1000*3;
	
	private int speed = 9600;
	
	private int dataBits = SerialPort.DATABITS_8;
	
	private int stopBits = SerialPort.STOPBITS_1;
	
	private int parity = SerialPort.PARITY_NONE;
	
	public String getPortId() {
		return portId;
	}

	public void setPortId(String portId) {
		this.portId = portId;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public int getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(int expirationTime) {
		this.expirationTime = expirationTime;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDataBits() {
		return dataBits;
	}

	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}

	public int getStopBits() {
		return stopBits;
	}

	public void setStopBits(int stopBits) {
		this.stopBits = stopBits;
	}

	public int getParity() {
		return parity;
	}

	public void setParity(int parity) {
		this.parity = parity;
	}
	
}
