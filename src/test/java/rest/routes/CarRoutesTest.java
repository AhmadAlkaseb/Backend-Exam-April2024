package rest.routes;

import dtos.CarDTO;
import entities.Populator;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.CarJPA;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarRoutesTest {
    private static EntityManagerFactory emf;
    private static Javalin app;

    @BeforeAll
    static void setUpBeforeAll() {
        RestAssured.baseURI = "http://localhost:7007/api/cars";
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        CarRoutes carRoutes = new CarRoutes(emf);
        app = Javalin.create();
        app.cfg.routing.contextPath = "/api";
        app.routes(carRoutes.getRoutes());
        app.start(7007);
    }

    @AfterAll
    static void shutDownAfterAll() {
        emf.close();
        app.close();
    }

    @BeforeEach
    void setUp() {
        Populator populator = new Populator(emf);
        populator.populate();
    }

    @AfterEach
    void shutDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("delete from CarJPA").executeUpdate();
            em.createQuery("delete from SellerJPA").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Creating car")
    void test1() {

        int i = 1;
        CarJPA car = new CarJPA("Model " + i, "Make " + i, "Brand " + i, LocalDate.of(2022 + i * 2, i, i), 500 + i);
        String response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(car)
                .when()
                .post("/")
                .then()
                .statusCode(200)
                .extract().asString();
        //.body("make", equalTo("Make 1"))
        //.body("model", equalTo("Model 1"));

        System.out.println(response);
        String brand = JsonPath.from(response).getString("brand");
        System.out.println("Brand: " + brand);
    }

    @Test
    @DisplayName("Get all cars")
    void test2() {

        RestAssured
                .given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body("size()", equalTo(5));
    }

    @Test
    @DisplayName("Get car by id")
    void test3() {
        CarDTO car = RestAssured
                .given()
                .pathParam("id", 1)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract().as(CarDTO.class);
        assertNotNull(car);
        assertEquals(car.getId(), 1);
    }


    @Test
    @DisplayName("Update car by id")
    void test4() {
        CarDTO updatedCar = CarDTO.builder()
                .brand("TEST")
                .model("TEST")
                .make("TEST")
                .firstRegistrationYear(LocalDate.of(2024, 3, 4))
                .price(5000)
                .build();

        CarDTO car = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(updatedCar)
                .pathParam("id", 2)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .extract().as(CarDTO.class);
        assertNotNull(car);
        assertEquals(car.getId(), 2);
    }

    @Test
    @DisplayName("Delete car by id")
    void test5() {
        CarDTO car = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 3)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(200)
                .extract().as(CarDTO.class);
        assertNotNull(car);
        assertEquals(car.getId(), 3);
    }
}