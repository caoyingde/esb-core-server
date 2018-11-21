package org.esb.server.serialport;

import gnu.io.CommPortIdentifier;

import java.io.IOException;
import java.util.Enumeration;

import org.esb.server.BaseServer;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public abstract class SerialportServer extends BaseServer {

    private CommPortIdentifier portId;

    private Serialport serialport;

    private SerialportListener serialportListener;

    public abstract Serialport getSerialport();

    public abstract void receive(byte[] data);

    public void write(byte[] data) throws IOException {
        serialportListener.write(data);
    }

    public SerialportListener getSerialportListener() {
        return new SerialportListener() {
            @Override
            public void receive(byte[] data) {
                SerialportServer.this.receive(data);
            }
        };

    }

    @Override
    public void preStart() throws Exception {
        serialport = getSerialport();
        serialportListener = getSerialportListener();

        @SuppressWarnings("unchecked")
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier
                .getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(serialport.getPortName())) {
                    serialportListener.setCommPortIdentifier(portId);
                    serialportListener.setSerialport(serialport);
                    serialportListener.start();
                }
            }
        }
    }

}
