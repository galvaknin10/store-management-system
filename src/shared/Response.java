package shared;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean success; // Indicates if the request was successful
    private Object data;     // The data returned, e.g., employee details or an error message
    private String message;  // A message describing the result

    public Response(boolean success, Object data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccessful() {
        return success;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
