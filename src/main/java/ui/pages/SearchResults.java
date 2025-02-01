package com.ui.automation.framework.pages;

import com.ui.automation.framework.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class SearchResultPage extends BasePage {

    // Locators using Page Factory annotations
    @FindBy(xpath = "//div/h1/span[@class='lighter']")
    private WebElement searchResults;

    @FindBy(xpath = "//a[@class='product_img_link']/img")
    private List<WebElement> itemList;

    // Constructor
    public SearchResultPage(WebDriver driver) {
        super(driver);  // Call the constructor of BasePage
        PageFactory.initElements(driver, this);  // Initialize Page Factory elements
    }

    // Get the search results text
    public String getSearchResults() {
        return getElementText(searchResults);
    }

    // Get the count of search items
    public int getSearchItemsListCount() {
        return itemList.size();
    }
}
