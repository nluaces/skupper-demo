package dev.nluaces.twitter

import org.apache.camel.main.Main

private const val CONSUMER_KEY = ""
private const val CONSUMER_SECRET = ""
private const val ACCESS_TOKEN = ""
private const val ACCESS_TOKEN_SECRET = ""


fun main(args: Array<String>) {
    println("""
               ===============================================
               Open your web browser on http://localhost:8080
               Or open your web browser on http://localhost:9090/index.html
               Press ctrl+c to stop this example
               ===============================================
            """)

    val searchTerm = "gaga"
    val delay = 6000
    val port = 9090

    val route = TwitterWebSocketRoute(port, searchTerm, delay, CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET)

    val main = Main()
    main.configure().addRoutesBuilder(MyRouteBuilder())
    main.configure().addRoutesBuilder(route)
    main.run(args)
}
