package com.jpragma.snmp.samples;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JvmStatSnmpAgentSpringAnnotated {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("Hit any key to interrupt");
        try {
            System.in.read();
        } catch (IOException e) {
        }
    }
}