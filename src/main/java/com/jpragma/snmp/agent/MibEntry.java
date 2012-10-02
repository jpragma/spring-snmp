package com.jpragma.snmp.agent;

import java.util.StringTokenizer;

public class MibEntry {

    public MibEntry() {
        writable = false;
    }

    public MibEntry(String oid, Object handlerBean, String handlerProperty, boolean writable, Class requiredType) {
        this(parseOID(oid), handlerBean, handlerProperty, writable, requiredType);
    }

    public MibEntry(String oid, Object handlerBean, String handlerProperty, Class requiredType) {
        this(oid, handlerBean, handlerProperty, false, requiredType);
    }

    private MibEntry(int oid[], Object handlerBean, String handlerProperty, boolean writable, Class requiredType) {
        this.writable = false;
        this.oid = oid;
        this.handlerBean = handlerBean;
        this.handlerProperty = handlerProperty;
        this.writable = writable;
        this.requiredType = requiredType;
    }

    private static int[] parseOID(String oid) {
        StringTokenizer tokenizer = new StringTokenizer(oid, ".");
        int numOfTokens = tokenizer.countTokens();
        int oidDigits[] = new int[numOfTokens];
        int curDigitIndex = 0;
        while (tokenizer.hasMoreTokens()) {
            String curToken = tokenizer.nextToken();
            int curDigit = Integer.parseInt(curToken);
            oidDigits[curDigitIndex++] = curDigit;
        }
        return oidDigits;
    }

    public int[] getOidDigits() {
        return oid;
    }

    public String getOid() {
        return oidDigitsToString(oid);
    }

    public void setOid(String oidString) {
        oid = parseOID(oidString);
    }

    public Object getHandlerBean() {
        return handlerBean;
    }

    public void setHandlerBean(Object handlerBean) {
        this.handlerBean = handlerBean;
    }

    public String getHandlerProperty() {
        return handlerProperty;
    }

    public void setHandlerProperty(String handlerProperty) {
        this.handlerProperty = handlerProperty;
    }

    public Class getRequiredType() {
        return requiredType;
    }

    public void setRequiredType(Class type) {
        requiredType = type;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    private static String oidDigitsToString(int oidDigits[]) {
        StringBuffer oid = new StringBuffer();
        for (int j = 0; j < oidDigits.length; j++) {
            oid.append(".");
            oid.append(oidDigits[j]);
        }

        return oid.toString();
    }

    public String toString() {
        return "MibEntry [OID:" + getOid() + ",HandlerBean:" + handlerBean.getClass().getName() + ",HandlerProperty:" + handlerProperty
                + ",RequiredType:" + requiredType + "]";
    }

    private int oid[];
    private Object handlerBean;
    private String handlerProperty;
    private boolean writable;
    private Class requiredType;
}
