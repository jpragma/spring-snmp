package com.jpragma.snmp.asn;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import com.jpragma.snmp.asn.ber.BerTlv;
import com.jpragma.snmp.asn.ber.BerTlvIdentifier;
import com.jpragma.snmp.util.BitManipulationHelper;

public class AsnOctetString extends AsnObject {

    public static final byte TAG_NUMBER = 4;
    protected byte value[];

    public AsnOctetString() {
        value = new byte[0];
    }

    public AsnOctetString(byte value[]) {
        this.value = value;
    }

    public AsnOctetString(String value) {
        this.value = value.getBytes();
    }

    public AsnOctetString(String value, Charset charset) {
        this.value = value.getBytes(charset);
    }

    public AsnOctetString(BerTlv tlv) {
        this();
        setValue(tlv);
    }

    public Object getValue() {
        return stringValue();
    }

    public String stringValue() {
        return new String(value);
    }

    public String stringValue(Charset charset) {
        return new String(value, charset);
    }

    public void setValue(Object value) throws AsnObjectValueException {
        if (value instanceof byte[])
            this.value = (byte[]) (byte[]) value;
        else if (value instanceof String)
            this.value = ((String) value).getBytes();
        else
            throw new AsnObjectValueException(getClass(), value.getClass(), byte[].class);
    }

    public void setValue(BerTlv tlv) {
        if (tlv.getTag().isPrimitiveEncoding()) {
            value = tlv.getValue();
        } else {
            for (ByteArrayInputStream stream = new ByteArrayInputStream(tlv.getValue()); stream.available() > 0;) {
                BerTlv childTlv = BerTlv.create(stream);
                if (childTlv.getTag().getTagValue() != 4)
                    throw new AsnParsingException("Nested TLV must be OCTET_STRING for constructed OCTET_STRING");
                AsnOctetString childOctetString = new AsnOctetString(childTlv);
                value = BitManipulationHelper.mergeArrays(value, childOctetString.value);
            }

        }
    }

    public BerTlv toBerTlv() {
        return toBerTlv(4);
    }

    protected BerTlv toBerTlv(int tagNumber) {
        BerTlvIdentifier tag = new BerTlvIdentifier();
        tag.setTagValue(tagNumber);
        BerTlv tlv = new BerTlv();
        tlv.setTag(tag);
        tlv.setLength(value.length);
        tlv.setValue(value);
        return tlv;
    }

    public String toString() {
        return "OctetString: " + stringValue();
    }

}
