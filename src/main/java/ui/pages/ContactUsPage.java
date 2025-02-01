package ui.pages;

import ui.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ContactUsPage extends BasePage {

    // Locators using Page Factory annotations
    @FindBy(id = "id_contact")
    private WebElement subjectHeadingDropdown;

    @FindBy(id = "email")
    private WebElement emailAddressField;

    @FindBy(id = "id_order")
    private WebElement orderReferenceField;

    @FindBy(id = "fileUpload")
    private WebElement attachFileField;

    @FindBy(id = "message")
    private WebElement messageField;

    @FindBy(id = "submitMessage")
    private WebElement sendButton;

    @FindBy(xpath = "//div[@class='alert alert-danger']")
    private WebElement failureMessage;

    @FindBy(xpath = "//p[@class='alert alert-success']")
    private WebElement successMessage;

    // Constructor
    public ContactUsPage(WebDriver driver) {
        super(driver);  // Call the constructor of BasePage
        PageFactory.initElements(driver, this);  // Initialize Page Factory elements
    }

    // Choose subject heading from the dropdown
    private void chooseSubjectHeading(String selectVal) {
    	selectFromDropDown(subjectHeadingDropdown, selectVal,"val");
    }

    // Type email address in the email field
    private void typeEmailAddress(String emailAddress) {
        sendKeys(emailAddressField, emailAddress);
    }

    // Type order reference in the order reference field
    private void typeOrderReference(String orderRef) {
        sendKeys(orderReferenceField, orderRef);
    }

    // Attach a file to the file upload field
    private void attachFile(String filePath) {
        uploadFile(filePath, attachFileField);
    }

    // Type message in the message field
    private void typeMessage(String message) {
        sendKeys(messageField, message);
    }

    // Click the send button
    private void clickSendButton() {
        click(sendButton);
    }

    // Send a message with provided details
    public ContactUsPage sendMessage(String subHeading, String emailAddress, String orderRef, String filePath, String message) {
        if (subHeading != null && emailAddress != null && message != null) {
            chooseSubjectHeading(subHeading);
            typeEmailAddress(emailAddress);
            typeOrderReference(orderRef);
            attachFile(filePath);
            typeMessage(message);
            clickSendButton();
        } else {
            clickSendButton();
        }
        return new ContactUsPage(driver);
    }

    // Get the success message text
    public String getSuccessMessage() {
        return getElementText(successMessage);
    }

    // Get the failure message text
    public String getFailureMessage() {
        return getElementText(failureMessage);
    }
}
