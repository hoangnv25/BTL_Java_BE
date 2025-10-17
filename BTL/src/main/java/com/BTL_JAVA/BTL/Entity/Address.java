package com.BTL_JAVA.BTL.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @Column
    boolean isDefault;

    @Column
    String street;

    @Column
    String ward;

    @Column
    String city;

}
