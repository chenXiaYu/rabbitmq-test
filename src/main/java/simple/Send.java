package simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    private final  static  String QUEUE_NAME="ts-q";
    private final  static ConnectionFactory connectionFactory;
    static {
         connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/chen");
        connectionFactory.setPort(5672);
    }

    public static void main(String[] args) {
        try {
            sendQueue("hello ....");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    public static  void sendQueue( String msg) throws IOException, TimeoutException {

        Connection connection =  connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        int a  =1;
        while (a<=50){
            channel.basicPublish("",QUEUE_NAME,null,String.valueOf(a).getBytes());
            System.out.println(a +" is sent");
            a++;
        }
        //如果不关闭则进入阻塞状态
    }
}
