package com.library.pages;

import com.library.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage {
    public MainPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//a[@href='#books']")
    private WebElement booksNav;

    public BooksPage goToBooks() {
        booksNav.click();
        return new BooksPage();
    }
}
