package mockapi;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.restassured.response.Response;
import mockapi.controller.MockApiController;
import mockapi.models.request.createorder.CreateOrderRequest;
import mockapi.models.request.authlogin.LoginRequest;
import mockapi.models.request.createorder.ProductDetails;
import mockapi.models.request.putorder.ItemRequest;
import mockapi.models.request.putorder.OrderRequest;
import mockapi.models.request.putorder.PutOrderRequest;
import mockapi.models.request.putorder.ShippingRequest;
import mockapi.models.response.authlogin.AuthLoginFailedResponse;
import mockapi.models.response.authlogin.AuthLoginSuccessfulResponse;
import mockapi.models.response.createorder.CreateOrderFailedResponse;
import mockapi.models.response.createorder.CreateOrderResponse;
import mockapi.models.response.getorder.*;
import mockapi.models.response.patchorder.PatchOrderResponse;
import mockapi.models.response.putorder.PutOrderResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import mockapi.service.WireMockService;

public class MockApiTest extends BaseTest {

	public WireMockService wireMockService = new WireMockService();

	public MockApiController mockApiController = new MockApiController();

	@Test
	public void testAuthLoginForSuccessfulUser() throws JsonProcessingException {

		LoginRequest loginRequest = LoginRequest.builder().username("testuser").password("password123").build();

		wireMockService.stubForAuthLoginSuccessfulRequest(loginRequest);

		Response response = mockApiController.postAuthLoginRequest(loginRequest);
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

		Response response = mockApiController.postAuthLoginRequest(loginRequest);
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

		Response response = mockApiController.postCreateOrderRequest(createOrderRequest);
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

		Response response = mockApiController.postCreateOrderRequest(createOrderRequest);
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

		Response response = mockApiController.postCreateOrderRequest(createOrderRequest);
		CreateOrderFailedResponse createOrderResponseForInvalidQuantity = response.as(CreateOrderFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
		Assertions.assertThat(createOrderResponseForInvalidQuantity.getMessage())
			.isEqualTo("Quantity should be greater than 0");
	}

	@Test
	public void testCreateOrderRequestForNotFoundProduct() throws JsonProcessingException {
		CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
			.userId(1001)
			.addressId(3001)
			.productDetails(ProductDetails.builder().productId(1111).quantity(1).build())
			.build();

		wireMockService.stubForCreateOrderRequestForNotFoundProduct(createOrderRequest);
		Response response = mockApiController.postCreateOrderRequest(createOrderRequest);

		CreateOrderFailedResponse createOrderResponseForNotFoundProduct = response.as(CreateOrderFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(404);
		Assertions.assertThat(createOrderResponseForNotFoundProduct.getMessage()).isEqualTo("Product not found");
	}

	@Test
	public void testGetOrderRequest() throws JsonProcessingException {

		wireMockService.stubForGetOrderRequest("MOCK-ORDER-001");
		Response response = mockApiController.getOrdersRequest("MOCK-ORDER-001");

		GetOrderResponse getOrderResponse = response.as(GetOrderResponse.class);
		OrderResponse order = getOrderResponse.getOrder();
		ItemResponse item = getOrderResponse.getItem();
		ShippingResponse shipping = getOrderResponse.getShipping();

		Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		Assertions.assertThat(order.getId()).isEqualTo("MOCK-ORDER-001");
		Assertions.assertThat(order.getStatus()).isEqualTo("CREATED");
		Assertions.assertThat(item.getProductId()).isEqualTo(555);
		Assertions.assertThat(item.getQuantity()).isEqualTo(2);
		Assertions.assertThat(shipping.getAddressId()).isEqualTo(3001);

	}

	@Test
	public void testGetOrderRequestWithInvalidOrderId() throws JsonProcessingException {

		wireMockService.stubForGetOrderRequestWithInvalidOrderId("INVALID-ORDER-ID");
		Response response = mockApiController.getOrdersRequest("INVALID-ORDER-ID");

		GetOrderFailedResponse getOrderResponseForInvalidOrderId = response.as(GetOrderFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
		Assertions.assertThat(getOrderResponseForInvalidOrderId.getMessage()).isEqualTo("Invalid order Id");

	}

	@Test
	public void testGetOrderRequestForNotFoundOrder() throws JsonProcessingException {

		wireMockService.stubForGetOrderRequestForNotFoundOrder("NOT-FOUND-ORDER-ID");
		Response response = mockApiController.getOrdersRequest("NOT-FOUND-ORDER-ID");

		GetOrderFailedResponse getOrderResponseForNotFoundOrder = response.as(GetOrderFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(404);
		Assertions.assertThat(getOrderResponseForNotFoundOrder.getMessage()).isEqualTo("Order not found");

	}

	@Test
	public void testPutOrderRequest() throws JsonProcessingException {
		PutOrderRequest putOrderRequest = PutOrderRequest.builder()
			.item(ItemRequest.builder().productId(590).quantity(10).build())
			.shipping(ShippingRequest.builder().addressId(5001).build())
			.order(OrderRequest.builder().id("MOCK-ORDER-001").status("UPDATED").build())
			.build();

		wireMockService.stubForPutOrderRequest("MOCK-ORDER-001", putOrderRequest);
		Response response = mockApiController.putOrdersRequest("MOCK-ORDER-001", putOrderRequest);

		PutOrderResponse putOrderResponse = response.as(PutOrderResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		Assertions.assertThat(putOrderResponse.getMessage()).isEqualTo("Order updated successfully");

	}

	@Test
	public void testPatchOrdersStatus() throws JsonProcessingException {
		OrderRequest orderRequest = OrderRequest.builder().id("MOCK-ORDER-001").status("CANCELLED").build();

		wireMockService.stubForPatchOrdersStatusRequest(orderRequest);
		Response response = mockApiController.patchOrdersRequest(orderRequest);

		PatchOrderResponse patchOrderResponse = response.as(PatchOrderResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		Assertions.assertThat(patchOrderResponse.getMessage()).isEqualTo("Order partially updated successfully");

	}

}
