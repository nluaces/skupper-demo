package dev.nluaces.twitter

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.twitter.search.TwitterSearchComponent
import org.apache.camel.component.websocket.WebsocketComponent


/**
 * A Camel route that updates from twitter all tweets using having the search term.
 * And post the changes to web-socket, that can be viewed from a web page
 */
class TwitterWebSocketRoute (
        var twPort: Int = 9090,
        var twSearchTErm: String,
        var twDelay: Int = 5000,
        var twConsumerKey: String,
        var twConsumerSecret: String,
        var twAccessToken: String,
        var twAccessTokenSecret: String) : RouteBuilder() {

    @Throws(Exception::class)
    override fun configure() {
        // setup Camel web-socket component on the port we have defined
        val wc: WebsocketComponent = context.getComponent("websocket", WebsocketComponent::class.java)
        wc.port = twPort
        // we can serve static resources from the classpath: or file: system
        wc.staticResources = "classpath:."

        // setup Twitter component
        val tc: TwitterSearchComponent = context.getComponent("twitter-search", TwitterSearchComponent::class.java)
        tc.apply {
            accessToken = twAccessToken
            accessTokenSecret = twAccessTokenSecret
            consumerKey = twConsumerKey
            consumerSecret = twConsumerSecret
        }


        // poll twitter search for new tweets
        fromF("twitter-search://%s?delay=%s", twSearchTErm, twDelay)
                .to("log:tweet") // and push tweets to all web socket subscribers on camel-tweet
                .to("websocket:camel-tweet?sendToAll=true")
                .to("kamelet:sink")
    }
}