package com.jpragma.snmp.asn;

import com.jpragma.snmp.asn.ber.BerTlv;

public class SmiGauge32 extends SmiCounter32 {

    public static final byte TAG_NUMBER = 66;

    public SmiGauge32() {
    }

    public SmiGauge32(BerTlv tlv) {
        super(tlv);
    }

    public SmiGauge32(long value) {
        super(value);
    }

    public void setValue(BerTlv tlv) {
        super.setValue(tlv);
    }

    public BerTlv toBerTlv() {
        return super.toBerTlv(66);
    }

    public String toString() {
        return "Gauge32: " + value;
    }

}
