package mockapi;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

public abstract class BaseTest {

	protected static WireMockServer wiremockServer;

	@BeforeAll
	public static void setupAllure() {
		RestAssured.filters(new AllureRestAssured());

		wiremockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
		wiremockServer.start();

		System.out.println("WireMock Server started on port: " + wiremockServer.port());

		configureFor("localhost", wiremockServer.port());

		RestAssured.baseURI = "http://localhost";
		RestAssured.port = wiremockServer.port();
	}


}
