package com.jpragma.snmp.types;

import java.net.InetAddress;

import com.jpragma.snmp.asn.AsnInteger;
import com.jpragma.snmp.asn.AsnOID;
import com.jpragma.snmp.asn.SmiIPAddress;
import com.jpragma.snmp.asn.SmiTimeTicks;
import com.jpragma.snmp.asn.ber.BerTlv;

public class TrapPDU extends AbstractPDU {

    public static final byte TAG_NUMBER = -92;

    public TrapPDU(AsnOID oid, InetAddress agentAddress, int trapType, long specificTrapCode, SmiTimeTicks timeStamp,
            VarBindList varBindList) {
        add(0, oid);
        add(1, new SmiIPAddress(agentAddress));
        add(2, new AsnInteger(trapType));
        add(3, new AsnInteger(specificTrapCode));
        add(4, timeStamp);
        add(5, varBindList);
    }

    public TrapPDU(AsnOID oid, InetAddress agentAddress, long specificTrapCode, SmiTimeTicks timeStamp, VarBindList varBindList) {
        this(oid, agentAddress, 6, specificTrapCode, timeStamp, varBindList);
    }

    public TrapPDU(AsnOID oid, InetAddress agentAddress, int trapType, SmiTimeTicks timeStamp) {
        this(oid, agentAddress, trapType, 0L, timeStamp, new VarBindList());
    }

    public TrapPDU(BerTlv tlv) {
        super(tlv);
    }

    public AsnOID getOID() {
        return (AsnOID) get(0);
    }

    public InetAddress getAgentAddress() {
        return ((SmiIPAddress) get(1)).inetAddressValue();
    }

    public String getAgentAddressAsString() {
        return ((SmiIPAddress) get(1)).stringValue();
    }

    public int getTrapType() {
        return ((AsnInteger) get(2)).intValue();
    }

    public int getSpecificTrapCode() {
        return ((AsnInteger) get(3)).intValue();
    }

    public SmiTimeTicks getTimeStamp() {
        return (SmiTimeTicks) get(4);
    }

    public VarBindList getVarBindList() {
        return (VarBindList) get(5);
    }

    public BerTlv toBerTlv() {
        return super.toBerTlv(TAG_NUMBER);
    }

    public String toString() {
        StringBuffer pduString = new StringBuffer(getClass().getCanonicalName());
        pduString.append(": [\n");
        pduString.append("enterprise: ").append(getOID()).append("\n").append("agent-addr: ").append(get(1).toString()).append("\n")
                .append("generic-trap: ").append(get(2).toString()).append("\n").append("specific-trap: ").append(get(3).toString())
                .append("\n").append("time-stamp: ").append(get(4).toString()).append("\n").append("variable-bindings: ")
                .append(get(5).toString()).append("\n]");
        return super.toString();
    }

}
