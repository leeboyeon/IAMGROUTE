package com.ssafy.groute.service;

import com.ssafy.groute.dto.Account;
import com.ssafy.groute.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountMapper accountMapper;

    @Override
    public void insertAccount(Account account) throws Exception {
        accountMapper.insertAccount(account);
    }

    @Override
    public Account selectAccount(int id) throws Exception {
        return accountMapper.selectAccount(id);
    }

    @Override
    public List<Account> selectAllAccount() throws Exception {
        return accountMapper.selectAllAccount();
    }

    @Override
    public void deleteAccount(int id) throws Exception {
        accountMapper.deleteAccount(id);
    }

    @Override
    public void updateAccount(Account account) throws Exception {
        accountMapper.updateAccount(account);
    }

    @Override
    public List<Account> selectByUserPlanId(int userPlanId) throws Exception {
        return accountMapper.selectByUserPlanId(userPlanId);
    }
}
