package dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class CarDTO {
    private int id;
    private String brand;
    private String model;
    private String make;
    private int year;
    private LocalDate firstRegistrationYear;
    private double price;
    private String sellerEmail;

    public CarDTO(int id, String brand, String model, String make, int year, LocalDate firstRegistrationYear, double price, String sellerEmail) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.make = make;
        this.year = year;
        this.firstRegistrationYear = firstRegistrationYear;
        this.price = price;
        this.sellerEmail = sellerEmail;
    }
}
