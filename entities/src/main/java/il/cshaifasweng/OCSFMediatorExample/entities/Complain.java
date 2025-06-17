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

    @Column(name = "clientId")
    private int clientId;




    public Complain() {} // ctor

    public Complain(String complaint, int clientId) {
        super();
        this.complaint = complaint;
        this.timestamp = LocalDateTime.now();
        this.clientId = clientId;


    }

    public void setId(int id) { this.id = id; }
    public String getComplaint() {
        return complaint;
    }
    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
    public LocalDateTime getTimestamp() {return timestamp;}

    public int getClientId() {
        return clientId;
    }
    public void setClientId(int clientID) { this.clientId = clientID; }
}
