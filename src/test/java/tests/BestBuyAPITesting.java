package tests;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BestBuyAPITesting {
	
	@BeforeClass
	public void setup() {
		RestAssured.baseURI = "http://localhost/";
		RestAssured.port = 3030;
	}
	
	@Test
	public void verifyGetProduct() {
		RestAssured.given().when().get("/products").then().log().all().statusCode(200);
		
	}
	//get
	@Test
	public void getRequestWithQueryparam() {
		Response response = RestAssured.given()
				.contentType(ContentType.JSON)
				.when()
				.queryParam("$limit", 1)
				.get("/products")
				.then()
				.log()
				.all()
				.extract().response();
		
		String headerValue = response.getHeader("Content-Type");
		Assert.assertEquals("application/json; charset=utf-8", headerValue);
		Assert.assertEquals(200, response.statusCode());
		Assert.assertEquals(response.jsonPath().getString("data[0].name"),"Duracell - AAA Batteries (4-Pack)");
		
		
	}
	
	@Test
	public void verifyGetProductWithLimit() {
		RestAssured.given().when().queryParam("$limit",1).get("/products").then().log().all().statusCode(200);
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
		.then()
		.statusCode(201).log().all();

		 
	
	}
	
	@Test
	public void verifyHeader() throws ParseException {
		String header = "[{\r\n"
				+ "  \"access-control-allow-origin\": \"*\",\r\n"
				+ "  \"allow\": \"GET,POST,PUT,PATCH,DELETE\",\r\n"
				+ "  \"connection\": \"keep-alive\",\r\n"
				+ "  \"content-encoding\": \"gzip\",\r\n"
				+ "  \"content-type\": \"application/json; charset=utf-8\",\r\n"
				+ "  \"date\": \"Thu, 07 Jul 2022 04:07:14 GMT\",\r\n"
				+ "  \"etag\": \"W/\\\"2caa-gGrPf+r5VRWlL/fiiXe6JNfANf0\\\"\",\r\n"
				+ "  \"keep-alive\": \"timeout=5\",\r\n"
				+ "  \"transfer-encoding\": \"chunked\",\r\n"
				+ "  \"vary\": \"Accept, Accept-Encoding\",\r\n"
				+ "  \"x-powered-by\": \"Express\"\r\n"
				+ "}]"; 
		JSONObject json = new JSONObject(header);
		JSONArray jarray = new JSONArray(header);
		int jarraySize = jarray.length();
		System.out.println(jarraySize);
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "/products");
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
//		response.then().log().all();
		
		Headers headers = response.getHeaders();
		int countOfHeaders = headers.asList().size();
		Assert.assertEquals(jarraySize, countOfHeaders);
		
		String headerValue = response.getHeader("Content-Type");
		Assert.assertEquals("application/json; charset=utf-8", headerValue);
		
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
