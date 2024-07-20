package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
@Transactional
@ComponentScan
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public MessageService(){
      
    }

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository=messageRepository;
    }

    public void addMessage(Message message){
        messageRepository.save(message);
    }

    
    
    public List<Message> getallmessages(){
        try {
            List<Message> msgs=messageRepository.findAll();
            return msgs;
        } catch (NullPointerException e) {
           return null;
        }
    }
        
    public Optional<Message> findById(int id){
        return messageRepository.findById(id);
    }

    public void deleteMessage(int id){
        messageRepository.deleteById(id);
    }

    public void updateMessage(String text, int id){
        messageRepository.updateMessage(text, id);
    }
    

}
