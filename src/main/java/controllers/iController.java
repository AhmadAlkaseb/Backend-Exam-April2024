package controllers;

import io.javalin.http.Handler;

public interface iController {
    Handler create();

    Handler getAll();

    Handler getById();

    Handler update();

    Handler delete();

    Handler filterByCarsByYear();

    Handler groupCarsByBrandAndGetTotalPrice();
}
