package com.salesforce.test.service;

import com.salesforce.test.page.login.LoginPage;
import com.salesforce.test.page.login.RegisterPhonePage;
import com.salesforce.test.page.login.VerifyIdentityPage;
import com.salesforce.test.util.EmailContentProvider;
import com.salesforce.test.core.driver.TestDriverManager;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class LoginService {

    private final LoginPage loginPage = new LoginPage();
    private final VerifyIdentityPage verifyIdentityPage = new VerifyIdentityPage();
    private final RegisterPhonePage registerPhonePage = new RegisterPhonePage();

    public void loginSalesforce(String username, String password) {
        log.info("Attempt to login as: {}", username);
        var searchFromTime = Instant.now();
        loginPage.openLoginPage()
                .enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
        var currentUrl = TestDriverManager.getTestDriver().getUrl();
        if (currentUrl.contains(verifyIdentityPage.getUrl())) {
            log.info("Attempt to enter verification code");
            var code = EmailContentProvider.getVerificationCode(searchFromTime);
            verifyIdentityPage.enterCode(code)
                    .clickVerifyButton();
        } else if (currentUrl.contains(registerPhonePage.getUrl())) {
            log.info("Attempt to skip phone registration");
            registerPhonePage.clickDontRegisterPhoneButton();
        } else {
            log.info("No verification pages were opened");
        }
    }

}
