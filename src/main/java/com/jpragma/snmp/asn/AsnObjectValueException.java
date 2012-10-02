package com.jpragma.snmp.asn;

public class AsnObjectValueException extends Exception {

    public AsnObjectValueException() {
    }

    public AsnObjectValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsnObjectValueException(String message) {
        super(message);
    }

    public AsnObjectValueException(Class asnClass, Class classFrom, Class classTo) {
        super("Error assigning value for " + asnClass.getSimpleName() + ". Can't convert " + classFrom.getSimpleName() + " to "
                + classTo.getSimpleName());
    }

    public AsnObjectValueException(Throwable cause) {
        super(cause);
    }
}
