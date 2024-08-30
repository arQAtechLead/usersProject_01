Feature: Books API

  Background:
    Given I have access to the Books API

  Scenario: Create a new book
    When I create a new book
    Then the status code should be 200
    And the book should be created successfully

  Scenario: Retrieve book details
    When I retrieve book with specific ID
    Then the status code should be 200
    And I should receive details of the book

  Scenario: Update book details
    When I update book with specific ID
    Then the status code should be 200
    And the book details should be updated successfully

  Scenario: Delete book
    When I delete book with specific ID
    Then the status code should be 200

  Scenario: Retrieve all books
    When I retrieve all books
    Then the status code should be 200
    And I should not see book with specific ID in output

  Scenario: Book creation fails with invalid input data
    When I attempt to create a new book with invalid input data
    Then the status code should be 400
    Then the book creation should fail