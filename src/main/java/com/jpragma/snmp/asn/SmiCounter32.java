package com.jpragma.snmp.asn;

import java.math.BigInteger;

import com.jpragma.snmp.asn.ber.BerTlv;

public class SmiCounter32 extends AsnInteger {
    public static final byte TAG_NUMBER = 65;
    private static final BigInteger MAX_VALUE = new BigInteger("4294967295");

    public SmiCounter32() {
        value = value.mod(MAX_VALUE);
    }

    public SmiCounter32(BerTlv tlv) {
        super(tlv);
        value = value.mod(MAX_VALUE);
    }

    public SmiCounter32(long value) {
        super(value);
        this.value = this.value.mod(MAX_VALUE);
    }

    public void setValue(Object value) throws AsnObjectValueException {
        super.setValue(value);
        this.value = this.value.mod(MAX_VALUE);
    }

    public BerTlv toBerTlv() {
        return super.toBerTlv(65);
    }

    public String toString() {
        return "Counter32: " + value;
    }

}
