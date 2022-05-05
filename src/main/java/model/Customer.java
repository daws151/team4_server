package model;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerId", nullable = false)
    private Integer customerId;

    public Integer getId() {
        return customerId;
    }

    public void setId(Integer id) {
        this.customerId = id;
    }

//TODO [JPA Buddy] generate columns from DB
}