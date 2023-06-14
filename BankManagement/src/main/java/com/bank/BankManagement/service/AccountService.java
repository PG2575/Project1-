package com.bank.BankManagement.service;

import com.bank.BankManagement.Repository.UserInfoRepository;
import com.bank.BankManagement.dto.Account;
import com.bank.BankManagement.entity.UserInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AccountService {

    List<Account> accountList = null;

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void loadProductsFromDB() {
        accountList = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> Account.builder()
                        .accountId(i)
                        .name("account " + i)
                        .balance(new Random().nextInt(500000)).build()
                ).collect(Collectors.toList());
    }


    public List<Account> getAccountList() {
        return accountList;
    }

    public Account getAccount(int id) {
        return accountList.stream()
                .filter(Account -> Account.getAccountId() == id)
                .findAny()
                .orElseThrow(() -> new RuntimeException("account " + id + " not found"));
    }


    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "Congrats!! User is added to system ";
    }
}
