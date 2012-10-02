package com.jpragma.snmp.types;

import com.jpragma.snmp.asn.ber.BerTlv;

public class GetResponsePDU extends PDU {

    public static final byte TAG_NUMBER = -94;

    public GetResponsePDU(long requestId, int errorStatus, long errorIndex, VarBindList varBindList) {
        super(requestId, errorStatus, errorIndex, varBindList);
    }

    public GetResponsePDU(long requestId, VarBindList varBindList) {
        super(requestId, varBindList);
    }

    public GetResponsePDU(BerTlv tlv) {
        super(tlv);
    }

    public BerTlv toBerTlv() {
        return super.toBerTlv(TAG_NUMBER);
    }

}
