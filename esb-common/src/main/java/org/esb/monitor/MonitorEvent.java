package org.esb.monitor;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.esb.akka.AkkaSeed;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public interface MonitorEvent {

    public static final String MONITOR_EVENT_TOPIC = "AKKA-ESB-MONITOR-EVENT-TOPIC";
    public static final String MONITOR_PASSIVE_EVENT_TOPIC = "AKKA-ESB-MONITOR-PASSIVE-EVENT-TOPIC";

    public static class ConsumerCallFailureEvent implements Serializable {
        private static final long serialVersionUID = 7184177039305427961L;

        private String nodeSeed;

        private String callServerName;

        private String callMethodName;

        public ConsumerCallFailureEvent(String nodeSeed, String callServerName,
                                        String callMethodName) {
            this.nodeSeed = nodeSeed;
            this.callMethodName = callMethodName;
            this.callServerName = callServerName;
        }

        public String getNodeSeed() {
            return nodeSeed;
        }

        public void setNodeSeed(String nodeSeed) {
            this.nodeSeed = nodeSeed;
        }

        public String getCallServerName() {
            return callServerName;
        }

        public void setCallServerName(String callServerName) {
            this.callServerName = callServerName;
        }

        public String getCallMethodName() {
            return callMethodName;
        }

        public void setCallMethodName(String callMethodName) {
            this.callMethodName = callMethodName;
        }

        @Override
        public String toString() {
            return nodeSeed + " CALL " + callMethodName + "【" + callServerName
                    + "】 Fail. ";
        }

    }

    public static class ProviderBeCalledCount implements Serializable {
        private static final long serialVersionUID = 3038220064657683245L;
        private String serverName;
        private Map<String, Long> countData;

        public ProviderBeCalledCount(String serverName,
                                     Map<String, Long> countData) {
            super();
            this.serverName = serverName;
            this.countData = countData;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public Map<String, Long> getCountData() {
            return countData;
        }

        public void setCountData(Map<String, Long> countData) {
            this.countData = countData;
        }
    }

    public static class NodeSeedJionEvent implements Serializable {
        private static final long serialVersionUID = 1940056693208182022L;
        private AkkaSeed akkaSeed;

        public NodeSeedJionEvent(AkkaSeed akkaSeed) {
            this.akkaSeed = akkaSeed;
        }

        public AkkaSeed getAkkaSeed() {
            return akkaSeed;
        }

        public void setAkkaSeed(AkkaSeed akkaSeed) {
            this.akkaSeed = akkaSeed;
        }

        @Override
        public String toString() {
            return "Node Seed Jion " + akkaSeed.toString();
        }
    }

    public static class NodeSeedLeaveEvent implements Serializable {
        private static final long serialVersionUID = 1373315734338620138L;
        private AkkaSeed akkaSeed;

        public NodeSeedLeaveEvent(AkkaSeed akkaSeed) {
            this.akkaSeed = akkaSeed;
        }

        public AkkaSeed getAkkaSeed() {
            return akkaSeed;
        }

        public void setAkkaSeed(AkkaSeed akkaSeed) {
            this.akkaSeed = akkaSeed;
        }

        @Override
        public String toString() {
            return "Node Seed Leave " + akkaSeed.toString();
        }
    }

    public static class NodeSeedAllEvent implements Serializable {
        private static final long serialVersionUID = -6377334942275916543L;
        private Set<AkkaSeed> akkaSeeds;

        public NodeSeedAllEvent(Set<AkkaSeed> akkaSeeds) {
            super();
            this.akkaSeeds = akkaSeeds;
        }

        public Set<AkkaSeed> getAkkaSeeds() {
            return akkaSeeds;
        }

        public void setAkkaSeeds(Set<AkkaSeed> akkaSeeds) {
            this.akkaSeeds = akkaSeeds;
        }
    }
}
