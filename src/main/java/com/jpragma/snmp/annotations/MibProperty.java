package com.jpragma.snmp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MibProperty {

    public abstract String oid();

    public abstract boolean writable() default false;

    public abstract Class requiredType();
}
