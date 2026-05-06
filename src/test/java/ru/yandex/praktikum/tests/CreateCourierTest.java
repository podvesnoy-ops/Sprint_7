package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.models.Courier;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

@DisplayName("Тесты на создание курьера")
public class CreateCourierTest extends BaseTest {

    @Test
    @DisplayName("Создание курьера с валидными данными возвращает 201 и ok:true")
    @Description("Проверяет успешное создание нового курьера при передаче всех обязательных полей. " +
            "Ожидается статус 201 Created и тело ответа {\"ok\": true}.")
    @Step("Создание курьера с валидными данными")
    public void createCourierWithValidDataReturns201() {
        Courier courier = generateCourier();

        apiSteps.createCourier(courier)
                .statusCode(201)
                .body("ok", equalTo(true));

        // Извлекаем ID для последующей очистки в @After
        courierId = apiSteps.extractCourierId(courier);
        assertTrue("ID курьера не должен быть пустым", courierId != null && !courierId.isEmpty());
    }

    @Test
    @DisplayName("Создание дубликата курьера возвращает код 409")
    @Description("Проверяет реакцию системы на попытку создания курьера с логином, который уже занят. " +
            "Ожидается статус 409 Conflict и сообщение об ошибке.")
    @Step("Создание дубликата курьера")
    public void createCourierWithDuplicateLoginReturns409() {
        Courier courier = generateCourier();

        // Создаём курьера
        apiSteps.createCourier(courier).statusCode(201);
        courierId = apiSteps.extractCourierId(courier);

        // Снова создаём курьера
        apiSteps.createCourier(courier)
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без пароля возвращает код 400")
    @Description("Негативный тест: проверка валидации обязательного поля password. " +
            "Если поле отсутствует или равно null, сервер должен вернуть ошибку 400 Bad Request.")
    @Step("Создание курьера без пароля")
    public void createCourierWithoutPasswordReturns400() {
        Courier courier = generateCourier();
        courier.setPassword(null);

        apiSteps.createCourier(courier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без логина возвращает код 400")
    @Description("Негативный тест: проверка валидации обязательного поля login. " +
            "Если поле отсутствует или равно null, сервер должен вернуть ошибку 400 Bad Request.")
    @Step("Создание курьера без логина")
    public void createCourierWithoutLoginReturns400() {
        Courier courier = generateCourier();
        courier.setLogin(null);

        apiSteps.createCourier(courier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}