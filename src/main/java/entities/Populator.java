package entities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.CarJPA;
import persistence.model.Role;
import persistence.model.SellerJPA;

import java.time.LocalDate;

public class Populator {
    private static EntityManagerFactory emf;

    public Populator(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void populate() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            for (int i = 1; i <= 5; i++) {
                SellerJPA seller = new SellerJPA("FirstName " + i, "LastName " + i, "seller" + i + "@carshop.com", i, "City " + i);
                //seller.addRole(em.getReference(Role.class, "verified"));
                em.persist(seller);
                CarJPA car = new CarJPA("Model " + i, "Make " + i, "Brand " + i, LocalDate.of(2022 + i * 2, i, i), 500 + i);
                car.setSellerid(seller);
                em.persist(car);
            }
            em.getTransaction().commit();
        }
    }
}
