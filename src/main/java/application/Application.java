package application;

import entities.Populator;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import persistence.config.HibernateConfig;
import rest.routes.CarRoutes;

public class Application {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig(false);
        CarRoutes carRoutes = new CarRoutes(emf);
        Populator populator = new Populator(emf);
        //populator.populate();
        Javalin app = Javalin.create();
        app.cfg.routing.contextPath = "/api";
        app.routes(carRoutes.getRoutes());
        app.start(7007);
    }
}