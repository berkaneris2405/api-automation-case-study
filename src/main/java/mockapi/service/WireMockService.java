package mockapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import mockapi.models.request.CreateOrderRequest;
import mockapi.models.request.LoginRequest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class WireMockService {

	public static final String CONTENT_TYPE_HEADER = "Content-Type";

	public static final String APPLICATION_JSON_UTF8 = "application/json; charset=UTF-8";

	private final ObjectMapper objectMapper = new ObjectMapper();

	public void stubForAuthLoginSuccessfulRequest(LoginRequest loginRequest) throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/auth/login"))
			.withRequestBody(WireMock.equalTo(objectMapper.writeValueAsString(loginRequest)))
			.willReturn(aResponse().withStatus(200)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"token\": \"mock_jwt_token\" }")));

	}

	public void stubForAuthLoginFailedRequest(LoginRequest loginRequest) throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/auth/login"))
			.withRequestBody(WireMock.equalTo(objectMapper.writeValueAsString(loginRequest)))
			.willReturn(aResponse().withStatus(401)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Authentication failed\" }")));

	}

	public void stubForCreateOrderSuccessfulRequest(CreateOrderRequest createOrderRequest)
			throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/orders"))
			.withRequestBody(WireMock.equalTo(objectMapper.writeValueAsString(createOrderRequest)))
			.willReturn(aResponse().withStatus(201)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"orderId\": \"MOCK-ORDER-001\", \"status\": \"CREATED\", \"quantity\": 2 }")));

	}

	public void stubForCreateOrderRequestForMissingUserId(CreateOrderRequest createOrderRequest)
			throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/orders"))
			.withRequestBody(WireMock.equalTo(objectMapper.writeValueAsString(createOrderRequest)))
			.willReturn(aResponse().withStatus(400)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"user id is not found\" }")));

	}

	public void stubForCreateOrderRequestForInvalidQuantity(CreateOrderRequest createOrderRequest)
			throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/orders"))
			.withRequestBody(WireMock.equalTo(objectMapper.writeValueAsString(createOrderRequest)))
			.willReturn(aResponse().withStatus(400)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Quantity should be greater than 0\" }")));

	}

}
