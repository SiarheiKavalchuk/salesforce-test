package com.salesforce.test.service;

import com.salesforce.test.core.driver.TestDriverManager;
import com.salesforce.test.model.AccountDto;
import com.salesforce.test.page.account.AccountCreateBlock;
import com.salesforce.test.page.account.AccountUpdateBlock;
import com.salesforce.test.page.account.AccountViewPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.awaitility.Awaitility.await;

@Slf4j
public class AccountService {

    private static final Pattern EXTRACT_ACCOUNT_SLUG_PATTERN = Pattern.compile("(?<=Account/).+?(?=/)");

    public String createAccount(AccountDto accountDto) {
        log.info("Attempt to create account");
        var accountCreateBlock = new AccountCreateBlock();
        accountCreateBlock.openPage();
        accountCreateBlock.enterAccountName(accountDto.getName())
                .enterPhone(accountDto.getPhone())
                .enterWebsite(accountDto.getWebsite())
                .clickSaveButton();

        waitForAccountViewPage(true);
        var slug = extractAccountSlugFromUrl(TestDriverManager.getTestDriver().getUrl());
        log.info("Account created: " + slug);
        return slug;
    }

    public void updateAccount(String slug, AccountDto accountDto) {
        log.info("Attempt to update account: " + slug);
        var accountUpdateBlock = new AccountUpdateBlock(slug);
        accountUpdateBlock.openPage();
        accountUpdateBlock.enterAccountName(accountDto.getName())
                .enterPhone(accountDto.getPhone())
                .enterWebsite(accountDto.getWebsite())
                .clickSaveButton();

        log.info("Account updated: " + slug);
    }

    public void deleteAccount(String slug){
        log.info("Attempt to delete account: " + slug);
        var accountViewPage = new AccountViewPage(slug);
        if (!isViewPageOpened(slug)) {
            accountViewPage.openPage();
        }
        accountViewPage.openMainBlockDdl()
                .clickDeleteDdlOption()
                .clickDeleteButton();

        waitForAccountViewPage(false);
        log.info("Account deleted: " + slug);
    }

    public AccountDto getAccountInfoFromViewAccountPage(String slug){
        var accountViewPage = new AccountViewPage(slug);
        accountViewPage.openPage();
        return AccountDto.builder()
                .name(accountViewPage.getMainAccountNameText())
                .phone(accountViewPage.getMainPhoneText().replaceAll("\\D", StringUtils.EMPTY))
                .website(accountViewPage.getMainWebsiteText())
                .build();
    }

    private void waitForAccountViewPage(boolean isOpened) {
        await("Account creation / deletion")
                .pollInSameThread()
                .atMost(Duration.ofSeconds(15))
                .pollInterval(1, TimeUnit.SECONDS)
                .until(this::isViewPageOpened, Predicate.isEqual(isOpened));
    }

    private boolean isViewPageOpened() {
        return TestDriverManager.getTestDriver().getUrl().contains("view");
    }

    private boolean isViewPageOpened(String slug) {
        var url = TestDriverManager.getTestDriver().getUrl();
        return url.contains("view") && url.contains(slug);
    }

    private String extractAccountSlugFromUrl(String url) {
        var matcher = EXTRACT_ACCOUNT_SLUG_PATTERN.matcher(url);
        return matcher.find() ? matcher.group(0) : null;
    }

}
