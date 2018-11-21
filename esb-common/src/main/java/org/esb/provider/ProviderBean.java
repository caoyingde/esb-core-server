package org.esb.provider;


public class ProviderBean {

	private Class<?> interfaze;
	private Object implementor;
	private String[] protocols;
	
	public ProviderBean(Class<?> interfaze) {
		super();
		this.interfaze = interfaze;
	}

	public ProviderBean(Class<?> interfaze, String[] protocols) {
		super();
		this.interfaze = interfaze;
		this.protocols = protocols;
	}

	public ProviderBean(Class<?> interfaze, String[] protocols, Object implementor) {
		super();
		this.interfaze = interfaze;
		this.protocols = protocols;
		this.implementor = implementor;
	}

	public String getName() {
		return interfaze.getName();
	}

	public Class<?> getInterfaze() {
		return interfaze;
	}

	public void setInterfaze(Class<?> interfaze) {
		this.interfaze = interfaze;
	}

	public Object getImplementor() {
		return implementor;
	}

	public void setImplementor(Object implementor) {
		this.implementor = implementor;
	}

	public String[] getProtocols() {
		return protocols;
	}

	public void setProtocols(String[] protocols) {
		this.protocols = protocols;
	}

}
