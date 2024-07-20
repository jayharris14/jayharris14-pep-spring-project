package com.example.controller;

import java.beans.JavaBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */


@Controller
@ComponentScan
public class SocialMediaController {

@Autowired
private AccountService accountService;

@Autowired
private MessageService messageService;





public SocialMediaController(){
  
}

public SocialMediaController(AccountService accountService, MessageService messageService){
    this.messageService=messageService;
    this.accountService=accountService;
}

@GetMapping("messages")
public ResponseEntity getMessages(){
  List<Message> msgs=messageService.getallmessages();
  return ResponseEntity.status(200).body(msgs);
}
@DeleteMapping("messages/{messageid}")
public ResponseEntity deleteMessagebyId(@PathVariable int messageid){
  List<Message> messages=messageService.getallmessages();
  int p=0;
  for (int i=0; i<messages.size(); i++){
    if (messages.get(i).getMessageId().equals(messageid)){
      p=1;
      messageService.deleteMessage(messageid);
      return ResponseEntity.status(200).body(1);
    }
  }
  return ResponseEntity.status(200).body(null);  
}

@PatchMapping("messages/{messageid}")
public ResponseEntity updateMessagebyId(@RequestBody Message message, @PathVariable int messageid){
  List<Message> messages=messageService.getallmessages();
  int p=0;
  for (int i=0; i<messages.size(); i++){
    if (messages.get(i).getMessageId().equals(messageid)){
      p=1;
      if (message.getMessageText().length()<=255 && message.getMessageText()!=""){
      messageService.updateMessage(messages.get(i).getMessageText(), messageid);
      return ResponseEntity.status(200).body(1);
      }
    }
  }
  return ResponseEntity.status(400).body(null);
}

@GetMapping("accounts/{accountId}/messages")
public ResponseEntity getMessagebyPostedBy(@PathVariable int accountId){
  List<Message> msgs=messageService.getallmessages();
  List<Message>imsgs=new ArrayList<>();
  for (int i=0; i<msgs.size(); i++){
    if (msgs.get(i).getPostedBy().equals(accountId)){
      imsgs.add(msgs.get(i));
  }
}
return ResponseEntity.status(200).body(imsgs);

}


@GetMapping("messages/{messageid}")
public ResponseEntity getMessagebyid(@PathVariable int messageid){
  Optional<Message> message=messageService.findById(messageid);
  if (message.equals(Optional.empty())){
    return ResponseEntity.status(200).body(null);
  }
else{
  return ResponseEntity.status(200).body(message);
}
}

@PostMapping("messages")
public ResponseEntity CreateMessage(@RequestBody Message message){
int p=0;
  if (message.getMessageText()!="" && message.getMessageText().length()<=255){
    List<Account> accs=accountService.getallaccounts();
    if (accs!=null){
      for (int i=0; i<accs.size(); i++){
        if (accs.get(i).getAccountId().equals(message.getPostedBy())){
          p=1;
          messageService.addMessage(message);
          List<Message> msgs=messageService.getallmessages();
          for (int j=0; j<msgs.size(); j++){
              if (msgs.get(j).getPostedBy().equals(message.getPostedBy())){
                return ResponseEntity.status(200).body(msgs.get(j));
              }              
          }
        }
      }
      if (p!=1){
        return ResponseEntity.status(400).body(null);
      }
    }
    else{
      return ResponseEntity.status(400).body(null);
    }
  }
  else{
    return ResponseEntity.status(400).body(null);
  }
  return ResponseEntity.status(400).body(null);
}

@PostMapping("login")
public ResponseEntity Login(@RequestBody Account newaccount){
  int p=0;
  if (accountService.getallaccounts()!=null){
    List <Account> accounts=accountService.getallaccounts();
    for (int i=0; i<accounts.size(); i++){
      if (newaccount.getUsername().equals(accounts.get(i).getUsername()) && newaccount.getPassword().equals(accounts.get(i).getPassword())){
        p=1;
        return ResponseEntity.status(200).body(accounts.get(i));
      }
    }
    if (p!=1){
      return ResponseEntity.status(401).body(null);
    }
  }
  return ResponseEntity.status(200).body(null);
}


@PostMapping("register")
public ResponseEntity Register(@RequestBody Account newaccount){
  System.out.println(newaccount); 
  int p=0;
  if (newaccount.getPassword().length()>=4 && newaccount.getUsername()!=null){
    if (accountService.getallaccounts()!=null){
      List <Account> accounts=accountService.getallaccounts();
      System.out.println(accounts); 
      for (int i=0; i<accounts.size(); i++){
        if((accounts.get(i).getUsername()).equals(newaccount.getUsername())){
         p=1;
         System.out.println("right here");
        }
      }
         if (p!=1){
          Account newacc2=new Account(newaccount.getUsername(), newaccount.getPassword());
           accountService.addAccount(newacc2);
           List <Account> iaccounts=accountService.getallaccounts();
           System.out.println(iaccounts); 
           for (int j=0; j<iaccounts.size(); j++){
             if(iaccounts.get(j).getUsername()==newaccount.getUsername()){
              Optional<Account> raccount= accountService.findById(iaccounts.get(j).getAccountId());
              return ResponseEntity.status(200).body(raccount);
             }
         }
       }
         else{
           return ResponseEntity.status(409).body(null);
         }
     }
     else{
      Account newacc=new Account(newaccount.getUsername(), newaccount.getPassword());
      System.out.println(newacc);
      accountService.addAccount(newacc);
           List <Account> iaccounts=accountService.getallaccounts();
           System.out.println(iaccounts); 
           for (int j=0; j<iaccounts.size(); j++){
             if(iaccounts.get(j).getUsername()==newaccount.getUsername()){
              Optional<Account> raccount= accountService.findById(iaccounts.get(j).getAccountId());
              return ResponseEntity.status(200).body(raccount);
             }
         }
    } 
}
else{
  return ResponseEntity.status(400).body(null);
}
return ResponseEntity.status(400).body(null);
}

}








