spring-snmp
===========

Expose Spring beans using SNMP (Currently the library supports SNMP v1; v2 and v3 are going to be added in future.) 

SNMP for Spring can be used directly, integrating with <a href="http://springframework.org/">Spring</a> using XML configuration files or using JDK 1.5 annotations.

Spring application (using annotations)
--------------------------------------

- Annotate your beans using @MibBean and @MibProperty.
- Create spring XML configuration file (e.g. applicationContext.xml) and define your beans, SNMP agent and MibAnnotationsProcessor, which inspects all spring bean and automatically creates appropriate MibEntries. 
Note that snmpAgent bean should have init-method="start" in order to start the agent upon context creation.

Main app
--------
```java
package com.jpragma.snmp.samples;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jpragma.snmp.SnmpAgent;

public class JvmStatSnmpAgentSpringAnnotated {
  public static void main(String[] args) {
    ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContextAnnotations.xml");
    System.out.println("Hit any key to interrupt");
    try {
      System.in.read();
    } catch (IOException e) {
    }
  }
}
```

applicationContext.xml
----------------------
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:p="http://www.springframework.org/schema/p"
   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">

  <bean id="jvmStat" class="com.jpragma.snmp.samples.JvmStatAnnotated"
    p:app-name="JVM Statistics with Spring (Annotated)"
    p:pool-size="20"/>

  <bean id="mib2System" class="com.jpragma.snmp.agent.Mib2System"
    p:sys-object-id="1.3.6.1.4.1.28824.99"
    p:sys-contact="Isaac Levin"
    p:sys-descr="JvmStat service"
    p:sys-location="Main Building, room 2001"
    p:has-physical-service="false"
    p:has-datalink-service="false"
    p:has-internet-service="false"
    p:has-end-to-end-service="true"
    p:has-applications-service="true"/>

  <bean id="snmpProxyClient" class="com.jpragma.snmp.SnmpClient"
    p:agent-address="localhost"
    p:agent-port="1161"
    p:timeout="5000"
    p:read-only-community="public"
    p:read-write-community="public"/>

  <bean id="snmpAgent" class="com.jpragma.snmp.SnmpAgent"
    p:read-only-comunity="public"
    p:read-write-comunity="private"
    p:listening-port="161"
    p:mib2-system-ref="mib2System"
    p:proxy-snmp-client-ref="snmpProxyClient"
    init-method="start"
    destroy-method="stop">
    <property name="handledOidPrefixes">
      <list>
        <value>1.3.6.1.2.1</value>
        <value>1.3.6.1.4.1.28824</value>
      </list>
    </property>
  </bean>

  <bean id="mibAnnotationsProcessor" class="com.jpragma.snmp.annotations.MibAnnotationsProcessor">
    <constructor-arg index="0" ref="snmpAgent"/>
  </bean>
    
</beans>
```

Annotated bean
--------------
```java
package com.jpragma.snmp.samples;

import com.jpragma.snmp.annotations.MibBean;
import com.jpragma.snmp.annotations.MibProperty;
import com.jpragma.snmp.asn.AsnInteger;
import com.jpragma.snmp.asn.AsnOctetString;
import com.jpragma.snmp.asn.SmiGauge32;

@MibBean
public class JvmStatAnnotated {
  private String appName = "JVM Statistics";
  private int poolSize = 10;
  
  @MibProperty(oid = "1.3.6.1.4.1.28824.99.1.0", writable = true, requiredType = AsnOctetString.class)
  public String getAppName() {
    return appName;
  }
  public void setAppName(String appName) {
    this.appName = appName;
  }
  
  @MibProperty(oid = "1.3.6.1.4.1.28824.99.2.0", writable = true, requiredType = AsnInteger.class)
  public int getPoolSize() {
    return poolSize;
  }
  public void setPoolSize(int poolSize) {
    this.poolSize = poolSize;
  }

  @MibProperty(oid = "1.3.6.1.4.1.28824.99.3.0", requiredType = AsnInteger.class)
  public int getAvailableProcessors() {
    return Runtime.getRuntime().availableProcessors();
  }

  @MibProperty(oid = "1.3.6.1.4.1.28824.99.4.0", requiredType = SmiGauge32.class)
  public long getFreeMemory() {
    return Runtime.getRuntime().freeMemory();
  }

  @MibProperty(oid = "1.3.6.1.4.1.28824.99.5.0", requiredType = SmiGauge32.class)
  public long getMaxMemory() {
    return Runtime.getRuntime().maxMemory();
  }

  @MibProperty(oid = "1.3.6.1.4.1.28824.99.6.0", requiredType = SmiGauge32.class)
  public long getTotalMemory() {
    return Runtime.getRuntime().totalMemory();
  }

  @MibProperty(oid = "1.3.6.1.4.1.28824.99.7.0", requiredType = AsnInteger.class)
  public int getNumberOfThreads() {
    return Thread.activeCount();
  }
}
```