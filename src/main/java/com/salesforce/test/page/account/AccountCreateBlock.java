package com.salesforce.test.page.account;

public class AccountCreateBlock extends BaseAccountCreateEditBlock {

    @Override
    public String getUrl() {
        return BASE_URL + "/lightning/o/Account/new?count=1";
    }

}
