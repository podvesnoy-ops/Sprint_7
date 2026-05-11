package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.models.Courier;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Авторизация курьера")
public class LoginCourierTest extends BaseTest {

    private Courier testCourier;

    @Before
    public void createTestCourier() {
        testCourier = generateCourier();
        apiSteps.createCourier(testCourier).statusCode(HttpStatus.SC_CREATED);
        courierId = apiSteps.extractCourierId(testCourier);
    }

    @Test
    @DisplayName("Авторизация курьера с валидными данными возвращает 200 и ID")
    @Description("Проверяет успешную авторизацию существующего курьера.")
    public void loginCourierWithValidCredentialsReturns200() {
        apiSteps.loginCourier(testCourier)
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверным паролем возвращает 404")
    @Description("Проверяет реакцию на вход с правильным логином, но неверным паролем.")
    public void loginCourierWithWrongPasswordReturns404() {
        apiSteps.loginCourier(getCourierWithWrongPassword(testCourier))
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация под несуществующим пользователем возвращает 404")
    @Description("Проверяет, что для несуществующего пользователя возвращается 404.")
    public void loginWithNonExistentUserReturns404() {
        apiSteps.loginCourier(getNonExistentCourier())
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация без передачи login возвращает 400")
    @Description("Негативный тест: проверка валидации поля login.")
    public void loginWithoutLoginFieldReturns400() {
        apiSteps.loginCourier(getCourierWithoutLogin(testCourier))
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    //@Ignore("Баг стенда: 504 Gateway Timeout при password: null")
    @Test
    @DisplayName("Авторизация без передачи password возвращает 400")
    @Description("Негативный тест: проверка валидации поля password.")
    public void loginWithoutPasswordFieldReturns400() {
        apiSteps.loginCourier(getCourierWithoutPassword(testCourier))
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}