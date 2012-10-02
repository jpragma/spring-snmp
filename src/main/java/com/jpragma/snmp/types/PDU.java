package com.jpragma.snmp.types;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

import com.jpragma.snmp.asn.AsnInteger;
import com.jpragma.snmp.asn.AsnObject;
import com.jpragma.snmp.asn.AsnParsingException;
import com.jpragma.snmp.asn.AsnSequence;
import com.jpragma.snmp.asn.ber.BerTlv;

public abstract class PDU extends AbstractPDU {

    public PDU(long requestId, int errorStatus, long errorIndex, AsnSequence varBindList) {
        add(0, new AsnInteger(requestId));
        add(1, new AsnInteger(errorStatus));
        add(2, new AsnInteger(errorIndex));
        add(3, varBindList);
    }

    public PDU(long requestId, AsnSequence varBindList) {
        this(requestId, 0, 0L, varBindList);
    }

    public PDU(BerTlv tlv) {
        setValue(tlv);
    }

    public long getRequestId() {
        return ((BigInteger) get(0).getValue()).longValue();
    }

    public int getErrorStatus() {
        return ((BigInteger) get(1).getValue()).intValue();
    }

    public long getErrorIndex() {
        return ((BigInteger) get(2).getValue()).longValue();
    }

    public VarBindList getVarBindList() {
        return (VarBindList) get(3);
    }

    public void setValue(BerTlv tlv) {
        byte buf[] = tlv.getValue();
        ByteArrayInputStream stream = new ByteArrayInputStream(buf);

        BerTlv childTlv = new BerTlv();
        childTlv.decode(stream);
        AsnObject requestId = AsnObject.getInstance(childTlv);
        if (!(requestId instanceof AsnInteger))
            throw new AsnParsingException("Bad object type. Must be an instance of AsnInteger");
        add(0, requestId);

        childTlv = new BerTlv();
        childTlv.decode(stream);
        AsnObject errorStatus = AsnObject.getInstance(childTlv);
        if (!(errorStatus instanceof AsnInteger))
            throw new AsnParsingException("Bad object type. Must be an instance of AsnInteger");
        add(1, errorStatus);

        childTlv = new BerTlv();
        childTlv.decode(stream);
        AsnObject errorIndex = AsnObject.getInstance(childTlv);
        if (!(errorIndex instanceof AsnInteger)) {
            throw new AsnParsingException("Bad object type. Must be an instance of AsnInteger");
        }
        add(2, errorIndex);

        childTlv = new BerTlv();
        childTlv.decode(stream);
        VarBindList varBindList = new VarBindList(childTlv);
        add(3, varBindList);
    }

    public String toString() {
        StringBuffer pduString = new StringBuffer(getClass().getSimpleName());
        pduString.append(": [\n");
        pduString.append("request-id: ").append(getRequestId()).append("\n").append("error-status: ").append(getErrorStatus()).append("\n")
                .append("error-index: ").append(getErrorIndex()).append("\n").append("variable-bindings: ").append(get(3).toString())
                .append("]");
        return pduString.toString();
    }
}
