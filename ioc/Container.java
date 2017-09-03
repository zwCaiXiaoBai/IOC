package log;


import java.util.Set;

public interface Container {
    /**
     * 根据class获取Bean
     * @param <T>
     * @return
     */
    public <T> T getBean(Class<T> clazz);

    /**
     * 根据name获取Bean
     * @param name
     * @param <T>
     * @return
     */
    public <T> T getBeanByName(String name);

    /**
     * 根据Object注册一个Bean
     * @param bean
     */
    public Object registeBean(Object bean);

    /**
     * 注册一个带name的Bean到容器
     * @param name
     * @param bean
     */
    public Object registeBean(String name, Object bean);
    /**
     * 注册一个Class到容器中
     * @param clazz
     */
    public Object registeBean(Class<?> clazz);

    /**
     * 删除一个bean
     * @param clazz
     */
    public void remove(Class<?> clazz);

    /**
     * 根据名称删除一个bean
     * @param name
     */
    public void removeByName(String name);

    /**
     * @return	返回所有bean对象名称
     */
    public Set<String> getBeanNames();

    /**
     * 初始化装配
     */
    public void initWired();
}
