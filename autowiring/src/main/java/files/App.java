package files;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        //Context is retrieved from this class as there is @SpringBootApplication annotation
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);

        //Getting and using formatter via service
        FooService fooService = ctx.getBean(FooService.class);
        System.out.println(fooService.doStuff());

        //Getting and using formatter as is
        BarFormatter barFormatter = ctx.getBean(BarFormatter.class);
        System.out.println(barFormatter.format());

        //Making sure getBean returns sungleton
        if (barFormatter == ctx.getBean(BarFormatter.class)) {
            System.out.println("Equal");
        } else {
            System.out.println("Not equal");
        }
    }
}
