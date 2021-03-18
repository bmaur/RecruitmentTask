package com.one2tribe.recruitment;

import com.one2tribe.recruitment.model.Message;
import com.one2tribe.recruitment.model.MessageInput;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/message/random/{messageNumber}")
    List<Message> randomMessages(@PathVariable("messageNumber") int messageNumber) {
        return messageService.findRandomMessages(messageNumber);
    }

    @GetMapping("/message")
    List<Message> getMessage() {
        return messageService.getAllMessages();
    }

    @PostMapping("/message")
    Message addMessage(@RequestBody MessageInput messageInput) {
        return messageService.createMessage(messageInput);
    }

    @PutMapping("/message/{uuid}")
    Message changeContent(@PathVariable UUID uuid, @RequestBody MessageInput messageInput) {
        return messageService.updateMessage(uuid, messageInput);
    }
}
