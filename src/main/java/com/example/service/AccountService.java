package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
@Transactional
@ComponentScan
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    public AccountService(){
      
    }

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository=accountRepository;
    }

    




    public void addAccount(Account account){
        
        accountRepository.save(account);
    }

    
    public List<Account> getallaccounts(){
        try {
            List<Account> accs=accountRepository.findAll();
            return accs;
        } catch (NullPointerException e) {
           return null;
        }
    }
        
    public Optional<Account> findById(int id){
        return accountRepository.findById(id);
    }
}
