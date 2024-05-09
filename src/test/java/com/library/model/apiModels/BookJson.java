package com.library.model.apiModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

public record BookJson(
        @JsonProperty("name")
        String name,

        @JsonProperty("isbn")
        String isbn,

        @JsonProperty("year")
        int year,

        @JsonProperty("author")
        String author,

        @JsonProperty("book_category_id")
        int bookCategoryId,

        @JsonProperty("description")
        String description
) {

    public static BookJson getRandomBook() {
        Faker faker = new Faker();
        String name = faker.book().title();
        String isbn = faker.code().isbn10();
        int year = faker.number().numberBetween(1000, 2021);
        String author = faker.book().author();
        int bookCategoryId = faker.number().numberBetween(1, 20);
        String description = faker.chuckNorris().fact();

        return new BookJson(name, isbn, year, author, bookCategoryId, description);
    }
}