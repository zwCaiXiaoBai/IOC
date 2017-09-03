package log;


import sun.reflect.misc.ReflectUtil;

import java.io.File;
import java.io.FileFilter;
import java.lang.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AbstracterContainer implements Container{
    /**
     * 保存所有bean对象，格式为 com.xxx.Person : @52x2xa
     */
    private Map<String, Object> beans;

    /**
     * 存储bean和name的关系. name:com.xxx.person
     */
    private Map<String, String> beankeys;

    private ClassLoade classLoade;
    public AbstracterContainer() {
        this.beans = new ConcurrentHashMap<String, Object>();
        this.beankeys = new ConcurrentHashMap<String, String>();
        this.classLoade = new ClassLoade();
        assembly();
        initWired();
    }
    @Override
    public <T> T getBean(Class<T> clazz) {
        String name = clazz.getName();
        Object object = beans.get(name);
        if(null!=object){
            return (T)object;
        }
        return null;
    }

    @Override
    public <T> T getBeanByName(String name) {
        String className = beankeys.get(name);
        Object obj = beans.get(className);
        if(null != obj){
            return (T) obj;
        }
        return null;
    }

    @Override
    public Object registeBean(Object bean) {
        String name = bean.getClass().getName();
        beankeys.put(name, name);
        beans.put(name, bean);
        return bean;
    }

    @Override
    public Object registeBean(String name, Object bean) {
        String className = bean.getClass().getName();
        beankeys.put(name, className);
        beans.put(className, bean);
        return bean;
    }

    @Override
    public Object registeBean(Class<?> clazz) {
        String name = clazz.getName();
        beankeys.put(name, name);
        Object bean = null;
        try {
            bean = ReflectUtil.newInstance(clazz);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        beans.put(name, bean);
        return bean;
    }

    @Override
    public void remove(Class<?> clazz) {
        String className = clazz.getName();
        if(null != className && !className.equals("")){
            beankeys.remove(className);
            beans.remove(className);
        }
    }

    @Override
    public void removeByName(String name) {
        String className = beankeys.get(name);
        if(null != className && !className.equals("")){
            beankeys.remove(name);
            beans.remove(className);
        }
    }

    @Override
    public Set<String> getBeanNames() {
        return beankeys.keySet();
    }

    @Override
    public void initWired() {
        Iterator<Map.Entry<String, Object>> it = beans.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            Object object = entry.getValue();
            injection(object);
        }
    }
    /**
     * 注入对象
     * @param object
     */
    private void injection(Object object) {
        // 所有字段
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 需要注入的字段
                AutoWired autoWired = field.getAnnotation(AutoWired.class);
                if (null != autoWired) {

                    // 要注入的字段
                    Object autoWiredField = null;

                    String name = autoWired.name();
                    if(!name.equals("")){
                        String className = beankeys.get(name);
                        if(null != className && !className.equals("")){
                            autoWiredField = beans.get(className);
                        }
                        if (null == autoWiredField) {
                            throw new RuntimeException("Unable to load " + name);
                        }
                    } else {
                        if(autoWired.value() == Class.class){
                            autoWiredField = recursiveAssembly(field.getType());
                        } else {
                            // 指定装配的类
                            autoWiredField = this.getBean(autoWired.value());
                            if (null == autoWiredField) {
                                autoWiredField = recursiveAssembly(autoWired.value());
                            }
                        }
                    }

                    if (null == autoWiredField) {
                        throw new RuntimeException("Unable to load " + field.getType().getCanonicalName());
                    }

                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    field.set(object, autoWiredField);
                    field.setAccessible(accessible);
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object recursiveAssembly(Class<?> clazz){
        if(null != clazz){
            return this.registeBean(clazz);
        }
        return null;
    }

    /**
     * 部署注解Bean
     */
    private void assembly(){
        String packageName = this.getClass().getPackage().getName();
        Set<ClassInfo> clsses = classLoade.getClassByAnnotation(packageName,null,MBean.class,true);
        for (ClassInfo clazz:clsses){
            registeBean(clazz.newInstance());
        }
    }

}
