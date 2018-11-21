package org.esb.protocol;

import java.util.Map;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class ProtocolType {

    private String type;

    private String subType;

    private Map<String, String> parameters;

    public ProtocolType(String type, String subType,
                        Map<String, String> parameters) {
        this.type = type;
        this.subType = subType;
        this.parameters = parameters;
    }

    public ProtocolType(String type) {
        this(type, null, null);
    }

    public final static ProtocolType WSDL_TYPE = new ProtocolType("webservice");
    public final static String WSDL = "webservice";

    public final static ProtocolType REST_TYPE = new ProtocolType("restful");
    public final static String REST = "restful";
}
