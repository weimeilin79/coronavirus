import org.apache.camel.builder.RouteBuilder;

//kamel run -d camel-jackson AlphaHandler.java --dev

public class AlphaHandler extends RouteBuilder {
  
  @Override
  public void configure() throws Exception {

    from("knative:channel/alpha-handler")
    .log("Alpha Events - ${body}")
    .to("kafka:my-topic?brokers=my-cluster-kafka-bootstrap.streams.svc:9092");
  }

  
}
