package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "triptypes")
public class Triptype {
    @Id
    @Column(name = "TripTypeId", nullable = false, length = 1)
    private String tripTypeId;

    @Column(name = "TTName", length = 25)
    private String tTName;

    public String getId() {
        return tripTypeId;
    }

    public void setId(String id) {
        this.tripTypeId = id;
    }

    public String getTTName() {
        return tTName;
    }

    public void setTTName(String tTName) {
        this.tTName = tTName;
    }

}