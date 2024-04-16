package persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "cars")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String model;
    private String make;
    private String brand;
    @Column(name = "registration_year")
    private LocalDate firstRegistrationYear;
    @Transient
    private int year;
    private int price;

    public CarJPA(String model, String make, String brand, LocalDate firstRegistrationYear, int price) {
        this.model = model;
        this.make = make;
        this.brand = brand;
        this.year = firstRegistrationYear.getYear();
        this.firstRegistrationYear = firstRegistrationYear;
        this.price = price;
    }

    @JsonIgnore
    @ManyToOne
    private SellerJPA sellerid;
}
