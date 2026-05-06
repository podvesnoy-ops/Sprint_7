package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.constants.ApiConstants;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.models.Order;

import static io.restassured.RestAssured.given;

public class ApiSteps {

    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post(ApiConstants.COURIER_ENDPOINT)
                .then();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse loginCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post(ApiConstants.LOGIN_ENDPOINT)
                .then();
    }

    //  Удаление курьера
    public ValidatableResponse deleteCourier(String id) {
        return given()
                .pathParam("id", id)
                .delete(ApiConstants.COURIER_ENDPOINT + "/{id}")
                .then();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .body(order)
                .post(ApiConstants.ORDER_ENDPOINT)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrders() {
        return given()
                .get(ApiConstants.ORDER_ENDPOINT)
                .then();
    }

    //  Получение ID курьера для удаления
    public String extractCourierId(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post(ApiConstants.LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("id");
    }


}