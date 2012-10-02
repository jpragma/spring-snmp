package com.jpragma.snmp.asn;

import java.math.BigInteger;

import com.jpragma.snmp.asn.ber.BerTlv;
import com.jpragma.snmp.asn.ber.BerTlvIdentifier;
import com.jpragma.snmp.util.BitManipulationHelper;

public class AsnInteger extends AsnObject {

    public static final byte TAG_NUMBER = 2;

    protected BigInteger value;

    public AsnInteger() {
        this(0L);
    }

    public AsnInteger(long value) {
        this.value = BigInteger.valueOf(value);
    }

    public AsnInteger(BerTlv tlv) {
        this();
        setValue(tlv);
    }

    public Object getValue() {
        return value;
    }

    public int intValue() {
        return value.intValue();
    }

    public void setValue(Object value) throws AsnObjectValueException {
        if (value instanceof BigInteger)
            this.value = (BigInteger) value;
        else if (value instanceof Boolean)
            this.value = ((Boolean) value).booleanValue() ? BigInteger.valueOf(1L) : BigInteger.valueOf(0L);
        else if (value instanceof byte[])
            this.value = new BigInteger((byte[]) (byte[]) value);
        else
            try {
                this.value = new BigInteger(value.toString());
            } catch (NumberFormatException e) {
                throw new AsnObjectValueException(getClass(), value.getClass(), java.math.BigInteger.class);
            }
    }

    public void setValue(BerTlv tlv) {
        value = new BigInteger(tlv.getValue());
    }

    public BerTlv toBerTlv() {
        return toBerTlv(2);
    }

    public BerTlv toBerTlv(int tagNumber) {
        BerTlvIdentifier tag = new BerTlvIdentifier();
        tag.setTagValue(tagNumber);
        BerTlv tlv = new BerTlv();
        tlv.setTag(tag);
        byte tlvValueBuf[] = value.toByteArray();
        tlvValueBuf = BitManipulationHelper.removeLeadingZeroBytes(tlvValueBuf);
        tlv.setLength(tlvValueBuf.length);
        tlv.setValue(tlvValueBuf);
        return tlv;
    }

    public String toString() {
        return "Integer: " + value;
    }

}
