package mockapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import mockapi.models.request.createorder.CreateOrderRequest;
import mockapi.models.request.authlogin.LoginRequest;
import mockapi.models.request.putorder.OrderRequest;
import mockapi.models.request.putorder.PutOrderRequest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class WireMockService {

	public static final String CONTENT_TYPE_HEADER = "Content-Type";

	public static final String APPLICATION_JSON_UTF8 = "application/json; charset=UTF-8";

	private final ObjectMapper objectMapper = new ObjectMapper();

	public void stubForAuthLoginSuccessfulRequest(LoginRequest loginRequest) throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/auth/login"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(loginRequest)))
			.willReturn(aResponse().withStatus(200)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"token\": \"mock_jwt_token\" }")));

	}

	public void stubForAuthLoginFailedRequest(LoginRequest loginRequest) throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/auth/login"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(loginRequest)))
			.willReturn(aResponse().withStatus(401)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Authentication failed\" }")));

	}

	public void stubForCreateOrderSuccessfulRequest(CreateOrderRequest createOrderRequest)
			throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/orders"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(createOrderRequest)))
			.willReturn(aResponse().withStatus(201)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"orderId\": \"MOCK-ORDER-001\", \"status\": \"CREATED\", \"quantity\": 2 }")));

	}

	public void stubForCreateOrderRequestForMissingUserId(CreateOrderRequest createOrderRequest)
			throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/orders"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(createOrderRequest)))
			.willReturn(aResponse().withStatus(400)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"user id is not found\" }")));

	}

	public void stubForCreateOrderRequestForInvalidQuantity(CreateOrderRequest createOrderRequest)
			throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/orders"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(createOrderRequest)))
			.willReturn(aResponse().withStatus(400)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Quantity should be greater than 0\" }")));

	}

	public void stubForCreateOrderRequestForNotFoundProduct(CreateOrderRequest createOrderRequest)
			throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/orders"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(createOrderRequest)))
			.willReturn(aResponse().withStatus(404)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Product not found\" }")));

	}

	public void stubForGetOrderRequest(String orderId) throws JsonProcessingException {

		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/orders/" + orderId))
			.willReturn(aResponse().withStatus(200)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody(
						"{ \"order\": { \"id\": \"MOCK-ORDER-001\", \"status\": \"CREATED\" }, \"item\": { \"productId\": 555, \"quantity\": 2 }, \"shipping\": { \"addressId\": 3001 } }")));

	}

	public void stubForGetOrderRequestWithInvalidOrderId(String orderId) throws JsonProcessingException {

		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/orders/" + orderId))
			.willReturn(aResponse().withStatus(400)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Invalid order Id\" }")));

	}

	public void stubForGetOrderRequestForNotFoundOrder(String orderId) throws JsonProcessingException {

		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/orders/" + orderId))
			.willReturn(aResponse().withStatus(404)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Order not found\" }")));

	}

	public void stubForPutOrderRequest(String orderId, PutOrderRequest putOrderRequest) throws JsonProcessingException {

		WireMock.stubFor(WireMock.put(WireMock.urlEqualTo("/orders/" + orderId))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(putOrderRequest)))
			.willReturn(aResponse().withStatus(200)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Order updated successfully\" }")));

	}

	public void stubForPatchOrdersStatusRequest(OrderRequest orderRequest) throws JsonProcessingException {

		WireMock.stubFor(WireMock.patch(WireMock.urlEqualTo("/orders/status"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(orderRequest)))
			.willReturn(aResponse().withStatus(200)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Order partially updated successfully\" }")));

	}

}
