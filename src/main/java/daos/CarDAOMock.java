package daos;

import dtos.CarDTO;
import entities.Car;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.CarJPA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CarDAOMock implements iDAO {
    private static List<CarDTO> list = new ArrayList<>();
    private static int counter = 1;
    private static CarDAOMock instance;
    private static EntityManagerFactory emf;

    private CarDAOMock(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static CarDAOMock getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CarDAOMock(emf);
        }
        return instance;
    }

    @Override
    public Set<CarDTO> getAll() {
        return new HashSet<>(list);
    }

    @Override
    public CarDTO getById(int id) {
        for (CarDTO carDto : list) {
            if (carDto.getId() == id) {
                return carDto;
            }
        }
        return null;
    }

    @Override
    public CarDTO create(Car car) {
        CarDTO converted = convertTODTO(car);
        converted.setId(counter);
        counter++;
        list.add(converted);
        return converted;
    }

    @Override
    public CarDTO create(CarJPA car) {
        return null;
    }

    @Override
    public CarDTO update(Car car) {
        for (CarDTO carDto : list) {
            if (carDto.getId() == car.getId()) {
                carDto.setBrand(car.getBrand());
                carDto.setModel(car.getModel());
                carDto.setMake(car.getMake());
                return carDto;
            }
        }
        return null;
    }

    @Override
    public CarDTO update(CarJPA car) {
        return null;
    }

    @Override
    public CarDTO delete(int id) {
        for (CarDTO carDto : list) {
            if (carDto.getId() == id) {
                return carDto;
            }
        }
        return null;
    }

    @Override
    public void addCarToSeller(int sellerId, int carId) {

    }

    @Override
    public Set<CarDTO> getCarsBySeller(int sellerId) {
        return Set.of();
    }

    private CarDTO convertTODTO(Car car) {
        return CarDTO.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .make(car.getMake())
                .year(car.getFirstRegistrationYear().getYear())
                .firstRegistrationYear(car.getFirstRegistrationYear())
                .price(car.getPrice())
                //.sellerEmail(car.getSellerEmail())
                .build();
    }
}
