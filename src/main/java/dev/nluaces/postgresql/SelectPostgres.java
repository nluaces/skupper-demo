package dev.nluaces.postgresql;

import org.apache.camel.builder.RouteBuilder;

public class SelectPostgres extends RouteBuilder {
    private final String AUTHORIZATION_TOKEN = "";
    private final String CHAT_ID = "";

    @Override
    public void configure() throws Exception {
        from("timer://foo?period=3000")
                .setBody(constant("SELECT sigthning FROM tw_feedback WHERE  created >= NOW() - INTERVAL '3 seconds'"))
                .to("jdbc:camel")
                .to("log:info")
                .choice().
                when(body().isNotEqualTo("[]"))
                .toF("telegram:bots/?authorizationToken=%s&chatId=%s", AUTHORIZATION_TOKEN, CHAT_ID )
                .otherwise().log("No results").
                endChoice();

    }

}