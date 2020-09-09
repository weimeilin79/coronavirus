import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

//kamel run -d camel-jackson VirusDispatcher.java --dev

public class VirusDispatcher extends RouteBuilder {

  
  @Override
  public void configure() throws Exception {

    JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
    jacksonDataFormat.setUnmarshalType(SingalInput.class);
    

    from("knative:endpoint/VirusDispatcher")
    .convertBodyTo(String.class) 
    .unmarshal(jacksonDataFormat)
        .choice()
          .when().simple("${body.genuses} == 'Alphacoronavirus'")
            .marshal(jacksonDataFormat)
            .log("Alpha ${body}")
            .to("knative:channel/alpha-handler")
          .when().simple("${body.genuses} == 'MERSvirus'")
             .marshal(jacksonDataFormat)
             .log("MERS - ${body}")
             .to("knative:channel/mers-handler")
          .when().simple("${body.genuses} == 'Novalvirus'")
             .marshal(jacksonDataFormat)
             .log("NEW - ${body}")
             .to("knative:channel/noval-handler")
          .otherwise()
             .setBody().constant("{\"type\":\"Virus\", \"genuses\":\"Unknown\"}")
             .to("knative:channel/unknown-handler")
        
    .end();
  }

  public static class SingalInput {

    String type;
    String genuses;

    public String getType(){
      return type;
    }
    public String getGenuses(){
      return genuses;
    }

    public void setType(String type){
      this.type = type;
    }

    public void setGenuses(String genuses){
      this.genuses = genuses;
    }

    @Override
    public String toString(){
        return "Type:["+type+"] Genuses:["+genuses+"]";
    }
  }

  
}
