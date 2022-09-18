package com.salesforce.test.page.account;

import com.salesforce.test.page.BasePage;
import com.salesforce.test.page.custom.PageOpener;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.Objects;

import static com.salesforce.test.core.driver.TestDriverManager.getTestDriver;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class AccountViewPage extends BasePage implements PageOpener {

    private final String mainBlockSelector = "return document.querySelector('#brandBand_2 one-record-home-flexipage2')" +
            ".shadowRoot.querySelector('forcegenerated-adg-rollup_component___force-generated__flexipage_-record-page___-account_-record_-page___-account___-v-i-e-w')" +
            ".shadowRoot.querySelector('forcegenerated-flexipage_account_record_page_account__view_js')" +
            ".shadowRoot.querySelector('record_flexipage-record-page-decorator')" +
            ".shadowRoot.querySelector('slot[record_flexipage-recordpagedecorator_recordpagedecorator]').assignedNodes(true)[0]" +
            ".shadowRoot.querySelector('div.region-header slot').assignedNodes(true)[0]" +
            ".shadowRoot.querySelector('slot').assignedNodes(true)[0]" +
            ".shadowRoot.querySelector('records-lwc-record-layout')" +
            ".shadowRoot.querySelector('forcegenerated-highlightspanel_account___012000000000000aaa___compact___view___recordlayout2')" +
            ".shadowRoot.querySelector('records-highlights2')";

    private final String accountNamePath =
            ".shadowRoot.querySelector('h1 > slot').assignedNodes(true)[0]" +
            ".shadowRoot.querySelector('sfa-output-name-with-hierarchy-icon-wrapper')" +
            ".shadowRoot.querySelector('force-aura-action-wrapper')" +
            ".shadowRoot.querySelector('lightning-formatted-text')";

    private final String mainBlockDdl = ".shadowRoot.querySelector('div.actionsContainer runtime_platform_actions-actions-ribbon')" +
            ".shadowRoot.querySelector('li.slds-dropdown-trigger lightning-button-menu')";

    private final String mainBlockDdlDeleteButton = ".shadowRoot.querySelector('div[role=\"menu\"] > slot').assignedNodes(true)[2]" +
                        ".shadowRoot.querySelector('slot').assignedNodes(true)[0]";

    private final String mainDetailBlockPhonePath = ".shadowRoot.querySelector('div.secondaryFields > slot').assignedNodes(true)" +
            "[1].shadowRoot.querySelector('div slot').assignedNodes(true)[0]" +
            ".shadowRoot.querySelector('a')";

    private final String mainDetailBlockWebsitePath = ".shadowRoot.querySelector('div.secondaryFields > slot').assignedNodes(true)" +
            "[2].shadowRoot.querySelector('div slot').assignedNodes(true)[0]" +
            ".shadowRoot.querySelector('a')";

    private final String slug;

    public AccountViewPage(String slug) {
        super();
        this.slug = slug;
    }

    public String getMainAccountNameText() {
        return getTestDriver().getText(getTestDriver().getElementWithJs(mainBlockSelector + accountNamePath));
    }

    public String getMainPhoneText() {
        return getTestDriver().getText(getTestDriver().getElementWithJs(mainBlockSelector + mainDetailBlockPhonePath));
    }

    public String getMainWebsiteText() {
        return getTestDriver().getText(getTestDriver().getElementWithJs(mainBlockSelector + mainDetailBlockWebsitePath));
    }

    public AccountViewPage openMainBlockDdl() {
        getTestDriver().clickButton(getTestDriver().getElementWithJs(mainBlockSelector + mainBlockDdl));
        return this;
    }

    public DeleteAccountBlock clickDeleteDdlOption() {
        getTestDriver().clickButton(getTestDriver().getElementWithJs(mainBlockSelector + mainBlockDdl + mainBlockDdlDeleteButton));
        return new DeleteAccountBlock();
    }

    @Override
    public String getUrl() {
        Objects.requireNonNull(slug, "Provide slug before getting page url");
        return BASE_URL.concat("/lightning/r/Account/").concat(slug).concat("/view");
    }

    @Override
    public ExpectedCondition<?> waitForCondition() {
        return javaScriptThrowsNoExceptions(mainBlockSelector + accountNamePath + ".checkVisibility()");
    }

}
