
//kamel run -d camel-kafka -d camel-kafka -d camel-mqtt ReceiverMQTT.java --dev

import org.apache.camel.builder.RouteBuilder;

public class Kafkatest extends RouteBuilder {

  private static final String STREAMS_URL = "my-cluster-kafka-bootstrap.streams.svc:9092";
  @Override
  public void configure() throws Exception {
    
    from("timer:tick?fixedRate=true&period=2000")
    .setBody().constant("{\"type\": \"Virus\", \"genuses\": \"Novalvirus\"}")
    .log("to Kafka --- ${body}")
    .to("kafka:my-topic?brokers="+STREAMS_URL);
  }

  
}
