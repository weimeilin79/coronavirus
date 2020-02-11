import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import java.util.Random;

//kamel run -d camel-jackson -d camel-bean SimulateSender.java --dev

public class SimulateSender extends RouteBuilder {

  
  @Override
  public void configure() throws Exception {

    JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
    jacksonDataFormat.setUnmarshalType(SingalInput.class);
    

    from("timer:tick?fixedRate=true&period=2000")
      .setBody(method(this, "genRandoSingalInput()"))
      .marshal(jacksonDataFormat)
      .setHeader("CE-Type", constant("dev.knative.humancontact"))
      .log("${body}")
      .to("knative:channel/humancontact");
  }

  public static SingalInput genRandoSingalInput(){

      SingalInput input = new SingalInput();
      Random generator = new Random();
      String[] genuses = {"Alphacoronavirus","Betacoronavirus","MERSvirus","Novalvirus"};
      //
      int randomIndex = generator.nextInt(genuses.length);

      input.setType("Virus");
      input.setGenuses(genuses[randomIndex]);
       
      return input;
  }

  public static class SingalInput {

    String type;
    String genuses;

    public String getType(){
      return genuses;
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
