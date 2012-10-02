package com.jpragma.snmp.asn;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import com.jpragma.snmp.asn.ber.BerTlv;
import com.jpragma.snmp.asn.ber.BerTlvIdentifier;

public class AsnBitString extends AsnObject {

    public static final byte TAG_NUMBER = 3;

    protected byte value[];

    public AsnBitString() {
    }

    public AsnBitString(byte value[]) {
        this.value = value;
    }

    public AsnBitString(BerTlv tlv) {
        this();
        setValue(tlv);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) throws AsnObjectValueException {
        if (value instanceof byte[])
            this.value = (byte[]) (byte[]) value;
        else
            throw new AsnObjectValueException(getClass(), value.getClass(), byte[].class);
    }

    public void setValue(BerTlv tlv) {
        if (tlv.getTag().isPrimitiveEncoding()) {
            byte content[] = tlv.getValue();
            byte initialOctet = content[0];
            if (initialOctet < 0 || initialOctet > 7)
                throw new AsnParsingException("Initial octet of BIT_STRING must be between 0 and 7");
            if (initialOctet == 0 && content.length == 1) {
                value = new byte[0];
            } else {
                content[content.length - 1] >>= initialOctet;
                content[content.length - 1] <<= initialOctet;
                value = new byte[content.length - 1];
                for (int i = 1; i < content.length; i++)
                    value[i - 1] = content[i];

            }
        } else {
            value = new byte[0];
            for (ByteArrayInputStream stream = new ByteArrayInputStream(tlv.getValue()); stream.available() > 0;) {
                BerTlv childTlv = BerTlv.create(stream);
                if (childTlv.getTag().getTagValue() != 3)
                    throw new AsnParsingException("Nested TLV must be BIT_STRING for constructed BIT_STRING");
                AsnBitString childBitString = new AsnBitString(childTlv);
                byte buffer[] = new byte[value.length + childBitString.value.length];
                System.arraycopy(value, 0, buffer, 0, value.length);
                System.arraycopy(childBitString.value, 0, buffer, value.length, childBitString.value.length);
                value = buffer;
            }

        }
    }

    public BerTlv toBerTlv() {
        BerTlvIdentifier tag = new BerTlvIdentifier();
        tag.setTagValue(3);
        BerTlv tlv = new BerTlv();
        tlv.setTag(tag);
        if (value == null || value.length == 0) {
            tlv.setLength(1);
            tlv.setValue(new byte[] { 0 });
        } else {
            tlv.setLength(value.length + 1);
            byte tlvValueBuf[] = new byte[value.length + 1];
            tlvValueBuf[0] = 0;
            System.arraycopy(value, 0, tlvValueBuf, 1, value.length);
            tlv.setValue(tlvValueBuf);
        }
        return tlv;
    }

    public String toString() {
        return "BitString: " + Arrays.toString(value);
    }

}
