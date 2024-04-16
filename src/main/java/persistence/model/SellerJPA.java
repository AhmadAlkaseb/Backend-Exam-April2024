package persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "sellers")
public class SellerJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private int phone;
    private String city;

    public SellerJPA(String firstName, String lastName, String email, int phone, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.city = city;
    }

    @OneToMany(mappedBy = "sellerid", cascade = CascadeType.ALL)
    private Set<CarJPA> collection = new HashSet<CarJPA>();

    public void addCar(CarJPA entity) {
        if (entity != null) {
            collection.add(entity);
            entity.setSellerid(this);
        }
    }
/*
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "email", referencedColumnName = "email")},
            inverseJoinColumns = {
                    @JoinColumn(name = "role_name", referencedColumnName = "name")})
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        if (role != null) {
            roles.add(role);
            role.getUsers().add(this);
        }
    }*/
}
