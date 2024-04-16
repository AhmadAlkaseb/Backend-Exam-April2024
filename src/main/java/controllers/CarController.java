package controllers;

import daos.CarDAO;
import daos.iDAO;
import dtos.CarDTO;
import exceptions.APIException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.CarJPA;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CarController implements iController {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());

    // Mock database
    //private iDAO dao = CarDAOMock.getInstance(emf);

    // Real database
    private iDAO dao;
    private static EntityManagerFactory emf;

    public CarController(EntityManagerFactory emf) {
        this.emf = emf;
        dao = CarDAO.getInstance(emf);
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            if (!dao.getAll().isEmpty()) {
                ctx.json(dao.getAll());
            } else {
                throw new APIException(404, "No data found.", "" + timestamp);
            }
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            CarDTO carDto = dao.getById(id);
            if (carDto != null) {
                ctx.json(carDto);
            } else {
                throw new APIException(404, "No data found.", "" + timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            CarJPA created = ctx.bodyAsClass(CarJPA.class);
            if (created != null) {
                ctx.json(dao.create(created));
            } else {
                throw new APIException(500, "No data found.", "" + timestamp);
            }
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            CarJPA product = ctx.bodyAsClass(CarJPA.class);
            product.setId(id);
            CarDTO updated = dao.update(product);
            if (updated != null) {
                ctx.json(updated);
            } else {
                throw new APIException(404, "No data found.", "" + timestamp);
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            CarDTO deleted = dao.delete(id);
            if (deleted != null) {
                ctx.json(deleted);
            } else {
                throw new APIException(404, "No data found.", "" + timestamp);
            }
        };
    }

    @Override
    public Handler filterByCarsByYear() {
        return ctx -> {
            int year = Integer.parseInt(ctx.pathParam("year"));
            Set<CarDTO> filteredCars = dao.getAll()
                    .stream()
                    .filter(carDTO -> carDTO.getYear() == year)
                    .collect(Collectors.toSet());
            if (filteredCars.isEmpty()) {
                throw new APIException(404, "No cars found for the year " + year, "" + timestamp);
            } else {
                ctx.json(filteredCars);
            }
        };
    }

    @Override
    public Handler groupCarsByBrandAndGetTotalPrice() {
        return ctx -> {
            Map<String, Double> map;
            Set<CarDTO> allCars = dao.getAll();
            if (allCars == null || allCars.isEmpty()) {
                throw new APIException(404, "No cars found.", "" + timestamp);
            } else {
                map = allCars.stream()
                        .collect(Collectors.groupingBy(CarDTO::getBrand,
                                Collectors.summingDouble(CarDTO::getPrice)));
                ctx.json(map);
            }
        };
    }
}
