package com.bank.BankManagement.Controller;

import com.bank.BankManagement.dto.Account;
import com.bank.BankManagement.dto.AuthRequest;
import com.bank.BankManagement.entity.UserInfo;
import com.bank.BankManagement.service.AccountService;
import com.bank.BankManagement.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
public class AccountController {

    @Autowired
    private AccountService service;

    @Autowired
    private JwtService jwtService;


    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/welcome")
    public String welcome() {
        return "welcome this endpoint is not secure ";
    }

    // Check if the url endpoints are working properly

    @PostMapping("/newAccount")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    // Create New Users to be added in DB --> Signup
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public List<Account> getAllTheAccount() {
        return service.getAccountList();
    }

    // Retrieve the data from the DB --> Data retrieval Only Manager --> can access ALL
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public Account getAccountById(@PathVariable int id) {
        return service.getAccount(id);

    }

    // Retrieve the data from the DB --> Data retrieval Only Customers

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }

        // Authenticate the users with JWT
    }
}


