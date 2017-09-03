package log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoWired {
    /**
     * @return	要注入的类类型
     */
    Class<?> value() default Class.class;

    /**
     * @return	bean的名称
     */
    String name() default "";
}
