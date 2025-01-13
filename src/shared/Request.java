package shared;

import java.io.Serializable;

public class Request implements Serializable {
    private String action; // The action to perform, e.g., "AUTHENTICATE_USER"
    private Object data;   // Data related to the action

    public Request(String action, Object data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Request{" +
                "action='" + action + '\'' +
                ", data=" + data +
                '}';
    }
}

