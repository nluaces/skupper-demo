import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;

public class TelegramRoute extends RouteBuilder {

    JacksonDataFormat jsonDataFormat = new JacksonDataFormat(Tweet.class);

    @Override
    public void configure() throws Exception {
        final String AUTHORIZATION_TOKEN =  getContext().getPropertiesComponent().resolveProperty("AUTHORIZATION_TOKEN").get();
        final String CHAT_ID = getContext().getPropertiesComponent().resolveProperty("CHAT_ID").get();

        from("timer://foo?period=3000")
                .setBody(constant("SELECT sigthning FROM tw_feedback WHERE  created >= NOW() - INTERVAL '3 seconds'"))
                .to("jdbc:camel")
                .to("log:info")
                .split(body())
                .choice().
                when(body().isNotNull())
                .marshal().json(JsonLibrary.Jackson)
                .unmarshal(jsonDataFormat)
                .setBody(simple("New feedback about Skupper \uD83D\uDCE2: \n ${body}"))
                .toF("telegram:bots/?authorizationToken=%s&chatId=%s", AUTHORIZATION_TOKEN, CHAT_ID )
                .otherwise().log("No results").
                endChoice();
    }
}


class Tweet {
    public String getSigthning() {
        return sigthning;
    }

    public void setSigthning(String sigthning) {
        this.sigthning = sigthning;
    }

    public String sigthning;

    @Override
    public String toString() {
        return sigthning;
    }
}

