package entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.model.SellerJPA;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Car {
    private int id;
    private String brand;
    private String model;
    private String make;
    private int year;
    private LocalDate firstRegistrationYear;
    private double price;
    private SellerJPA sellerEmail;

    public Car(String brand, String model, String make, LocalDate firstRegistrationYear, int price) {
        this.brand = brand;
        this.model = model;
        this.make = make;
        this.year = firstRegistrationYear.getYear();
        this.firstRegistrationYear = firstRegistrationYear;
        this.price = price;
    }

    public SellerJPA setSellerEmail(SellerJPA email) {
        this.sellerEmail = email;
        return sellerEmail;
    }
}
