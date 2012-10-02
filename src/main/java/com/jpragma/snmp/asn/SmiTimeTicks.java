package com.jpragma.snmp.asn;

import com.jpragma.snmp.asn.ber.BerTlv;

public class SmiTimeTicks extends SmiCounter32 {

    public static final byte TAG_NUMBER = 67;

    public SmiTimeTicks() {
    }

    public SmiTimeTicks(BerTlv tlv) {
        super(tlv);
    }

    public SmiTimeTicks(long value) {
        super(value);
    }

    public void setValue(BerTlv tlv) {
        super.setValue(tlv);
    }

    public BerTlv toBerTlv() {
        return super.toBerTlv(67);
    }

    public String toString() {
        return "TimeTicks: " + value;
    }

}
