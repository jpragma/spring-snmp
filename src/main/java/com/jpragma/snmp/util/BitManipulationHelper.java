package com.jpragma.snmp.util;

import com.jpragma.snmp.asn.ber.BerParsingException;

public final class BitManipulationHelper {

    private BitManipulationHelper() {
    }

    public static boolean getBitValue(int value, int bitPosition) {
        if (bitPosition > 32) {
            throw new BerParsingException("Can't retrieve bit value at position " + bitPosition + ". Integer has only 32 bits.");
        } else {
            bitPosition--;
            int mask = 1 << bitPosition;
            return (value & mask) != 0;
        }
    }

    public static int setBitValue(int value, int bitPosition, boolean bitValue) {
        if (bitPosition > 32)
            throw new BerParsingException("Can't set bit value at position " + bitPosition + ". Integer has only 32 bits.");
        bitPosition--;
        int mask = 1 << bitPosition;
        if (bitValue)
            return value | mask;
        else
            return value & ~mask;
    }

    public static byte[] intToByteArray(int number) {
        byte byteArray[] = new byte[4];
        byteArray[0] = (byte) (number >> 24 & 0xff);
        byteArray[1] = (byte) (number >> 16 & 0xff);
        byteArray[2] = (byte) (number >> 8 & 0xff);
        byteArray[3] = (byte) (number & 0xff);
        return byteArray;
    }

    public static byte[] removeLeadingZeroBytes(byte buf[]) {
        int numOfUsedBytes = buf.length;
        for (int i = 0; i < buf.length && buf[i] == 0; i++)
            numOfUsedBytes--;

        if (numOfUsedBytes == 0)
            numOfUsedBytes = 1;
        byte resBuf[] = new byte[numOfUsedBytes];
        System.arraycopy(buf, buf.length - numOfUsedBytes, resBuf, 0, resBuf.length);
        return resBuf;
    }

    public static byte[] mergeArrays(byte buf1[], byte buf2[]) {
        byte resBuf[] = new byte[buf1.length + buf2.length];
        System.arraycopy(buf1, 0, resBuf, 0, buf1.length);
        System.arraycopy(buf2, 0, resBuf, buf1.length, buf2.length);
        return resBuf;
    }
}
