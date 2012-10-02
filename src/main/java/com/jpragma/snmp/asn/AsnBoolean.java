package com.jpragma.snmp.asn;

import com.jpragma.snmp.asn.ber.BerTlv;
import com.jpragma.snmp.asn.ber.BerTlvIdentifier;

public class AsnBoolean extends AsnObject {

    public static final byte TAG_NUMBER = 1;

    protected boolean value;

    public AsnBoolean() {
        this(false);
    }

    public AsnBoolean(boolean value) {
        this.value = value;
    }

    public AsnBoolean(BerTlv tlv) {
        this();
        setValue(tlv);
    }

    public Object getValue() {
        return Boolean.valueOf(value);
    }

    public void setValue(Object value) throws AsnObjectValueException {
        if (value instanceof Boolean) {
            this.value = ((Boolean) value).booleanValue();
        } else {
            String strValue = value.toString();
            if (strValue.equals("0") || strValue.equalsIgnoreCase("false"))
                this.value = false;
            else if (strValue.length() == 1 && Character.isDigit(strValue.charAt(0)) || strValue.equalsIgnoreCase("true"))
                this.value = true;
            else
                throw new AsnObjectValueException(getClass(), value.getClass(), Boolean.TYPE);
        }
    }

    public void setValue(BerTlv tlv) {
        byte buf[] = tlv.getValue();
        if (buf.length != 1)
            throw new AsnParsingException("Length of provided byte[] doesn't match expected for this object type");
        if (buf[0] == 0)
            value = false;
        else
            value = true;
    }

    public BerTlv toBerTlv() {
        BerTlvIdentifier tag = new BerTlvIdentifier();
        tag.setTagValue(1);
        BerTlv tlv = new BerTlv();
        tlv.setTag(tag);
        tlv.setLength(1);
        tlv.setValue(new byte[] { ((byte) (value ? -1 : 0)) });
        return tlv;
    }

    public String toString() {
        return "Boolean: " + Boolean.toString(value);
    }

}
