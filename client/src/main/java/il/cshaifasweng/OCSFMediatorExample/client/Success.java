package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Success implements Serializable {
    private String message;
    private LocalDateTime time;

    public Success(String message) {
        this.message = message;
        this.time = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTime() {
        return time;
    }
} 