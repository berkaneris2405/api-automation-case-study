package restfulbooker;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;

import io.restassured.response.Response;
import models.request.CreateOrderRequest;
import models.request.LoginRequest;
import models.request.ProductDetails;
import models.response.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import service.WireMockService;

import static io.restassured.RestAssured.given;

public class MockApiTest extends BaseTest {


    public WireMockService wireMockService = new WireMockService();

	@Test
	public void testAuthLoginForSuccessfulUser() throws JsonProcessingException {

		LoginRequest loginRequest = LoginRequest.builder()
				.username("testuser")
				.password("password123")
				.build();

		wireMockService.stubForAuthLoginSuccessfulRequest(loginRequest);

		Response response =given()
				.contentType(ContentType.JSON)
				.body(loginRequest)
				.when()
				.post("/auth/login");

		AuthLoginSuccessfulResponse authRequest = response.as(AuthLoginSuccessfulResponse.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		Assertions.assertThat(authRequest.getToken()).isEqualTo("mock_jwt_token");
	}

	@Test
	public void testAuthLoginForFailedUser() throws JsonProcessingException {
		LoginRequest loginRequest = LoginRequest.builder()
				.username("testuserForFailed")
				.password("password123")
				.build();

		wireMockService.stubForAuthLoginFailedRequest(loginRequest);

		Response response = given()
				.contentType(ContentType.JSON)
				.body(loginRequest)
				.when()
				.post("/auth/login");

		AuthLoginFailedResponse authLoginFailed = response.as(AuthLoginFailedResponse.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(401);
		Assertions.assertThat(authLoginFailed.getMessage()).isEqualTo("Authentication failed");
	}

	@Test
	public void testCreateOrderRequestSuccessful() throws JsonProcessingException {
		CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
				.userId(1001)
				.addressId(3001)
				.productDetails(ProductDetails.builder().productId(555).quantity(2).build())
				.build();

		wireMockService.stubForCreateOrderSuccessfulRequest(createOrderRequest);

		Response response = given()
				.contentType(ContentType.JSON)
				.body(createOrderRequest)
				.when()
				.post("/orders");

		CreateOrderResponse createOrderResponse = response.as(CreateOrderResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(201);
		Assertions.assertThat(createOrderResponse.getOrderId()).isEqualTo("MOCK-ORDER-001");
		Assertions.assertThat(createOrderResponse.getStatus()).isEqualTo("CREATED");
		Assertions.assertThat(createOrderResponse.getQuantity()).isEqualTo(2);
	}

	@Test
	public void testCreateOrderRequestWithMissingUserId() throws JsonProcessingException {
		CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
				.addressId(3001)
				.productDetails(ProductDetails.builder().productId(555).quantity(2).build())
				.build();

		wireMockService.stubForCreateOrderRequestForMissingUserId(createOrderRequest);

		Response response = given()
				.contentType(ContentType.JSON)
				.body(createOrderRequest)
				.when()
				.post("/orders");

		CreateOrderFailedResponse createOrderResponseForMissingUserId = response.as(CreateOrderFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
		Assertions.assertThat(createOrderResponseForMissingUserId.getMessage()).isEqualTo("user id is not found");
	}


	@Test
	public void testCreateOrderRequestWithInvalidQuantity() throws JsonProcessingException {
		CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
				.userId(1001)
				.addressId(3001)
				.productDetails(ProductDetails.builder().productId(555).quantity(0).build())
				.build();

		wireMockService.stubForCreateOrderRequestForInvalidQuantity(createOrderRequest);

		Response response = given()
				.contentType(ContentType.JSON)
				.body(createOrderRequest)
				.when()
				.post("/orders");

		CreateOrderFailedResponse createOrderResponseForInvalidQuantity = response.as(CreateOrderFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
		Assertions.assertThat(createOrderResponseForInvalidQuantity.getMessage()).isEqualTo("Quantity should be greater than 0");
	}


}
