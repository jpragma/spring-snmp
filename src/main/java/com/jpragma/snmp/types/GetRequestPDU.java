package com.jpragma.snmp.types;

import com.jpragma.snmp.asn.AsnParsingException;
import com.jpragma.snmp.asn.ber.BerTlv;

public class GetRequestPDU extends PDU {

    public static final byte TAG_NUMBER = -96;

    public GetRequestPDU(long requestId, int errorStatus, long errorIndex, VarBindList varBindList) {
        super(requestId, errorStatus, errorIndex, varBindList);
        if (errorStatus != 0 || errorIndex != 0L)
            throw new AsnParsingException("errorStatus and errorIndex must be 0 for GetRequestPDU");
    }

    public GetRequestPDU(long requestId, VarBindList varBindList) {
        super(requestId, varBindList);
    }

    public GetRequestPDU(BerTlv tlv) {
        super(tlv);
    }

    public BerTlv toBerTlv() {
        return super.toBerTlv(TAG_NUMBER);
    }

}
