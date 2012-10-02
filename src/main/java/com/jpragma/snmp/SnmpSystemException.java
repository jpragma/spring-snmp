// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SnmpSystemException.java

package com.jpragma.snmp;


public class SnmpSystemException extends RuntimeException
{

    public SnmpSystemException()
    {
    }

    public SnmpSystemException(String message)
    {
        super(message);
    }

    public SnmpSystemException(Throwable cause)
    {
        super(cause);
    }

    public SnmpSystemException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
