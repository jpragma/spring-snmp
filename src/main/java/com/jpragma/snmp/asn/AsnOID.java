package com.jpragma.snmp.asn;

import java.util.ArrayList;
import java.util.Iterator;

import com.jpragma.snmp.asn.ber.BerTlv;
import com.jpragma.snmp.asn.ber.BerTlvIdentifier;
import com.jpragma.snmp.util.BitManipulationHelper;

public class AsnOID extends AsnObject {

    public static final byte TAG_NUMBER = 6;
    protected int value[];

    public AsnOID() {
    }

    public AsnOID(int value[]) {
        this.value = value;
    }

    public AsnOID(String value) throws AsnObjectValueException {
        setValue(value);
    }

    public AsnOID(BerTlv tlv) {
        this();
        setValue(tlv);
    }

    public Object getValue() {
        return value;
    }

    public String stringValue() {
        StringBuffer oidString = new StringBuffer();
        if (value.length > 0)
            oidString.append(value[0]);
        for (int i = 1; i < value.length; i++)
            oidString.append(".").append(value[i]);

        return oidString.toString();
    }

    public void setValue(Object value) throws AsnObjectValueException {
        if (value instanceof int[])
            this.value = (int[]) (int[]) value;
        else if (value instanceof String) {
            String digits[] = ((String) value).split("\\.");
            if (digits.length == 0)
                throw new AsnObjectValueException(value.toString() + " is not valid OID");
            this.value = new int[digits.length];
            for (int i = 0; i < digits.length; i++)
                this.value[i] = Integer.parseInt(digits[i]);

        } else {
            throw new AsnObjectValueException(getClass(), value.getClass(), int[].class);
        }
    }

    public void setValue(BerTlv tlv) {
        byte buf[] = tlv.getValue();
        if (buf.length == 0)
            value = new int[0];
        ArrayList oidComponents = new ArrayList();
        byte mask = 127;
        int subIDValue = 0;
        int index = 0;
        index = 0;

        while (true) {
            if (index >= buf.length)
                break;
            subIDValue = subIDValue * 128 + (buf[index] & mask);
            if (!BitManipulationHelper.getBitValue(buf[index], 8)) {
                index++;
                break;
            }
            index++;
        }

        if (subIDValue < 40) {
            oidComponents.add(new Integer(0));
            oidComponents.add(new Integer(subIDValue));
        } else if (subIDValue < 80) {
            oidComponents.add(new Integer(1));
            oidComponents.add(new Integer(subIDValue - 40));
        } else {
            oidComponents.add(new Integer(2));
            oidComponents.add(new Integer(subIDValue - 80));
        }

        for (; index < buf.length; oidComponents.add(new Integer(subIDValue))) {
            subIDValue = 0;
            while (true) {
                if (index >= buf.length)
                    break;
                subIDValue = subIDValue * 128 + (buf[index] & mask);
                if (!BitManipulationHelper.getBitValue(buf[index], 8)) {
                    index++;
                    break;
                }
                index++;
            }
        }

        index = 0;
        value = new int[oidComponents.size()];
        for (Iterator iter = oidComponents.iterator(); iter.hasNext();)
            value[index++] = ((Integer) iter.next()).intValue();

    }

    public BerTlv toBerTlv() {
        BerTlvIdentifier tag = new BerTlvIdentifier();
        tag.setTagValue(6);
        BerTlv tlv = new BerTlv();
        tlv.setTag(tag);
        int x = value[0];
        int y = value.length <= 1 ? 0 : value[1];
        byte tlvValueBuf[] = new byte[0];
        for (int i = 1; i < value.length; i++) {
            int curValue = i != 1 ? value[i] : x * 40 + y;
            int subIdBytesNumber = 1;
            for (int j = curValue; (j = (int) Math.floor(j / 128)) > 0;)
                subIdBytesNumber++;

            byte subIdBytes[] = new byte[subIdBytesNumber];
            subIdBytes[subIdBytes.length - 1] = (byte) (curValue % 128);
            curValue = (int) Math.floor(curValue / 128);
            for (int b = subIdBytes.length - 2; b >= 0; b--) {
                subIdBytes[b] = (byte) (curValue % 128 + 128);
                curValue = (int) Math.floor(curValue / 128);
            }

            tlvValueBuf = BitManipulationHelper.mergeArrays(tlvValueBuf, subIdBytes);
        }

        tlv.setLength(tlvValueBuf.length);
        tlv.setValue(tlvValueBuf);
        return tlv;
    }

    public String toString() {
        StringBuffer oidString = new StringBuffer("Oid: ");
        oidString.append(stringValue());
        return oidString.toString();
    }

}
