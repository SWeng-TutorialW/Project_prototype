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

    @ManyToOne
    @JoinColumn(name = "order_id") // This will create a foreign key column in the Complains table
    private Order order;

    private String complaint;
    private LocalDateTime timestamp;
    private String clientName;
    private double refundAmount;



    public Complain() {} // ctor

    public Complain(String complaint){
        super();
        this.complaint = complaint;
        this.timestamp = LocalDateTime.now();
        this.order = null;
        this.refundAmount =0;
    }
    public Complain(String complaint, Order order){
        super();
        this.complaint = complaint;
        this.timestamp = LocalDateTime.now();
        this.order = order;
        this.refundAmount =0;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public double getRefundAmount() {
        return refundAmount;}
    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }
    public String getComplaint() {
        return complaint;
    }
    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
    public LocalDateTime getTimestamp() {return timestamp;}
    public void setTimestamp(LocalDateTime timestamp) {this.timestamp = timestamp;}

    public String getClient() {
        return clientName;
    }
    public void setClient(String name) { clientName = name; }

    public int getId() { return id; }
}
