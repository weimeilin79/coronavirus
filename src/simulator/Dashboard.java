import org.apache.camel.builder.RouteBuilder;

public class Dashboard extends RouteBuilder {

  private static final String STREAMS_URL = "my-cluster-kafka-bootstrap.streams.svc:9092";
  @Override
  public void configure() throws Exception {

    from("kafka:my-topic?brokers="+STREAMS_URL)
	.to("ahc-ws://myui:8181/echo");
  }


}
