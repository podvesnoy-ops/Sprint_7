package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.models.Order;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Создание заказа")
@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {

    private final List<String> colors;

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Создание заказа (Передаваемые цвета: {0}) возвращает 201 и track")
    public static Collection<Object[]> getOrderColorData() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @Test
    @Step("Проверка создания заказа")
    @Description("Параметризованный тест: проверка создания заказа.")
    public void createOrderReturnsTrack() {
        Order order = new Order(colors);

        //для @After
        orderTrack = apiSteps.createOrder(order)
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue())
                .extract()
                .jsonPath()
                .getInt("track");
    }
}