package rs.ac.bg.fon.fonsledje.connection;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class HttpResponse {
    public static Response getResponse(String message) {
        return new Response(message);
    }

    public static Response getResponseWithData(String message, Map<?, ?> data) {
        return new Response(message, data);
    }

    public static Response getResponse(String message, HttpStatus status) {
        return new Response(message);
    }

    public static Response getResponseWithData(String message, Map<?, ?> data, HttpStatus status) {
        return new Response(message, data);
    }
}
