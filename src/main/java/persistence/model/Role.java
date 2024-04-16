package persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "name", nullable = false)
    private String name;

   /* @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    Set<SellerJPA> users = new HashSet<>();*/

}
