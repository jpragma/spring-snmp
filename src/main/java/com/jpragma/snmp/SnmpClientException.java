// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SnmpClientException.java

package com.jpragma.snmp;


public class SnmpClientException extends RuntimeException
{

    public SnmpClientException()
    {
    }

    public SnmpClientException(String message)
    {
        super(message);
    }

    public SnmpClientException(Throwable cause)
    {
        super(cause);
    }

    public SnmpClientException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
