package reactive;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DummyHttpServer {

    static HttpHandler httpHandler = exchange -> {
        String body = "hello devoxx";
        exchange.sendResponseHeaders(200, body.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());
        }
    };

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/hello", httpHandler);
        server.start();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8000/hello")).GET().build();

        CompletableFuture<HttpResponse<String>> response =
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().statusCode());
        System.out.println(response.get().body());
    }
}
