import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.twitter.search.TwitterSearchComponent;
import org.apache.camel.spi.PropertiesComponent;
import twitter4j.Status;

public class TwitterRoute extends RouteBuilder {

    private final Processor routeProcessor = exchange -> {
        final Status status = (Status) exchange.getIn().getBody();
        exchange.getIn().setBody(status.getText());
    };


    @Override
    public void configure() throws Exception {

        PropertiesComponent pc = getContext().getPropertiesComponent();
        getContext().getPropertiesComponent().setLocation("config.properties");

        String CONSUMER_KEY = pc.resolveProperty("twitter.consumer-key").get();
        String CONSUMER_SECRET = pc.resolveProperty("twitter.consumer-secret").get();
        String ACCESS_TOKEN = pc.resolveProperty("twitter.access-token").get();
        String ACCESS_TOKEN_SECRET = pc.resolveProperty("twitter.access-token-secret").get();

        // setup Twitter component
        TwitterSearchComponent tc = getContext().getComponent("twitter-search", TwitterSearchComponent.class);
        tc.setAccessToken(ACCESS_TOKEN);
        tc.setAccessTokenSecret(ACCESS_TOKEN_SECRET);
        tc.setConsumerKey(CONSUMER_KEY);
        tc.setConsumerSecret(CONSUMER_SECRET);


        // poll twitter search for new tweets
        fromF("twitter-search://%s?delay=%s", "skupper", 5000)
                .log("${body}")
                .process(routeProcessor)
                .to("kamelet:twitter-postgresql-sink");

    }

}