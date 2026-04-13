package handles;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CourierHandles {

    /*
     * Метод для логина курьера в системе
     * Возвращает response c id курьера
     * На вход принимает login курьера и пароль курьера
     */
    @Step("Авторизация курьера")
    public Response loginCourier(String login, String password) {
        Map<String, String> json = new HashMap<>();
        // Добавляем поле только если оно не null — иначе поле отсутствует в запросе
        if (login != null) json.put("login", login);
        if (password != null) json.put("password", password);

        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login");

        return response;

    }

    /*
     * Метод для создания курьера в системе
     * Возвращает response со статусом создания
     * На вход принимает логин нового курьера, пароль и его имя
     */
    @Step("Создание курьера")
    public Response createCourier(String login, String password, String firstName) {
        Map<String, String> json = new HashMap<>();
        // Добавляем поле только если оно не null — иначе поле отсутствует в запросе
        if (login != null) json.put("login", login);
        if (password != null) json.put("password", password);
        if (firstName != null) json.put("firstName", firstName);

        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    /*
     * Метод удаления курьера
     * Возвращает response со статусом удаления
     * На вход принимает id курьера
     */
    @Step("Удаление курьера")
    public Response deleteCourier(int id) {
        Response response = given()
                .when()
                .delete("/api/v1/courier/" + id);

        return response;
    }

    /*
     * Удаление курьера по его логину и паролю
     */
    public Response deleteCourier(String login, String password) {
        // Если логин не прошёл (курьер не был создан) — пропускаем удаление
        Response loginResponse = loginCourier(login, password);
        if (loginResponse.statusCode() != 200) {
            return loginResponse;
        }
        int id = loginResponse.jsonPath().getInt("id");
        return deleteCourier(id);
    }

}
