package com.ssafy.groute.service;

import com.ssafy.groute.dto.AccountCategory;

import java.util.List;

public interface AccountCategoryService {
    void insertAccountCategory(AccountCategory accountCategory) throws Exception;
    AccountCategory selectAccountCategory(int id) throws Exception;
    List<AccountCategory> selectAllAccountCategory() throws Exception;
    void deleteAccountCategory(int id) throws Exception;
    void updateAccountCategory(AccountCategory accountCategory) throws Exception;
}
