package handles;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrdersHandles {

    /*
     * Метод для создания заказа
     * Возвращает response с track-номером заказа
     */
    public Response createOrder(String firstName,
                                String lastName,
                                String address,
                                int metroStation,
                                String phone,
                                int rentTime,
                                String deliveryDate,
                                String comment,
                                String[] color
    ) {
        Map<String, Object> json = new HashMap<>();
        json.put("firstName", firstName);
        json.put("lastName", lastName);
        json.put("address", address);
        json.put("metroStation", metroStation);
        json.put("phone", phone);
        json.put("rentTime", rentTime);
        json.put("deliveryDate", deliveryDate);
        json.put("comment", comment);
        json.put("color", color);

        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/orders");
    }

    /*
     * Метод для получения списка заказов
     * Возвращает response со списком заказов
     */
    public Response getOrders() {
        return given()
                .when()
                .get("/api/v1/orders");
    }
}
