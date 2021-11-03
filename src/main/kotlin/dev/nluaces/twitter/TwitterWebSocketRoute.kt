package dev.nluaces.twitter

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.twitter.search.TwitterSearchComponent
import org.apache.camel.component.websocket.WebsocketComponent


private const val CONSUMER_KEY = ""
private const val CONSUMER_SECRET = ""
private const val ACCESS_TOKEN = ""
private const val ACCESS_TOKEN_SECRET = ""

/**
 * A Camel route that updates from twitter all tweets using having the search term.
 * And post the changes to web-socket, that can be viewed from a web page
 */
class TwitterWebSocketRoute : RouteBuilder() {

    @Throws(Exception::class)
    override fun configure() {
        // setup Camel web-socket component on the port we have defined
        val wc: WebsocketComponent = context.getComponent("websocket", WebsocketComponent::class.java)
        wc.port = 9090
        // we can serve static resources from the classpath: or file: system
        wc.staticResources = "classpath:."

        // setup Twitter component
        val tc: TwitterSearchComponent = context.getComponent("twitter-search", TwitterSearchComponent::class.java)
        tc.apply {
            accessToken = ACCESS_TOKEN
            accessTokenSecret = ACCESS_TOKEN_SECRET
            consumerKey = CONSUMER_KEY
            consumerSecret = CONSUMER_SECRET
        }

        val searchTerm = "skupper"

        // poll twitter search for new tweets
        fromF("twitter-search://%s?delay=%s", searchTerm, 5000)
                .to("log:tweet") // and push tweets to all web socket subscribers on camel-tweet
                .to("websocket:camel-tweet?sendToAll=true")
                .to("kamelet:sink")
    }
}