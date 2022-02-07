package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.Account;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {
    void insertAccount(Account account) throws Exception;
    Account selectAccount(int id) throws Exception;
    List<Account> selectAllAccount() throws Exception;
    void deleteAccount(int id) throws Exception;
    void updateAccount(Account account) throws Exception;
    void deleteAllAccountByRId(int routesId) throws Exception;
    List<Account> selectByUserPlanId(int userPlanId) throws Exception;
    void deleteByUserPlanId(int userPlanId) throws Exception;
}
