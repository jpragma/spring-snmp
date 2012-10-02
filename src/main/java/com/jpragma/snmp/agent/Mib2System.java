package com.jpragma.snmp.agent;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Mib2System {

    private String sysDescr;
    private String sysObjectId;
    private String sysContact;
    private String sysName;
    private String sysLocation;
    private boolean hasPhysicalService;
    private boolean hasDatalinkService;
    private boolean hasInternetService;
    private boolean hasEndToEndService;
    private boolean hasApplicationsService;
    private static long startTimestamp = System.currentTimeMillis();

    public Mib2System() {
    }

    public String getSysDescr() {
        return sysDescr;
    }

    public void setSysDescr(String sysDescr) {
        this.sysDescr = sysDescr;
    }

    public String getSysObjectId() {
        return sysObjectId;
    }

    public void setSysObjectId(String sysObjectId) {
        this.sysObjectId = sysObjectId;
    }

    public long getSysUpTime() {
        long upTimeMs = System.currentTimeMillis() - startTimestamp;
        return upTimeMs / 10L;
    }

    public String getSysContact() {
        return sysContact;
    }

    public void setSysContact(String sysContact) {
        this.sysContact = sysContact;
    }

    public String getSysName() {
        if (sysName != null)
            return sysName;
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysLocation() {
        return sysLocation;
    }

    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    public boolean isHasApplicationsService() {
        return hasApplicationsService;
    }

    public void setHasApplicationsService(boolean hasApplicationsService) {
        this.hasApplicationsService = hasApplicationsService;
    }

    public boolean isHasDatalinkService() {
        return hasDatalinkService;
    }

    public void setHasDatalinkService(boolean hasDatalinkService) {
        this.hasDatalinkService = hasDatalinkService;
    }

    public boolean isHasEndToEndService() {
        return hasEndToEndService;
    }

    public void setHasEndToEndService(boolean hasEndToEndService) {
        this.hasEndToEndService = hasEndToEndService;
    }

    public boolean isHasInternetService() {
        return hasInternetService;
    }

    public void setHasInternetService(boolean hasInternetService) {
        this.hasInternetService = hasInternetService;
    }

    public boolean isHasPhysicalService() {
        return hasPhysicalService;
    }

    public void setHasPhysicalService(boolean hasPhysicalService) {
        this.hasPhysicalService = hasPhysicalService;
    }

    public byte getSysServices() {
        byte sysServicesValue = 0;
        if (hasPhysicalService)
            sysServicesValue += calcLayerValue(1);
        if (hasDatalinkService)
            sysServicesValue += calcLayerValue(2);
        if (hasInternetService)
            sysServicesValue += calcLayerValue(3);
        if (hasEndToEndService)
            sysServicesValue += calcLayerValue(4);
        if (hasApplicationsService)
            sysServicesValue += calcLayerValue(7);
        return sysServicesValue;
    }

    private byte calcLayerValue(int layer) {
        return (byte) (int) Math.pow(2D, layer - 1);
    }

}
