package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;

import java.util.List;
import static org.hamcrest.Matchers.instanceOf;

@DisplayName("Проверка получения списка заказов")
public class GetOrdersTest extends BaseTest {

    @Test
    @DisplayName("Получение списка заказов возвращает 200 и список orders")
    @Description("Проверяет возможность получения списка всех заказов. " +
            "Ожидается статус 200 OK и тело ответа, содержащее массив 'orders'.")
    @Step("Запрос списка заказов")
    public void getOrdersListReturns200AndOrdersList() {
        apiSteps.getOrders()
                .statusCode(200)
                .body("orders", instanceOf(List.class));
    }
}