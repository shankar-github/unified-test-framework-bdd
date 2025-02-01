package ui.pages;

import ui.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends BasePage {

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
    public HomePage(WebDriver driver) {
        super(driver);  // Call the constructor of BasePage
        PageFactory.initElements(driver, this);  // Initialize Page Factory elements
    }

    // Enter search text in the search box
    private void enterSearchText(String searchText) {
        sendKeys(searchBox, searchText);
    }

    // Click the search button
    private void clickSearchButton() {
        click(searchButton);
    }

    // Click the Contact Us link
    private void clickContactUsLink() {
        click(contactUsLink);
    }

    // Click the Sign In link
    private void clickSignInLink() {
        click(signInLink);
    }

    // Perform search action and return a SearchResultPage instance
    public SearchResultPage performSearch(String searchFor) {
        enterSearchText(searchFor);
        clickSearchButton();
        return new SearchResultPage(driver);
    }

    // Navigate to the Contact Us page and return a ContactUsPage instance
    public ContactUsPage navigateToContactUs() {
        clickContactUsLink();
        return new ContactUsPage(driver);
    }

    // Navigate to the Sign In page and return a SignInPage instance
    public SignInPage navigateToSignInPage() {
        clickSignInLink();
        return new SignInPage(driver);
    }
}
