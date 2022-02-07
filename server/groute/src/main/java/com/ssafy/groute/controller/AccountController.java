package com.ssafy.groute.controller;

import com.ssafy.groute.dto.Account;
import com.ssafy.groute.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
@RequestMapping("/account")
public class AccountController {


    @Autowired
    AccountService accountService;

    @ApiOperation(value = "account 추가",notes = "account 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertAccount(@RequestBody Account req){

        try {
            accountService.insertAccount(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "account 검색",notes = "이름으로 account 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailAccount(@RequestParam("id") int id) throws Exception{

        Account res = accountService.selectAccount(id);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Account>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list account",notes = "모든 account 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listAccount() throws Exception{

        List<Account> res = accountService.selectAllAccount();
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Account>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete account",notes = "account 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deleteAccount(@RequestParam("id") int id) throws Exception{

        try {
            accountService.deleteAccount(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "updateAccount",notes = "account 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateAccount(@RequestBody Account account) throws Exception{

        try {
            accountService.updateAccount(account);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
}
