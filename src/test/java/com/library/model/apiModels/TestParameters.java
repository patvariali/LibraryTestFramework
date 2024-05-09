package com.library.model.apiModels;

public record TestParameters(
        UserOrBookResponse userOrBookResponse,
        BookJson bookJson
) {
    public static TestParameters createNewTestParameters(UserOrBookResponse userOrBookResponse, BookJson bookJson) {
        return new TestParameters(userOrBookResponse, bookJson);
    }
}
