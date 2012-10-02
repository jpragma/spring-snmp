package com.jpragma.snmp.annotations;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;

import com.jpragma.snmp.SnmpAgent;
import com.jpragma.snmp.agent.MibEntry;

public class MibAnnotationsProcessor implements BeanFactoryAware, BeanNameAware, InitializingBean {

    private Log log = LogFactory.getLog(this.getClass());

    private String beanName;
    private ListableBeanFactory beanFactory;
    private SnmpAgent snmpAgent;

    public MibAnnotationsProcessor(SnmpAgent agent) {
        beanFactory = null;
        snmpAgent = agent;
    }

    public void setBeanName(String name) {
        beanName = name;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof ListableBeanFactory)) {
            throw new BeanCreationException(beanName, "beanFactory must be of type " + ListableBeanFactory.class.getName());
        } else {
            this.beanFactory = (ListableBeanFactory) beanFactory;
        }
    }

    public void afterPropertiesSet() throws Exception {
        Set<MibEntry> mibEntries = new HashSet<MibEntry>();
        Map beans = beanFactory.getBeansOfType(null);
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            String beanName = (String) entry.getKey();
            Object beanObject = entry.getValue();
            if (beanObject.getClass().isAnnotationPresent(MibBean.class)) {
                log.debug("Creating MibEntries for bean " + beanName);

                Method[] methods = beanObject.getClass().getMethods();

                for (Method beanMethod : methods) {
                    if (beanMethod.isAnnotationPresent(MibProperty.class)) {
                        if (!beanMethod.getName().startsWith("get") && !beanMethod.getName().startsWith("set"))
                            throw new AnnotationFormatError("Only getters and setters can be annotated with @MibProperty");

                        String propertyName = beanMethod.getName().substring(3, 4).toLowerCase() + beanMethod.getName().substring(4);
                        MibProperty mibProperty = (MibProperty) beanMethod.getAnnotation(MibProperty.class);
                        String oid = mibProperty.oid();
                        boolean writable = mibProperty.writable();
                        Class requiredType = mibProperty.requiredType();
                        log.debug("Creating MibEntry for property " + propertyName);
                        MibEntry mibEntry = new MibEntry(oid, beanObject, propertyName, writable, requiredType);
                        mibEntries.add(mibEntry);
                    }
                }
            }
        }
        if (mibEntries.size() > 0)
            snmpAgent.setMibEntries(mibEntries);
    }
}
