package daos;

import dtos.CarDTO;
import entities.Car;
import persistence.model.CarJPA;

import java.util.Set;

public interface iDAO {
    CarDTO create(Car car);

    CarDTO create(CarJPA car);

    Set<CarDTO> getAll();

    CarDTO getById(int id);

    CarDTO update(Car healthProduct);

    CarDTO update(CarJPA car);

    CarDTO delete(int id);

    void addCarToSeller(int sellerId, int carId);

    Set<CarDTO> getCarsBySeller(int sellerId);
}
