package com.library.pages;

import com.library.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class BooksPage {
    public BooksPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }
    private final WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(15));

    @FindBy(xpath = "//input[@type='search']")
    private WebElement searchInput;

    @FindAll(
            @FindBy(xpath = "//table[@id='tbl_books']/tbody/tr"))
    private List<WebElement> allBooksByRow;

    public BooksPage search(String key) {
        searchInput.sendKeys(key);
        return new BooksPage();
    }



    public void checkHasBookByNameAndAuthor(String name, String author) {
        List<WebElement> requiredElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//td[3][text()='" + name + "']/following-sibling::td[1][text()='" + author + "']/parent::tr")));
        assertFalse(requiredElements.isEmpty());
    }





}
