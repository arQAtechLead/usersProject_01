package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import static org.junit.Assert.*;

public class BooksStepDefinitions {

    private Response response;
    private final String uri = "http://77.102.250.113:17354/api/v1/books/";
    private final String name = "user4";
    private final String pwd = "hlB5U1rA";
    static int bookId;

    @Given("I have access to the Books API")
    public void i_have_access_to_the_Books_API() {
        // Set up base URL + authentication method for the Books API
        RequestSpecification httpRequest = RestAssured.given().auth().basic(name, pwd);
        response = httpRequest.get(uri);
    }

    @Then("the status code should be {int}")
    public void the_status_code_should_be_ID(int status) {
        assertNotNull(response);
        response.then().statusCode(status);
    }

    @When("I create a new book")
    public void i_create_a_new_book() {
        // Set up authentication method for the Books API
        RequestSpecification httpRequest = RestAssured.given().auth().basic(name, pwd);
        // Define the request payload for creating a new book
        String requestBody = "{"
                + "\"name\": \"Refactoring: Improving the Design of Existing Code\","
                + "\"author\": \"Martin Fowler\","
                + "\"publication\": \"Addison-Wesley Professional\","
                + "\"category\": \"Programming\","
                + "\"pages\": \"448\","
                + "\"price\": \"36.6\""
                + "}";

        // Send a POST request to create a new book
        response = httpRequest.given()
                .contentType("application/json")
                .body(requestBody)
                .post(uri);
    }

    @And("the book should be created successfully")
    public void the_book_should_be_created_successfully() {
        // Validate that the response contains book details
        JsonPath jsonPath = response.jsonPath();
        assertNotNull("Refactoring: Improving the Design of Existing Code", jsonPath.getString("name"));
        assertNotNull("Martin Fowler", jsonPath.getString("author"));
        assertNotNull("36.6", jsonPath.getString("price"));
        bookId = Integer.parseInt(jsonPath.getString("id"));
    }

    @When("I retrieve book with specific ID")
    public void i_retrieve_book_with_specific_ID() {
        // Set up authentication method for the Books API
        RequestSpecification httpRequest = RestAssured.given().auth().basic(name, pwd);
        response = httpRequest.get(uri + bookId);
    }

    @When("I retrieve all books")
    public void i_retrieve_all_books() {
        // Set up authentication method for the Books API
        RequestSpecification httpRequest = RestAssured.given().auth().basic(name, pwd);
        response = httpRequest.get(uri);
    }

    @And("I should not see book with specific ID in output")
        public void i_should_not_receive_details_of_the_specific_book() {
        JsonPath jsonPath = response.jsonPath();

        String id = jsonPath.getString("data.id");
        Assert.assertFalse(id.contains(String.valueOf(bookId))); //  in real API should be False as we removed the book in previous step
    }

    @Then("I should receive details of the book")
    public void i_should_receive_details_of_the_book() {
        // Validate that the response contains book details
        JsonPath jsonPath = response.jsonPath();
        assertNotNull("Refactoring: Improving the Design of Existing Code", jsonPath.getString("name"));
        assertNotNull("Martin Fowler", jsonPath.getString("author"));
        assertNotNull("Addison-Wesley Professional", jsonPath.getString("publication"));
        assertNotNull("35.5", jsonPath.getString("price"));
    }

    @When("I update book with specific ID")
    public void i_update_book_with_ID() {
        // Set up authentication method for the Books API
        RequestSpecification httpRequest = RestAssured.given().auth().basic(name, pwd);
        // Define the request payload for updating the book
        String requestBody = "{"
                + "\"name\": \"Updated: Improving the Design of Existing Code\","
                + "\"author\": \"Fowler Martin\""
                + "}";

        // Send a PUT request to update the book with the given ID
        response = httpRequest.given()
                .contentType("application/json")
                .body(requestBody)
                .put(uri + bookId);
    }

    @And("the book details should be updated successfully")
    public void the_book_details_should_be_updated_successfully() {
        JsonPath jsonPath = response.jsonPath();
        assertEquals("Updated: Improving the Design of Existing Code", jsonPath.getString("name"));
        assertEquals("Fowler Martin", jsonPath.getString("author"));
    }

    @When("I delete book with specific ID")
    public void i_delete_book_with_ID() {
        // Set up authentication method for the Books API
        RequestSpecification httpRequest = RestAssured.given().auth().basic(name, pwd);
        // Send a DELETE request to delete the book with the given ID
        response = httpRequest.delete(uri + bookId);
    }

    @When("I attempt to create a new book with invalid input data")
    public void i_attempt_to_create_a_new_book_with_invalid_input_data() {
        // Set up authentication method for the Books API
        RequestSpecification httpRequest = RestAssured.given().auth().basic(name, pwd);
        // Define the request payload with invalid input data
        String requestBody = "{"
                + "\"email\": \"fake@email\","
                + "}";

        // Send a POST request to create a new book with invalid input data
        response = httpRequest.given()
                .contentType("application/json")
                .body(requestBody)
                .post(uri);
    }

    @And("the book creation should fail")
    public void the_book_creation_should_fail() {
        response.then().statusCode(400);
    }
}
