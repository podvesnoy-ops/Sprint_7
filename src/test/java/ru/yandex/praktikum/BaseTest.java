package ru.yandex.praktikum;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import ru.yandex.praktikum.constants.ApiConstants;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.steps.ApiSteps;

import java.util.UUID;

public class BaseTest {
    protected ApiSteps apiSteps;
    protected String courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = ApiConstants.BASE_URL;
        apiSteps = new ApiSteps();
        courierId = null;
    }
    // Удаление курьера, если он был создан
    @After
    public void tearDown() {
        if (courierId != null && !courierId.isBlank()) {
            try {
                apiSteps.deleteCourier(courierId)
                        .statusCode(200)
                        .body("ok", org.hamcrest.Matchers.equalTo(true));
            } catch (AssertionError e) {
                System.err.println("⚠️ Ошибка очистки курьера ID=" + courierId + ": " + e.getMessage());
            }
        }
    }

    // Создание уникального курьера
    protected Courier generateCourier() {
        return new Courier(
                "courier_" + UUID.randomUUID().toString().substring(0, 8),
                ApiConstants.DEFAULT_PASSWORD,
                "Test", "User", "Moscow", "+79000000000", "black"
        );
    }
}