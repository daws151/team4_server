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
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//TODO [JPA Buddy] generate columns from DB
}