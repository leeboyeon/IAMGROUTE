package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.AccountCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountCategoryMapper {
    void insertAccountCategory(AccountCategory accountCategory) throws Exception;
    AccountCategory selectAccountCategory(int id) throws Exception;
    List<AccountCategory> selectAllAccountCategory() throws Exception;
    void deleteAccountCategory(int id) throws Exception;
    void updateAccountCategory(AccountCategory accountCategory) throws Exception;
}
