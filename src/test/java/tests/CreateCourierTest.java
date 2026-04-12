package tests;

import handles.CourierHandles;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    String courierLogin;
    String courierPassword;
    String courierName;


    @BeforeEach
    public void setUp() {
        Date date = new Date();
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courierLogin = "TestLogin" + date.getTime();
        courierPassword = "TestPass" + date.getTime();
        courierName = "Виталик";
    }


    @Test
    public void successfulСreationCourier() {
        CourierHandles courierHandles = new CourierHandles();

        // Проверка создания курьера
        Response response = courierHandles.createCourier(courierLogin, courierPassword, courierName);
        response.then().assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));

    }

    @Test
    public void duplicateCourier() {
        CourierHandles courierHandles = new CourierHandles();

        // Создаю курьера (должна быть 201)
        Response response_first = courierHandles.createCourier(courierLogin, courierPassword, courierName);
        response_first.then().assertThat().statusCode(201)
                .body("ok", equalTo(true));

        // Создаю курьера с теми же данными что и были (должна быть 409 ошибка)
        Response response_second = courierHandles.createCourier(courierLogin, courierPassword, courierName);
        response_second.then().assertThat().statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));


    }


    static Stream<Object[]> missingFieldCases() {
        return Stream.of(
                new Object[]{null, "TestPass", "Недостаточно данных для создания учетной записи"},
                new Object[]{"TestLogin", null, "Недостаточно данных для создания учетной записи"},
                new Object[]{null, null, "Недостаточно данных для создания учетной записи"}
        );
    }

    @ParameterizedTest(name = "login={0}, password={1}")
    @MethodSource("missingFieldCases")
    public void createCourierWithMissingField(String login, String password, String expectedMessage) {
        CourierHandles courierHandles = new CourierHandles();

        Response response = courierHandles.createCourier(login, password, courierName);
        response.then().assertThat()
                .statusCode(400)
                .body("message", equalTo(expectedMessage));
    }

    @AfterEach
    public void afterTest() {
        CourierHandles courierHandles = new CourierHandles();
        // Удаление курьера
        courierHandles.deleteCourier(courierLogin, courierPassword);
    }

}
