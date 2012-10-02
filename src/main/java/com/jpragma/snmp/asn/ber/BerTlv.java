package com.jpragma.snmp.asn.ber;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.jpragma.snmp.util.BitManipulationHelper;

public class BerTlv {

    private BerTlvIdentifier tag;
    private int length;
    private byte value[];

    public BerTlvIdentifier getTag() {
        return tag;
    }

    public void setTag(BerTlvIdentifier tag) {
        this.tag = tag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte value[]) {
        this.value = value;
    }

    public static BerTlv create(ByteArrayInputStream stream) {
        BerTlv tlv = new BerTlv();
        tlv.decode(stream);
        return tlv;
    }

    public void decode(byte data[]) {
        decode(new ByteArrayInputStream(data));
    }

    public void decode(ByteArrayInputStream stream) {
        tag = new BerTlvIdentifier();
        tag.decode(stream);

        int tmpLength = stream.read();
        if (tmpLength <= 127)
            length = tmpLength;
        else if (tmpLength == 128) {
            length = tmpLength;
        } else {
            int numberOfLengthOctets = tmpLength & 0x7f;
            tmpLength = 0;
            for (int i = 0; i < numberOfLengthOctets; i++) {
                int nextLengthOctet = stream.read();
                tmpLength <<= 8;
                tmpLength |= nextLengthOctet;
            }

            length = tmpLength;
        }

        if (length == 128) {
            stream.mark(0);
            int prevOctet = 1;
            int curOctet = 0;
            int len = 0;
            while (true) {
                len++;
                curOctet = stream.read();
                if (prevOctet == 0 && curOctet == 0)
                    break;
                prevOctet = curOctet;
            }
            len -= 2;
            value = new byte[len];
            stream.reset();
            stream.read(value, 0, len);
            length = len;
        } else {
            value = new byte[length];
            stream.read(value, 0, length);
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
        byte tagBytes[] = tag.getBytes();
        byte lengthBytes[];
        if (value.length < 128) {
            lengthBytes = (new byte[] { (byte) value.length });
        } else {
            byte tmpLengthBytes[] = BitManipulationHelper.intToByteArray(value.length);
            int numOfLengthOctets = tmpLengthBytes.length;
            for (int i = 0; i < tmpLengthBytes.length && tmpLengthBytes[i] == 0; i++)
                numOfLengthOctets--;

            lengthBytes = new byte[numOfLengthOctets + 1];
            lengthBytes[0] = (byte) numOfLengthOctets;
            lengthBytes[0] |= 0x80;
            int curLengthBytesIdx = 1;
            for (int i = tmpLengthBytes.length - numOfLengthOctets; i < tmpLengthBytes.length; i++)
                lengthBytes[curLengthBytesIdx++] = tmpLengthBytes[i];

        }
        byte content[] = new byte[tagBytes.length + lengthBytes.length + value.length];
        System.arraycopy(tagBytes, 0, content, 0, tagBytes.length);
        System.arraycopy(lengthBytes, 0, content, tagBytes.length, lengthBytes.length);
        System.arraycopy(value, 0, content, tagBytes.length + lengthBytes.length, value.length);
        return content;
    }

    public String toString() {
        return "[TLV: ID=" + tag + ";Length=" + length + ";Value=" + (value != null ? value.length + " bytes" : "null") + "]";
    }

}
