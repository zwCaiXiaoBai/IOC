package log;

import org.junit.Before;
import org.junit.Test;

public class IocTest  {

    Container container;

    Parent parent;

    @Before
    public void bf(){
        container = new AbstracterContainer();
    }

    @Test
    public void baseTest(){
        //container.registeBean(Lol.class);
        // class 注入容器
        System.out.println("perform into Test "+this.getClass().getName());
        parent = container.getBean(Parent.class);
        parent.print();
    }

    public void iocClassTest(){
        //container.registeBean(Lol2.class);
        // 初始化注入
        container.initWired();

        Lol lol = container.getBean(Lol.class);
        lol.work();
    }

    public void iocNameTest(){
        container.registeBean("face", new Lol());

        //名字注入
        Lol lol = container.getBean(Lol.class);
        lol.work();
    }

//    public static void main(String[] args) {
//        baseTest();
//        //iocClassTest();
//        //iocNameTest();
//    }

}