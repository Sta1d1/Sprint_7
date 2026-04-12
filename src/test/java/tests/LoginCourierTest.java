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

import static org.hamcrest.Matchers.*;

public class LoginCourierTest {
    String courierLogin;
    String courierPassword;
    String courierName;


    @BeforeEach
    public void setUp() {
        Date date = new Date();
        CourierHandles courierHandles = new CourierHandles();

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courierLogin = "TestLogin" + date.getTime();
        courierPassword = "TestPass" + date.getTime();
        courierName = "Виталик";
        // Создание курьера
        Response response = courierHandles.createCourier(courierLogin, courierPassword, courierName);
        response.then().assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    public void correctLogin() {
        CourierHandles courierHandles = new CourierHandles();

        Response response = courierHandles.loginCourier(courierLogin, courierPassword);
        response.then().assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("id", greaterThan(0));
    }


    static Stream<Integer> incorrectCredentialCases() {
        return Stream.of(0, 1, 2);
    }

    @ParameterizedTest(name = "case={0}")
    @MethodSource("incorrectCredentialCases")
    public void authIncorrectCredential(int caseIndex) {
        CourierHandles courierHandles = new CourierHandles();

        // Подставляем нужные креденшелы в зависимости от кейса
        String login = (caseIndex == 0) ? courierLogin : "IncorrectLogin";    // кейс 0: верный логин
        String password = (caseIndex == 1) ? courierPassword : "IncorrectPassword"; // кейс 1: верный пароль

        Response response = courierHandles.loginCourier(login, password);
        response.then().assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }


    static Stream<Object[]> missingFieldCases() {
        return Stream.of(
                new Object[]{null, "TestPass", "Недостаточно данных для входа"},  // нет логина
                new Object[]{"TestLogin", null, "Недостаточно данных для входа"}   // нет пароля
        );
    }

    @ParameterizedTest(name = "login={0}, password={1}")
    @MethodSource("missingFieldCases")
    public void loginWithMissingField(String login, String password, String expectedMessage) {
        CourierHandles courierHandles = new CourierHandles();

        Response response = courierHandles.loginCourier(login, password);
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
