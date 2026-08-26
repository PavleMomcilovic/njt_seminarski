package rs.ac.bg.fon.fonsledje.connection;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private String message;
    private Map<?, ?> data;

    public Response(String message) {
        this.message = message;
    }

    public Response(String message, Map<?, ?> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Map<?, ?> getData() {
        return data;
    }
}
