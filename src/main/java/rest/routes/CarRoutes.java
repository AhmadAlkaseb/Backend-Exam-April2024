package rest.routes;

import controllers.CarController;
//import controllers.SecurityController;
import controllers.iController;
import exceptions.logger.CustomLogger;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CarRoutes {
    private static CustomLogger customLogger = new CustomLogger();
    private static EntityManagerFactory emf;
    private static iController controller;
    //private static SecurityController securityController;

    public CarRoutes(EntityManagerFactory emf) {
        this.emf = emf;
        controller = new CarController(emf);
        //securityController = new SecurityController(emf);
    }

    public EndpointGroup getRoutes() {
        return () -> path("/cars", () -> {
            get("/", customLogger.handleExceptions(ctx -> controller.getAll().handle(ctx)));

            get("/{id}", customLogger.handleExceptions(ctx -> controller.getById().handle(ctx)));

            get("/grouping/{year}", customLogger.handleExceptions(ctx -> controller.filterByCarsByYear().handle(ctx)));

            get("/grouping", customLogger.handleExceptions(ctx -> controller.groupCarsByBrandAndGetTotalPrice().handle(ctx)));

            post("/", customLogger.handleExceptions(ctx -> controller.create().handle(ctx)));

            put("/{id}", customLogger.handleExceptions(ctx -> controller.update().handle(ctx)));

            delete("/{id}", customLogger.handleExceptions(ctx -> controller.delete().handle(ctx)));

           // post("/login", customLogger.handleExceptions(ctx -> securityController.login()));
        });
    }
}
