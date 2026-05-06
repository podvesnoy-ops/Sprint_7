package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import org.junit.Ignore;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.models.Courier;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Авторизация курьера")
public class LoginCourierTest extends BaseTest {

    @Test
    @DisplayName("Авторизация курьера с валидными данными возвращает 200 и ID")
    @Description("Проверяет успешную авторизацию существующего курьера. " +
            "Ожидается статус 200 OK и наличие уникального идентификатора (id) в теле ответа.")
    @Step("Авторизация курьера")
    public void loginCourierWithValidCredentialsReturns200() {
        Courier courier = generateCourier();
        apiSteps.createCourier(courier).statusCode(201);

        courierId = apiSteps.loginCourier(courier)
                .statusCode(200)
                .body("id", notNullValue())
                .extract().jsonPath().getString("id");
    }

    @Test
    @DisplayName("Авторизация с неверным паролем возвращает 404")
    @Description("Проверяет реакцию системы на попытку входа с правильным логином, но неверным паролем. " +
            "Ожидается статус 404 Not Found и сообщение 'Учетная запись не найдена'.")
    @Step("Авторизация курьера с неверным паролем")
    public void loginCourierWithWrongPasswordReturns404() {
        Courier courier = generateCourier();
        apiSteps.createCourier(courier).statusCode(201);
        courierId = apiSteps.extractCourierId(courier);

        courier.setPassword("wrong_password");
        apiSteps.loginCourier(courier)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("авторизация под несуществующим пользователем возвращает 404")
    @Description("Проверяет, что система не раскрывает информацию о наличии/отсутствии логина при ошибке. " +
            "Для несуществующего пользователя возвращается тот же статус 404, что и для неверного пароля.")
    @Step("Авторизация с несуществующим логином")
    public void loginWithNonExistentUserReturns404() {
        apiSteps.loginCourier(generateCourier())
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация без передачи login возвращает 400")
    @Description("Негативный тест: проверка валидации входных данных. " +
            "Если поле login отсутствует или равно null, сервер должен вернуть ошибку 400 Bad Request.")
    @Step("Авторизация без передачи login")
    public void loginWithoutLoginFieldReturns400() {
        Courier courier = generateCourier();
        courier.setLogin(null);

        apiSteps.loginCourier(courier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    //@Ignore("Баг стенда: 504 Gateway Timeout при password: null")
    @Test
    @DisplayName("Авторизация без передачи password возвращает 400")
    @Description("Негативный тест: проверка валидации обязательного поля password. " +
            "Если поле password отсутствует или равно null, сервер должен вернуть ошибку 400 Bad Request.")
    @Step("Авторизация без передачи password")
    public void loginWithoutPasswordFieldReturns400() {
        Courier courier = generateCourier();
        courier.setPassword(null);

        apiSteps.loginCourier(courier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}