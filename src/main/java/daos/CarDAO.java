package daos;

import dtos.CarDTO;
import entities.Car;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import persistence.model.CarJPA;
import persistence.model.SellerJPA;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CarDAO implements iDAO {
    private static CarDAO instance;
    private static EntityManagerFactory emf;

    private CarDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static CarDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CarDAO(emf);
        }
        return instance;
    }

    @Override
    public CarDTO create(Car car) {
        return null;
    }

    @Override
    public CarDTO create(CarJPA car) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(car);
            em.getTransaction().commit();
            return convertTODTO(car);
        }
    }

    @Override
    public Set<CarDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<CarJPA> query = em.createQuery("SELECT c FROM CarJPA c", CarJPA.class);
            List<CarDTO> listOfCarDTOs = query.getResultList().stream()
                    .map(car -> convertTODTO(car))
                    .collect(Collectors.toList());
            return new HashSet<>(listOfCarDTOs);
        }
    }

    @Override
    public CarDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return convertTODTO(em.find(CarJPA.class, id));
        }
    }

    @Override
    public CarDTO update(Car healthProduct) {
        return null;
    }

    @Override
    public CarDTO update(CarJPA car) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(car);
            em.getTransaction().commit();
            return convertTODTO(car);
        }
    }

    @Override
    public CarDTO delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            CarJPA car = em.find(CarJPA.class, id);
            if (car != null) {
                em.remove(car);
                em.getTransaction().commit();
            }
            return convertTODTO(car);
        }
    }

    @Override
    public void addCarToSeller(int sellerId, int carId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            CarJPA car = em.find(CarJPA.class, carId);
            SellerJPA seller = em.find(SellerJPA.class, sellerId);
            seller.addCar(car);
            em.getTransaction().commit();
        }
    }

    @Override
    public Set<CarDTO> getCarsBySeller(int sellerId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<CarJPA> query = em.createQuery("SELECT c FROM CarJPA c WHERE c.sellerid.id = :sellerid", CarJPA.class);
            query.setParameter("sellerid", sellerId);
            Set<CarDTO> listOfCarDTOs = query.getResultList().stream()
                    .map(car -> convertTODTO(car))
                    .collect(Collectors.toSet());
            return listOfCarDTOs; //new HashSet<>(listOfCarDTOs);
        }
    }

    public CarDTO convertTODTO(CarJPA car) {
        if (car != null) {
            return CarDTO.builder()
                    .id(car.getId())
                    .brand(car.getBrand())
                    .model(car.getModel())
                    .make(car.getMake())
                    .year(car.getFirstRegistrationYear().getYear())
                    .firstRegistrationYear(car.getFirstRegistrationYear())
                    .price(car.getPrice())
                    .build();
        } else {
            return null;
        }
    }
}
