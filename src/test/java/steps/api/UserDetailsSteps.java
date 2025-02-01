package steps.api;
//import static io.restassured.RestAssured.*;
//import static org.hamcrest.Matchers.*;
//
//import io.cucumber.java.en.*;
//
//public class UserDetailsSteps {
//
//    private String endpoint;
//
//    
//    @Given("the API endpoint is set to \\/users\\/{string}")
//    public void the_api_endpoint_is_set_to(String userId) {
//    	 this.endpoint = "/users/"+userId;
//    }
//    @When("a GET request is sent to the endpoint")
//    public void a_get_request_is_sent_to_the() {
//    	given()
//        .baseUri("https://jsonplaceholder.typicode.com")
//    .when()
//        .get(endpoint)
//    .then()
//        .log().all(); // Logs the response
//    }
//    @Then("the status code should be {int}")
//    public void the_status_code_should_be(int statusCode) {
//    	given()
//        .baseUri("https://jsonplaceholder.typicode.com")
//    .when()
//        .get(endpoint)
//    .then()
//        .statusCode(statusCode);
//    }
//    @Then("the response body should contain the string {string}")
//    public void the_response_body_should_contain_the_string(String responseMessage) {
//    	 given()
//         .baseUri("https://jsonplaceholder.typicode.com")
//     .when()
//         .get(endpoint)
//     .then()
//         .body(containsString(responseMessage));
//    }
//}



