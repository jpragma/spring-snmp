package com.jpragma.snmp.asn;

import com.jpragma.snmp.asn.ber.BerTlv;

public class SmiUInteger32 extends SmiCounter32 {

    public static final byte TAG_NUMBER = 71;

    public SmiUInteger32() {
    }

    public SmiUInteger32(BerTlv tlv) {
        super(tlv);
    }

    public SmiUInteger32(long value) {
        super(value);
    }

    public void setValue(BerTlv tlv) {
        super.setValue(tlv);
    }

    public BerTlv toBerTlv() {
        return super.toBerTlv(71);
    }

    public String toString() {
        return "UInteger32: " + value;
    }

}
