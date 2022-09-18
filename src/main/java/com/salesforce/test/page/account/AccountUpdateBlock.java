package com.salesforce.test.page.account;

import java.util.Objects;

public class AccountUpdateBlock extends BaseAccountCreateEditBlock {

    private final String slug;

    public AccountUpdateBlock(String slug) {
        super();
        this.slug = slug;
    }

    @Override
    public String getUrl() {
        Objects.requireNonNull(slug, "Provide slug before getting page url");
        return BASE_URL.concat("/lightning/r/Account/").concat(slug).concat("/edit?count=1");
    }

}
