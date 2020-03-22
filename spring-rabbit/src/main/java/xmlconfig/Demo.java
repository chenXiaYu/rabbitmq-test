package xmlconfig;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        AbstractApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:/spring.xml");
        AmqpTemplate template = context.getBean(AmqpTemplate.class);
        for(int i= 0;i<10;i++){
            template.convertAndSend("helloworld");
        }
        Thread.sleep(1000);
        context.close();
    }
}
