import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.twitter.search.TwitterSearchComponent;
import twitter4j.Status;



public class TwitterRoute extends RouteBuilder {


    private Processor routeProcessor = exchange -> {
        final Status status = (Status) exchange.getIn().getBody();
        exchange.getIn().setBody(status.getText());
    };


    @Override
    public void configure() throws Exception {

        String CONSUMER_KEY = getContext().getPropertiesComponent().resolveProperty("CONSUMER_KEY").get();
        String CONSUMER_SECRET = getContext().getPropertiesComponent().resolveProperty("CONSUMER_SECRET").get();
        String ACCESS_TOKEN = getContext().getPropertiesComponent().resolveProperty("ACCESS_TOKEN").get();
        String ACCESS_TOKEN_SECRET = getContext().getPropertiesComponent().resolveProperty("ACCESS_TOKEN_SECRET").get();

        // setup Twitter component
        TwitterSearchComponent tc = getContext().getComponent("twitter-search", TwitterSearchComponent.class);
        tc.setAccessToken(ACCESS_TOKEN);
        tc.setAccessTokenSecret(ACCESS_TOKEN_SECRET);
        tc.setConsumerKey(CONSUMER_KEY);
        tc.setConsumerSecret(CONSUMER_SECRET);


        // poll twitter search for new tweets
        fromF("twitter-search://%s?delay=%s", "skupper", 5000)
                .log("${body}")
                .to("log:tweet")
                .process(routeProcessor)
                .to("kamelet:postgresql-sink");

    }

}