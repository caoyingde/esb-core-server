package org.esb.akka;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import akka.cluster.Member;
/**
 * @author Andy.Cao
 * @date 2018-11-21
 */
public class AkkaSeed implements Serializable {

	private static final long serialVersionUID = 3573355594416259880L;

	private String host;

	private int port;

	private String protocol = AkkaEsbSystem.DEFAULT_PROTOCOL;

	private Set<String> roles;

	private String systemName;

	public AkkaSeed(Member member) {
		this.host = member.address().host().get();
		this.port = Integer.valueOf(member.address().port().get().toString());
		this.protocol = member.address().protocol();
		this.roles = new HashSet<String>();
		roles.addAll(member.getRoles());
		this.systemName = member.address().system();
	}

	public boolean hasRole(String role) {
		return roles.contains(role);
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

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		String role = "";
		for (Iterator<String> iterator = roles.iterator(); iterator.hasNext();) {
			String r = iterator.next();
			role += "," + r;
		}
		role = role.length() > 0 ? role.substring(1) : role;
		return protocol + "://" + systemName + "@" + host + ":" + port + " ["
				+ role + "]";
	}

}
