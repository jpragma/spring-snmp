package com.jpragma.snmp.asn;

import java.math.BigInteger;

import com.jpragma.snmp.asn.ber.BerTlv;
import com.jpragma.snmp.types.GetNextRequestPDU;
import com.jpragma.snmp.types.GetRequestPDU;
import com.jpragma.snmp.types.GetResponsePDU;
import com.jpragma.snmp.types.SetRequestPDU;
import com.jpragma.snmp.types.TrapPDU;

public abstract class AsnObject {

    public abstract Object getValue();

    public abstract void setValue(Object obj) throws AsnObjectValueException;

    public abstract void setValue(BerTlv bertlv);

    public abstract BerTlv toBerTlv();

    public static AsnObject getInstance(BerTlv tlv) {
        byte tagBytes[] = tlv.getTag().getBytes();
        if (tagBytes.length > 1)
            tagBytes[0] = 0;
        int tagNumber = (new BigInteger(tagBytes)).intValue();
        switch (tagNumber) {
        case 5: // '\005'
            return new AsnNull(tlv);

        case 3: // '\003'
            return new AsnBitString(tlv);

        case 1: // '\001'
            return new AsnBoolean(tlv);

        case 2: // '\002'
            return new AsnInteger(tlv);

        case 4: // '\004'
            return new AsnOctetString(tlv);

        case 48: // '0'
            return new AsnSequence(tlv);

        case 6: // '\006'
            return new AsnOID(tlv);

        case 65: // 'A'
            return new SmiCounter32(tlv);

        case 66: // 'B'
            return new SmiGauge32(tlv);

        case 64: // '@'
            return new SmiIPAddress(tlv);

        case 67: // 'C'
            return new SmiTimeTicks(tlv);

        case 71: // 'G'
            return new SmiUInteger32(tlv);

        case -96:
            return new GetRequestPDU(tlv);

        case -95:
            return new GetNextRequestPDU(tlv);

        case -93:
            return new SetRequestPDU(tlv);

        case -94:
            return new GetResponsePDU(tlv);

        case -92:
            return new TrapPDU(tlv);
        }
        throw new AsnParsingException("Unknown ASN.1 type - 0x" + Integer.toHexString(tagNumber));
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AsnObject))
            return false;
        else
            return ((AsnObject) obj).getValue().equals(getValue());
    }
}
