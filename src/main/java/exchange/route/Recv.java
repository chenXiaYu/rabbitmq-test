package exchange.route;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {
    private final static String QUEUE_NAME = "ts-q-2-";
    private final  static  String EX_NAME="exchange-route";

    public static void recvQueue(String tag,int count) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory;

        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/chen");
        connectionFactory.setPort(5672);

        //创建队列以免找不到时候报错
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //每次只接收一条信息
        channel.basicQos(count);
        //绑定交换机
        channel.queueBind(QUEUE_NAME,EX_NAME,"update");
        channel.queueBind(QUEUE_NAME,EX_NAME,"delete");
//        Consumer consumer = new DefaultConsumer(channel){
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                super.handleDelivery(consumerTag, envelope, properties, body);
//                System.out.println("recv msg" +new String(body));
//            }
//        };
//
//        channel.basicConsume(QUEUE_NAME,consumer);


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(tag + ":'" + message + "'");

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });
//        只要不关闭channel这回阻塞监听
//        channel.close();
//        connection.close();

    }

    public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {
        recvQueue("B",1);

    }
}
