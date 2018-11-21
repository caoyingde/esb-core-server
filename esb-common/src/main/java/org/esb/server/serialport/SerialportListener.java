package org.esb.server.serialport;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import org.apache.log4j.Logger;

public abstract class SerialportListener implements Runnable,
		SerialPortEventListener {

	private Logger logger = Logger.getLogger(SerialportListener.class);

	private CommPortIdentifier portId;

	private Serialport serialport;

	private Thread thread;

	private InputStream inputStream;

	private OutputStream outputStream;

	private SerialPort serialPort;

	public SerialportListener() {

	}

	final protected void setCommPortIdentifier(CommPortIdentifier portId) {
		this.portId = portId;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			try {
				byte[] tt = new byte[inputStream.available()];
				while (inputStream.available()>0) {
					inputStream.read(tt);
				}
				receive(tt);
			} catch (IOException e) {
			}
			break;
		}
	}

	public abstract void receive(byte[] data);

	public void write(byte[] data) throws IOException {
		logger.debug("Serialport write data:" + new String(data));
		outputStream.write(data);
	}

	public void start() {
		try {
			serialPort = (SerialPort) portId.open(serialport.getPortId(),
					serialport.getExpirationTime());
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			serialPort.notifyOnOutputEmpty(true);
			serialPort.setSerialPortParams(serialport.getSpeed(),
					serialport.getDataBits(), serialport.getStopBits(),
					serialport.getParity());
			thread = new Thread(this);
			thread.start();
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		} finally {
		}

	}

	protected void setSerialport(Serialport serialport) {
		this.serialport = serialport;
	}

}
