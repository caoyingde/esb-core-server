package org.esb.server;

import java.io.Serializable;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public interface ServerEvent {

    public void run() throws Exception;

    void postRestart() throws Exception;

    public void postStop() throws Exception;

    public void preRestart() throws Exception;

    public void preStart() throws Exception;

    public static class RUN implements Serializable {

        private static final long serialVersionUID = 2924161713951777244L;

    }

    public static class STOP implements Serializable {
        private static final long serialVersionUID = -7496030329338095215L;

    }

    public void preRun() throws Exception;

    public void postRun() throws Exception;

}
