package com.jpragma.snmp.asn.ber;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import com.jpragma.snmp.util.BitManipulationHelper;

public class BerTlvIdentifier {

    public static final byte TAG_CLASS_UNIVERSAL = 0;
    public static final byte TAG_CLASS_APPLICATION = 1;
    public static final byte TAG_CLASS_CONTEXT_SPECIFIC = 2;
    public static final byte TAG_CLASS_PRIVATE = 3;

    private byte value[];

    public boolean isPrimitiveEncoding() {
        if (value == null)
            return false;
        else
            return !BitManipulationHelper.getBitValue(value[0], 6);
    }

    public byte getTagClass() {
        if (value == null)
            return 0;
        if (!BitManipulationHelper.getBitValue(value[0], 8) && !BitManipulationHelper.getBitValue(value[0], 7))
            return 0;
        if (!BitManipulationHelper.getBitValue(value[0], 8) && BitManipulationHelper.getBitValue(value[0], 7))
            return 1;
        return ((byte) (!BitManipulationHelper.getBitValue(value[0], 8) || BitManipulationHelper.getBitValue(value[0], 7) ? 3 : 2));
    }

    public int getTagValue() {
        if (value == null)
            return 0;
        if (value.length == 1)
            return value[0];
        byte tagBytes[] = Arrays.copyOfRange(value, 1, value.length);
        for (int i = 0; i < tagBytes.length - 1; i++)
            tagBytes[i] = (byte) BitManipulationHelper.setBitValue(tagBytes[i], 8, false);

        return (new BigInteger(tagBytes)).intValue();
    }

    public void setTagValue(int tagValue) {
        if (tagValue >= -127 && tagValue <= 127)
            value = (new byte[] { (byte) tagValue });
        else
            value = BitManipulationHelper.removeLeadingZeroBytes(BitManipulationHelper.intToByteArray(tagValue));
    }

    public void decode(ByteArrayInputStream stream) {
        int tlvIdFirstOctet = stream.read();
        value = (new byte[] { (byte) tlvIdFirstOctet });
        int mask = 31;
        boolean lastOctet = false;
        if ((tlvIdFirstOctet & mask) == mask)
            while (!lastOctet) {
                int tlvIdNextOctet = stream.read();
                lastOctet = false;
                if (!BitManipulationHelper.getBitValue(tlvIdNextOctet, 8))
                    lastOctet = true;
                value = BitManipulationHelper.mergeArrays(value, new byte[] { (byte) tlvIdNextOctet });
            }
    }

    public void encode(ByteArrayOutputStream stream) {
        try {
            stream.write(getBytes());
        } catch (IOException e) {
            throw new BerParsingException(e);
        }
    }

    public byte[] getBytes() {
        return value;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof BerTlvIdentifier))
            return false;
        else
            return Arrays.equals(value, ((BerTlvIdentifier) obj).value);
    }

    public int hashCode() {
        return Arrays.hashCode(value);
    }

    public String toString() {
        if (value == null)
            return "NULL";
        StringBuffer buf = new StringBuffer("[");
        for (int i = 0; i < value.length; i++)
            buf.append("0x").append(Integer.toHexString(value[i])).append(" ");

        buf.append("]");
        return buf.toString();
    }

}
