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
    protected Courier courierForCleanup;
    protected Integer orderTrack;

    @Before
    public void setUp() {
        RestAssured.baseURI = ApiConstants.BASE_URL;
        apiSteps = new ApiSteps();
        courierId = null;
        courierForCleanup = null;
        orderTrack = null;
    }

    @After
    public void tearDown() {
        // Удаление курьера
        if (courierId != null && !courierId.isBlank()) {
            try {
                apiSteps.deleteCourier(courierId)
                        .statusCode(200)
                        .body("ok", org.hamcrest.Matchers.equalTo(true));
            } catch (AssertionError e) {
                System.err.println("Не удалось удалить курьера ID=" + courierId);
            }
        }

        // Отмена заказа — мягкая, не роняет тест
        if (orderTrack != null) {
            try {
                apiSteps.cancelOrder(orderTrack)
                        .statusCode(200)
                        .body("ok", org.hamcrest.Matchers.equalTo(true));
            } catch (AssertionError e) {
                System.err.println("Не удалось отменить заказ track=" + orderTrack);
            }
        }
    }

    protected Courier generateCourier() {
        return new Courier(
                "courier_" + UUID.randomUUID().toString().substring(0, 8),
                ApiConstants.DEFAULT_PASSWORD,
                "Test", "User", "Moscow", "+79000000000", "black"
        );
    }

    // подготовка тестовых данных
    protected Courier getCourierWithWrongPassword(Courier base) {
        return new Courier(base.getLogin(), "wrong_password", null, null, null, null, null);
    }

    protected Courier getNonExistentCourier() {
        return generateCourier();
    }

    protected Courier getCourierWithoutLogin(Courier base) {
        return new Courier(null, base.getPassword(), null, null, null, null, null);
    }

    protected Courier getCourierWithoutPassword(Courier base) {
        return new Courier(base.getLogin(), null, null, null, null, null, null);
    }
}