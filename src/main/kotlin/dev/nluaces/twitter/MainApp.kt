package dev.nluaces.twitter

import org.apache.camel.main.Main


fun main(args: Array<String>) {
    println("""
               ===============================================
               Open your web browser on http://localhost:8080
               Or open your web browser on http://localhost:9090/index.html
               Press ctrl+c to stop this example
               ===============================================
            """)


    val route = TwitterWebSocketRoute()

    val main = Main()
    main.configure().addRoutesBuilder(MyRouteBuilder())
    main.configure().addRoutesBuilder(route)
    main.run(args)
}
