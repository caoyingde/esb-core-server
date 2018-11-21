package org.esb.consumer;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class ConsumerBean {

    private Class<?> interfaze;
    private String[] protocols;

    public ConsumerBean(Class<?> interfaze) {
        super();
        this.interfaze = interfaze;
    }

    public ConsumerBean(Class<?> interfaze, String[] protocols) {
        super();
        this.interfaze = interfaze;
        this.protocols = protocols;
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

    public String[] getProtocols() {
        return protocols;
    }

    public void setProtocols(String[] protocols) {
        this.protocols = protocols;
    }
}
