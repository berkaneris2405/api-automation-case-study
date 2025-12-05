package mockapi;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

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
		// wiremockServer = new WireMockServer(PORT);
		// wiremockServer.start();
		//
		// configureFor("localhost", wiremockServer.port());
		//
		// RestAssured.baseURI = "http://localhost";
		// RestAssured.port = wiremockServer.port();
	}

}
