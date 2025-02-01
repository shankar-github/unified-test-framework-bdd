package com.ui.automation.framework.pages;

import com.ui.automation.framework.pages.BasePage;
import com.ui.automation.framework.pages.SearchResultPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignInPage extends BasePage {

    // Locators using Page Factory annotations
    @FindBy(id = "search_query_top")
    private WebElement searchBox;

    @FindBy(name = "submit_search")
    private WebElement searchButton;

    @FindBy(linkText = "Contact us")
    private WebElement contactUsLink;

    @FindBy(linkText = "Sign in")
    private WebElement signInLink;

    // Constructor
    public SignInPage(WebDriver driver) {
        super(driver);  // Call the constructor of BasePage
        PageFactory.initElements(driver, this);  // Initialize Page Factory elements
    }

    // Enter search text
    public void enterSearchText(String searchText) {
        typeKeys(searchText, searchBox);
    }

    // Click on the search button
    public void clickSearchButton() {
        elementClick(searchButton);
    }

    // Click on the Contact Us link
    public void clickContactUsLink() {
        elementClick(contactUsLink);
    }

    // Click on the Sign In link
    public void clickSignInLink() {
        elementClick(signInLink);
    }

    // Perform search and return SearchResultPage
    public SearchResultPage performSearch(String searchFor) {
        enterSearchText(searchFor);
        clickSearchButton();
        return new SearchResultPage(driver);
    }

    // Navigate to Contact Us page
    public void navigateToContactUs() {
        clickContactUsLink();
    }

    // Navigate to Sign In page
    public void navigateToSignInPage() {
        clickSignInLink();
    }
}
