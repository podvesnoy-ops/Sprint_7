package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.models.Courier;

import static org.hamcrest.Matchers.equalTo;

@DisplayName("Тесты на создание курьера")
public class CreateCourierTest extends BaseTest {

    // ✅ Шаг извлечения ID вынесен сюда, как требовал ревьювер
    @After
    public void extractIdForCleanup() {
        if (courierForCleanup != null) {
            try {
                courierId = apiSteps.extractCourierId(courierForCleanup);
            } catch (AssertionError ignored) {

            }
        }
    }

    @Test
    @DisplayName("Создание курьера с валидными данными возвращает 201 и ok:true")
    @Description("Проверяет успешное создание нового курьера.")
    @Step("Создание курьера с валидными данными")
    public void createCourierWithValidDataReturns201() {
        Courier courier = generateCourier();
        courierForCleanup = courier; // для @After

        apiSteps.createCourier(courier)
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание дубликата курьера возвращает код 409")
    @Description("Проверяет реакцию системы на попытку создания курьера с занятым логином.")
    @Step("Создание дубликата курьера")
    public void createCourierWithDuplicateLoginReturns409() {
        Courier courier = generateCourier();
        courierForCleanup = courier;

        apiSteps.createCourier(courier).statusCode(HttpStatus.SC_CREATED);

        apiSteps.createCourier(courier)
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без пароля возвращает код 400")
    @Description("Негативный тест: проверка валидации поля password.")
    @Step("Создание курьера без пароля")
    public void createCourierWithoutPasswordReturns400() {
        Courier courier = generateCourier();
        courierForCleanup = courier;
        courier.setPassword(null);

        apiSteps.createCourier(courier)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без логина возвращает код 400")
    @Description("Негативный тест: проверка валидации поля login.")
    @Step("Создание курьера без логина")
    public void createCourierWithoutLoginReturns400() {
        Courier courier = generateCourier();
        courierForCleanup = courier;
        courier.setLogin(null);

        apiSteps.createCourier(courier)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}