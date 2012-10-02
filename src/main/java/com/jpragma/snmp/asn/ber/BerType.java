package com.jpragma.snmp.asn.ber;

public interface BerType {
    public static final byte BOOLEAN = 1;
    public static final byte INTEGER = 2;
    public static final byte BIT_STRING = 3;
    public static final byte OCTET_STRING = 4;
    public static final byte NULL = 5;
    public static final byte OID = 6;
    public static final byte SEQUENCE = 48;
    public static final byte IPADDRESS = 64;
    public static final byte COUNTER32 = 65;
    public static final byte GAUGE32 = 66;
    public static final byte TIME_TICKS = 67;
    public static final byte UINTEGER32 = 71;
    public static final byte GET_REQUEST_PDU = -96;
    public static final byte GET_NEXT_REQUEST_PDU = -95;
    public static final byte GET_RESPONSE_PDU = -94;
    public static final byte SET_REQUEST_PDU = -93;
    public static final byte TRAP_PDU = -92;
}
