package reflection.web;

import com.sun.net.httpserver.HttpServer;

public class WebServer {

    public void startServer() {
        try {

            HttpServer server = HttpServer.create(ServerConfiguration.getInstance().getServerAddress(), 0);

            server.createContext("/greeting").setHandler(exchange -> {
                String response = ServerConfiguration.getInstance().getGreetingMessage();
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
            });

            System.out.println("Server started at " + ServerConfiguration.getInstance().getServerAddress());
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
