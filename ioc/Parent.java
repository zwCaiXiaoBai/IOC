package log;

/**
 * Created by lizw on 2017/8/13.
 */
@MBean
public class Parent {
    @AutoWired
    private Lol lol;


    private String name;

    public Parent() {
        //System.out.println("obtain this obj "+lol.getClass().getName());
    }
    public void print(){
        System.out.println("obtain this obj "+lol.getClass().getName());
    }
}
