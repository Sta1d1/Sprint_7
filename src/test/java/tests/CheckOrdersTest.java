package tests;

import handles.OrdersHandles;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

public class CheckOrdersTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    static Stream<Arguments> colorCases() {
        return Stream.of(
                Arguments.of((Object) new String[]{"BLACK"}),
                Arguments.of((Object) new String[]{"GREY"}),
                Arguments.of((Object) new String[]{"BLACK", "GREY"}),
                Arguments.of((Object) new String[]{})
        );
    }

    @ParameterizedTest(name = "color={0}")
    @MethodSource("colorCases")
    public void checkCreateOrder(String[] color) {
        OrdersHandles ordersHandles = new OrdersHandles();

        Response response = ordersHandles.createOrder(
                "Naruto", "Uchiha", "Konoha, 142 apt.", 4,
                "+7 800 355 35 35", 5, "2020-06-06",
                "Saske, come back to Konoha", color);

        response.then().assertThat()
                .statusCode(201)
                .body("track", notNullValue())             // поле присутствует
                .body("track", instanceOf(Integer.class)); // значение является int
    }

    @Test
    public void getOrdersListNotEmpty() {
        OrdersHandles ordersHandles = new OrdersHandles();

        ordersHandles.getOrders()
                .then().assertThat()
                .statusCode(200)
                // Список заказов присутствует и не пустой
                .body("orders", notNullValue())
                .body("orders", hasSize(greaterThan(0)))
                // Проверяем структуру первого элемента списка
                .body("orders[0].id", notNullValue())
                .body("orders[0].track", notNullValue())
                .body("orders[0].status", notNullValue())
                .body("orders[0].createdAt", notNullValue())
                .body("orders[0].updatedAt", notNullValue());
    }
}
