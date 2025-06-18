package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Complains")
public class Complain implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String complaint;
    private LocalDateTime timestamp;
    private String clientName;


    public Complain() {} // ctor

    public Complain(String complaint){
        super();
        this.complaint = complaint;
        this.timestamp = LocalDateTime.now();



    }

    public String getComplaint() {
        return complaint;
    }
    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
    public LocalDateTime getTimestamp() {return timestamp;}

    public String getClient() {
        return clientName;
    }
    public void setClient(String name) { clientName = name; }

}
