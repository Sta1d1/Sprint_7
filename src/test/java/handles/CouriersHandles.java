package handles;

import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;

import static io.restassured.RestAssured.given;

public class CouriersHandles {

    private WebDriver driver; // Инициализация драйвера

    public CouriersHandles(WebDriver driver) {
        this.driver = driver;
    }

    /*
     * Метод возвращает количество заказов курьера по id курьера
     * На вход принимает id курьера
     */
    public Response couriersOrders(int id) {
        Response response = given()
                .when()
                .get("/v1/courier/" + id + "/ordersCount");

        return response;
    }

}
