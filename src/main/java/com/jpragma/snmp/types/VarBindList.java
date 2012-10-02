package com.jpragma.snmp.types;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;

import com.jpragma.snmp.asn.AsnParsingException;
import com.jpragma.snmp.asn.AsnSequence;
import com.jpragma.snmp.asn.ber.BerTlv;

public class VarBindList extends AsnSequence {

    public VarBindList() {
    }

    public VarBindList(List listOfVarBinds) {
        Object nextVarBind;
        for (Iterator iter = listOfVarBinds.iterator(); iter.hasNext(); add((VarBind) nextVarBind)) {
            nextVarBind = iter.next();
            if (!(nextVarBind instanceof VarBind))
                throw new AsnParsingException("Bad object type. Must be an instance of VarBind");
        }
    }

    public VarBindList(BerTlv tlv) {
        setValue(tlv);
    }

    public void add(VarBind varBind) {
        super.add(varBind);
    }

    public VarBind getVarBind(int index) {
        return (VarBind) get(index);
    }

    public void setValue(BerTlv tlv) {
        byte buf[] = tlv.getValue();
        VarBind varBind;
        for (ByteArrayInputStream stream = new ByteArrayInputStream(buf); stream.available() > 0; add(varBind)) {
            BerTlv childTlv = new BerTlv();
            childTlv.decode(stream);
            varBind = new VarBind(childTlv);
        }

    }

    public Iterator iterator() {
        return value.iterator();
    }

    public int size() {
        return value.size();
    }

    public String toString() {
        StringBuffer varBindListString = new StringBuffer("VarBindList: [\n");
        for (Iterator iter = value.iterator(); iter.hasNext(); varBindListString.append(iter.next().toString()).append("\n"));
        varBindListString.replace(varBindListString.length() - 1, varBindListString.length(), "]");
        return varBindListString.toString();
    }
}
