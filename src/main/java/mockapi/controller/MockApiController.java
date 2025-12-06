package mockapi.controller;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import mockapi.models.request.createorder.CreateOrderRequest;
import mockapi.models.request.authlogin.LoginRequest;
import mockapi.models.request.putorder.OrderRequest;
import mockapi.models.request.putorder.PutOrderRequest;

import static io.restassured.RestAssured.given;

@Data
@AllArgsConstructor
public class MockApiController {

    public Response postAuthLoginRequest(LoginRequest loginRequest) {
       return given().contentType(ContentType.JSON).body(loginRequest).when().post("/auth/login");
    }

    public Response postCreateOrderRequest(CreateOrderRequest createOrderRequest) {
        return given().contentType(ContentType.JSON).body(createOrderRequest).when().post("/orders");
    }

    public Response getOrdersRequest(String orderId) {
        return given().contentType(ContentType.JSON).when().get("/orders/" + orderId);
    }

    public Response putOrdersRequest(String orderId, PutOrderRequest putOrderRequest) {
        return given().contentType(ContentType.JSON).body(putOrderRequest).when().put("/orders/" + orderId);
    }

    public Response patchOrdersRequest(OrderRequest orderRequest) {
        return given().contentType(ContentType.JSON).body(orderRequest).when().patch("/orders/status");
    }
}
