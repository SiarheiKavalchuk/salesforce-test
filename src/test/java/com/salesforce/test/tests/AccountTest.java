package com.salesforce.test.tests;

import com.salesforce.test.core.CustomTestListener;
import com.salesforce.test.core.driver.TestDriverManager;
import com.salesforce.test.model.AccountDto;
import com.salesforce.test.service.AccountService;
import com.salesforce.test.service.LoginService;
import com.salesforce.test.util.EmailContentProvider;
import com.salesforce.test.util.PropertiesProvider;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.salesforce.test.core.constant.AppConstants.APP_PROPERTY_APP_PASSWORD;
import static com.salesforce.test.core.constant.AppConstants.APP_PROPERTY_APP_USERNAME;

@Listeners(CustomTestListener.class)
public class AccountTest {

    private final AccountService accountService = new AccountService();

    private String slug;

    @BeforeClass
    public void loginToSalesforce() {
        var username = PropertiesProvider.getAppPropertyValue(APP_PROPERTY_APP_USERNAME);
        var password = PropertiesProvider.getAppPropertyValue(APP_PROPERTY_APP_PASSWORD);
        new LoginService().loginSalesforce(username, password);
    }

    @Test
    public void checkCreateAccount() {
        var testAccount = generateRandomTestAccount();
        slug = accountService.createAccount(testAccount);
        var actualResult = accountService.getAccountInfoFromViewAccountPage(slug);
        Assert.assertEquals(actualResult, testAccount);
    }

    @Test(dependsOnMethods = {"checkCreateAccount"})
    public void checkUpdateAccount() {
        var testAccount = generateRandomTestAccount();
        accountService.updateAccount(slug, testAccount);

        var actualResult = accountService.getAccountInfoFromViewAccountPage(slug);
        Assert.assertEquals(actualResult, testAccount);
    }

    @AfterClass(alwaysRun = true)
    public void deleteAccountAndQuitTestDriver() {
        if (slug != null) {
            accountService.deleteAccount(slug);
        }
        TestDriverManager.quitTestDriver();
    }

    @AfterTest(alwaysRun = true)
    public void deleteEmails() {
        EmailContentProvider.cleanInboxFolderAndCloseConnection();
    }

    private AccountDto generateRandomTestAccount() {
        return AccountDto.builder()
                .name(RandomStringUtils.randomAlphanumeric(10))
                .phone(RandomStringUtils.randomNumeric(10))
                .website(RandomStringUtils.randomAlphabetic(10))
                .build();
    }

}
