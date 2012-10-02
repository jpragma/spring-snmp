package com.jpragma.snmp.asn;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.jpragma.snmp.asn.ber.BerTlv;
import com.jpragma.snmp.asn.ber.BerTlvIdentifier;
import com.jpragma.snmp.util.BitManipulationHelper;

public class AsnSequence extends AsnObject {

    public static final byte TAG_NUMBER = 48;
    protected List value;

    public AsnSequence() {
        value = new ArrayList();
    }

    public AsnSequence(List value) throws AsnObjectValueException {
        validateList(value);
        this.value = value;
    }

    public void add(AsnObject asn) {
        value.add(asn);
    }

    public void add(int index, AsnObject asn) {
        value.add(index, asn);
    }

    public AsnObject get(int index) {
        return (AsnObject) value.get(index);
    }

    public AsnSequence(BerTlv tlv) {
        this();
        setValue(tlv);
    }

    public Object getValue() {
        return Collections.unmodifiableList(value);
    }

    public void setValue(Object value) throws AsnObjectValueException {
        if (!(value instanceof List)) {
            throw new AsnObjectValueException("Bad object type. Must be an instance of java.util.List");
        } else {
            validateList((List) value);
            this.value = (List) value;
            return;
        }
    }

    public void setValue(BerTlv tlv) {
        byte buf[] = tlv.getValue();
        AsnObject asn;
        for (ByteArrayInputStream stream = new ByteArrayInputStream(buf); stream.available() > 0; value.add(asn)) {
            BerTlv childTlv = new BerTlv();
            childTlv.decode(stream);
            asn = AsnObject.getInstance(childTlv);
        }

    }

    public BerTlv toBerTlv() {
        return toBerTlv(48);
    }

    public BerTlv toBerTlv(int tagNumber) {
        BerTlvIdentifier tag = new BerTlvIdentifier();
        tag.setTagValue(tagNumber);
        BerTlv tlv = new BerTlv();
        tlv.setTag(tag);
        byte tlvValueBuf[] = new byte[0];
        for (Iterator iter = value.iterator(); iter.hasNext();) {
            AsnObject childAsn = (AsnObject) iter.next();
            byte childTlvValueBuf[] = childAsn.toBerTlv().getBytes();
            tlvValueBuf = BitManipulationHelper.mergeArrays(tlvValueBuf, childTlvValueBuf);
        }

        tlv.setLength(tlvValueBuf.length);
        tlv.setValue(tlvValueBuf);
        return tlv;
    }

    private void validateList(List list) throws AsnObjectValueException {
        for (Iterator iter = list.iterator(); iter.hasNext();)
            if (!(iter.next() instanceof AsnObject))
                throw new AsnObjectValueException("Bad object type. Must be an instance of AsnObject");

    }

    public String toString() {
        StringBuffer seqString = new StringBuffer("Sequence: [");
        for (Iterator iter = value.iterator(); iter.hasNext(); seqString.append(iter.next().toString()).append(";"));
        seqString.replace(seqString.length() - 1, seqString.length(), "]");
        return seqString.toString();
    }

}
