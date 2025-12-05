package restfulbooker;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


public abstract class BaseTest {


	protected static WireMockServer wiremockServer;
	private static final int PORT = 8081;

	@BeforeAll
	public static void setupAllure() {
		RestAssured.filters(new AllureRestAssured());

		wiremockServer = new WireMockServer(PORT);
		wiremockServer.start();

		configureFor("localhost", wiremockServer.port());

		RestAssured.baseURI = "http://localhost";
		RestAssured.port = wiremockServer.port();
	}

	@BeforeEach
	public void setup() {
//		wiremockServer = new WireMockServer(PORT);
//		wiremockServer.start();
//
//		configureFor("localhost", wiremockServer.port());
//
//		RestAssured.baseURI = "http://localhost";
//		RestAssured.port = wiremockServer.port();
	}

}
