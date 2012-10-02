package com.jpragma.snmp.types;

public interface TrapType {

    public static final int COLD_START = 0;
    public static final int WARM_START = 1;
    public static final int LINK_DOWN = 2;
    public static final int LINK_UP = 3;
    public static final int AUTH_FAILURE = 4;
    public static final int EGP_NEIGHBOR_LOSS = 5;
    public static final int ENTERPRISE_SPECIFIC = 6;
}
