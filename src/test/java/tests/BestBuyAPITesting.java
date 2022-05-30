package tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class BestBuyAPITesting {
	
	@BeforeClass
	public void setup() {
		RestAssured.baseURI = "http://localhost/";
		RestAssured.port = 3030;
	}
	
	@Test
	public void verifyGetProduct() {
		RestAssured.given().when().get("/products").then().statusCode(200);
	}
	
	@Test
	public void verifyGetProductWithLimit() {
		RestAssured.given().when().queryParam("$limit", 5).get("/products").then().log().all().statusCode(200);
	}
	
	@Test
	public void verifyGetProductWithSpecificId() {
		RestAssured.given().when().param("id", 43900).get("/products").then().log().all().statusCode(200);
	}
	
	@Test 
	public void verifyPostProduct() {
		String requestPayload = "{\n"
				+ "  \"name\": \"iphone\",\n"
				+ "  \"type\": \"mobile\",\n"
				+ "  \"price\": 3000,\n"
				+ "  \"shipping\": 50,\n"
				+ "  \"upc\": \"moile\",\n"
				+ "  \"description\": \"mobile phone\",\n"
				+ "  \"manufacturer\": \"apple\",\n"
				+ "  \"model\": \"string\",\n"
				+ "  \"url\": \"string\",\n"
				+ "  \"image\": \"string\"\n"
				+ "}";
		RestAssured.given().contentType(ContentType.JSON)
		.body(requestPayload)
		.when().post("/products")
		.then().statusCode(201).log().all();
	}
	
	@Test
	public void verifyUpdateProductWithPayloadAsObject() {
		Map<String, Object> requestpayload = new HashMap<>();
		requestpayload.put("name","iPhone 12");
		requestpayload.put("type", "iPhone Mobile");
		requestpayload.put("upc", "iphone Mobile");
		requestpayload.put("price", 500);
		requestpayload.put("shipping", 20);
		requestpayload.put("description", "iPhone 12");
		requestpayload.put("model", "iPhone 12");
		
		int productId = RestAssured.given().contentType(ContentType.JSON)
		.body(requestpayload)
		.when().post("/products")
		.then().extract().path("id");
		
			requestpayload.put("name","iPhone 12 new");
			requestpayload.put("type", "iPhone Mobile");
			requestpayload.put("upc", "iphone Mobile");
			requestpayload.put("price", 500);
			requestpayload.put("shipping", 20);
			requestpayload.put("description", "iPhone 12");
			requestpayload.put("model", "iPhone 12");
			
			RestAssured.given().contentType(ContentType.JSON)
			.body(requestpayload)
			.when().put("/products/" + productId)
			.then().statusCode(200).log().all();
		
	}

	@Test
	public void verifyDeleteProduct() {
		Map<String, Object> requestpayload = new HashMap<>();
		requestpayload.put("name", "iPhone 12");
		requestpayload.put("type", "iPhone Mobile");
		requestpayload.put("upc", "iphone Mobile");
		requestpayload.put("price", 500);
		requestpayload.put("shipping", 20);
		requestpayload.put("description", "iPhone 12");
		requestpayload.put("model", "iPhone 12");

		int productId = RestAssured.given().contentType(ContentType.JSON).body(requestpayload).when().post("/products")
				.then().extract().path("id");

		RestAssured.given().contentType(ContentType.JSON).when().delete("/products/" + productId).then().statusCode(200)
				.log().all();

		RestAssured.given().when().get("/products/" + productId).then().statusCode(404);

	}

}
