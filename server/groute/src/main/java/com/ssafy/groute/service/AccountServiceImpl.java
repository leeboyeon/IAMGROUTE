package com.ssafy.groute.service;

import com.ssafy.groute.dto.Account;
import com.ssafy.groute.mapper.AccountMapper;
import com.ssafy.groute.mapper.UserPlanMapper;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    UserPlanMapper userPlanMapper;

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

    @Override
    public Map<Integer, Integer> totalCategoryPriceByPlanId(int planId) throws Exception{
        if (userPlanMapper.selectUserPlan(planId) == null) {
            return null;
        }
        Map<Integer, Integer> price = new HashMap<>();
        for(int i=1; i < 9; i++) {
            Integer priceByCategory = accountMapper.totalCategoryPriceByPlanId(planId, i);
            if (priceByCategory != null) {
                price.put(i, priceByCategory);
            } else {
                price.put(i, 0);
            }
        }
        return price;
    }
}
