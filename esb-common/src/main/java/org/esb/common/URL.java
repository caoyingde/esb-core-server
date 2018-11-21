package org.esb.common;
/**
 * @deprecated
 * @author Andy.Cao
 * @date 2018-11-21
 */
public class URL {

	private String host;

	private int port = -1;

	private String protocol;

	private String path;

	public URL() {
		super();
	}

	public URL(String host, int port, String protocol, String path) {
		super();
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.path = path;
	}

	public String getFullPath() {
		String fullPath = protocol + "://" + host + ":" + port + "/";
		fullPath = path == null ? fullPath : (fullPath + path + "/");
		return fullPath;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
