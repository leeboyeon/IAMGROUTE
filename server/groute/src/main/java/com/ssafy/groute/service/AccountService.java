package com.ssafy.groute.service;

import com.ssafy.groute.dto.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    void insertAccount(Account account) throws Exception;
    Account selectAccount(int id) throws Exception;
    List<Account> selectAllAccount() throws Exception;
    void deleteAccount(int id) throws Exception;
    void updateAccount(Account account) throws Exception;
    List<Account> selectByUserPlanId(int userPlanId) throws Exception;
}
