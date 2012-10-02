package com.jpragma.snmp.asn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import com.jpragma.snmp.asn.ber.BerTlv;

public class SmiIPAddress extends AsnOctetString {

    public static final byte TAG_NUMBER = 64;

    public SmiIPAddress() {
        value = new byte[4];
        Arrays.fill(value, (byte) 0);
    }

    public SmiIPAddress(BerTlv tlv) {
        super(tlv);
        if (value.length != 4)
            throw new AsnParsingException("SmiIPAddress must have 4 octets");
    }

    public SmiIPAddress(byte value[]) {
        super(value);
        if (value.length != 4)
            throw new AsnParsingException("SmiIPAddress must have 4 octets");
    }

    public SmiIPAddress(String value) throws AsnObjectValueException {
        setValue(value);
    }

    public SmiIPAddress(InetAddress value) {
        this.value = value.getAddress();
    }

    public Object getValue() {
        return value[0] + "." + value[1] + "." + value[2] + "." + value[3];
    }

    public InetAddress inetAddressValue() {
        try {
            return InetAddress.getByAddress(value);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public void setValue(Object value) throws AsnObjectValueException {
        byte tmpValue[] = null;
        if (value instanceof byte[]) {
            if (((byte[]) (byte[]) value).length == 4)
                tmpValue = (byte[]) (byte[]) value;
        } else if (value instanceof String) {
            String networks[] = ((String) value).split(".");
            if (networks.length != 4)
                throw new AsnObjectValueException(value + " is not valid IP Address");
            tmpValue = new byte[4];
            for (int i = 0; i < networks.length; i++) {
                int net = Integer.parseInt(networks[i].trim());
                if (net >= 0 && net <= 255)
                    tmpValue[i] = (byte) net;
            }

        }
        if (tmpValue == null) {
            throw new AsnObjectValueException(value.getClass() + ":" + value.toString() + "is not valid IP Address");
        } else {
            this.value = tmpValue;
            return;
        }
    }

    public BerTlv toBerTlv() {
        return super.toBerTlv(64);
    }

    public String toString() {
        StringBuffer ipString = new StringBuffer("IpAddress: ");
        ipString.append(getValue().toString());
        return ipString.toString();
    }

}
