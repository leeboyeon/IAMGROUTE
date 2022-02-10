package com.ssafy.groute.service;

import com.ssafy.groute.dto.AccountCategory;
import com.ssafy.groute.mapper.AccountCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountCategoryServiceImpl implements AccountCategoryService {
    @Autowired
    AccountCategoryMapper accountCategoryMapper;

    @Override
    public void insertAccountCategory(AccountCategory accountCategory) throws Exception {
        accountCategoryMapper.insertAccountCategory(accountCategory);
    }

    @Override
    public AccountCategory selectAccountCategory(int id) throws Exception {
        return accountCategoryMapper.selectAccountCategory(id);
    }

    @Override
    public List<AccountCategory> selectAllAccountCategory() throws Exception {
        return accountCategoryMapper.selectAllAccountCategory();
    }

    @Override
    public void deleteAccountCategory(int id) throws Exception {
        accountCategoryMapper.deleteAccountCategory(id);
    }

    @Override
    public void updateAccountCategory(AccountCategory accountCategory) throws Exception {
        accountCategoryMapper.updateAccountCategory(accountCategory);
    }
}
