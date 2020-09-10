/** 
kamel run -d camel-undertow NovelHandler.java --dev
*/

import org.apache.camel.builder.RouteBuilder;


public class NovalHandler extends RouteBuilder {

    private final static String KAFKA_ADDRESS = "my-cluster-kafka-bootstrap.streams.svc:9092";

    @Override
    public void configure() throws Exception {
        
        
        from("knative:channel/noval-handler")
        .log("${body}")
        .to("kafka:my-topic?brokers="+KAFKA_ADDRESS);
        

        
    }
    
}
