package ecom.ble.health.passport.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "id", length = 255, nullable = false)
    private String id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "blood_group", length = 10)
    private String bloodGroup;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;
}

