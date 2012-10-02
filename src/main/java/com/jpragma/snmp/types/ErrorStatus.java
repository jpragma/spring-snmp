package com.jpragma.snmp.types;

public interface ErrorStatus {

    public static final int NO_ERROR = 0;
    public static final int TOO_BIG = 1;
    public static final int NO_SUCH_NAME = 2;
    public static final int BAD_VALUE = 3;
    public static final int READ_ONLY = 4;
    public static final int GEN_ERR = 5;
}
