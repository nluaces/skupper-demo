package dev.nluaces.twitter

import org.apache.camel.builder.RouteBuilder

class MyRouteBuilder : RouteBuilder() {

    override fun configure() {
        from("jetty:http://0.0.0.0:8080")
                .transform(constant("Hello from Kotlin"))
    }

}