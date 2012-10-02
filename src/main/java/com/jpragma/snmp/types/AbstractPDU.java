package com.jpragma.snmp.types;

import java.util.List;

import com.jpragma.snmp.asn.AsnObjectValueException;
import com.jpragma.snmp.asn.AsnSequence;
import com.jpragma.snmp.asn.ber.BerTlv;

public abstract class AbstractPDU extends AsnSequence {

    public AbstractPDU() {
    }

    public AbstractPDU(List value) throws AsnObjectValueException {
        super(value);
    }

    public AbstractPDU(BerTlv tlv) {
        super(tlv);
    }
}
