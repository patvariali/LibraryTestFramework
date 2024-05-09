package com.library.test;

import com.library.jupiter.annotation.api.AddBook;
import com.library.jupiter.annotation.User;
import com.library.jupiter.annotation.api.GetAllUsers;
import com.library.jupiter.annotation.api.GetUserById;
import com.library.jupiter.annotation.meta.Api;
import com.library.jupiter.annotation.meta.WebTest;
import com.library.model.apiModels.AllUserModel;
import com.library.model.apiModels.TestParameters;
import com.library.model.apiModels.UserJson;
import com.library.pages.LoginPage;
import com.library.utilities.ConfigurationReader;
import com.library.utilities.Driver;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.library.jupiter.annotation.User.UserType.LIBRARIAN;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/* Scenario: Retrieve all users from the API endpoint
    Given the user logged in Library API as a "librarian"
    And Accept header is "application/json"
    When the user sends GET request to "/get_all_users" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And "id" field should not be null
    And "name" field should not be null  */

@Api
public class LibraryTest {

    @User(userType = LIBRARIAN)
    @GetAllUsers()
    @Test
    void testGetAllUsersEndpoint(List<AllUserModel> response) {
        System.out.println(System.getProperty("user"));
        Allure.step("And 'id' field should not be null\n" +
                "And 'name' field should not be null", () -> {

            for (AllUserModel each : response) {
                assertThat(each.getId(), notNullValue());
                assertThat(each.getName(), notNullValue());

            }
        });

    }

    /*
    Scenario: Retrieve single user
    Given the user logged in Library API as a "librarian"
    And Accept header is "application/json"
    And Path param is 1
    When the user sends GET request to "/get_user_by_id/{id}" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And "id" field should be same with path param
    And following fields should not be null
      |full_name|
      |email|
      |password|
     */
    @User(userType = LIBRARIAN)
    @GetUserById(
            id = "1"
    )
    @Test
    void testGetSingleUserEndpoint(UserJson response) {
        Allure.step("And full_name, email, password fields should not be null", () -> {

            assertThat(response.fullName(), notNullValue());
            assertThat(response.email(), notNullValue());

        });

    }

    /*
    Scenario: Create a new book API
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    And Request Content Type header is "application/x-www-form-urlencoded"
    And I create a random "book" as request body
    When I send POST request to "/add_book" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And the field value for "message" path should be equal to "The book has been created."
    And "book_id" field should not be null
     */

    @User(userType = LIBRARIAN)
    @AddBook()
    @Test
    void testCreateBook(TestParameters testParameters) {
        Allure.step("The field value for 'message' path should be equal to 'The book has been created.'", () -> {

            assertTrue(testParameters.userOrBookResponse().message().equals("The book has been created."));

        });

        Allure.step("And 'book_id' field should not be null.", () -> {

            assertThat(testParameters.userOrBookResponse().book_id(), notNullValue());

        });
    }

    @User(userType = LIBRARIAN)
    @AddBook
    @Test
    @WebTest
    void testCreateBookAllLayers(TestParameters testParameters) {
        Allure.step("And 'book_id' field should not be null.", () -> {

            assertThat(testParameters.userOrBookResponse().book_id(), notNullValue());

        });

        String name = testParameters.bookJson().name();
        String author = testParameters.bookJson().author();

        Allure.step("UI, Database and API created book information must match", () -> {

        });
            Driver.open("https://library2.cydeo.com/login.html", LoginPage.class)
                    .doLogin("librarian10@library", "libraryUser")
                    .goToBooks()
                    .search(author)
                    .checkHasBookByNameAndAuthor(name, author);


    }

    @User(userType = LIBRARIAN)
    @AddBook
    @Test
    @WebTest
    void explicitlyFailedTest(TestParameters testParameters) {
        String name = testParameters.bookJson().name();
        String author = testParameters.bookJson().author();

        Allure.step("And 'book_id' field should not be null.", () -> {

            assertThat(testParameters.userOrBookResponse().book_id(), notNullValue());

        });

        Allure.step("UI, Database and API created book information must match", () -> {

            Driver.open("https://en.wikipedia.org/wiki/Black_hole", LoginPage.class)
                    .doLogin("librarian10@library", "libraryUser")
                    .goToBooks()
                    .search(author)
                    .checkHasBookByNameAndAuthor(name, author);
            fail();

        });
    }
}
