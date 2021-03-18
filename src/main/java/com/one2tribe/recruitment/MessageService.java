package com.one2tribe.recruitment;

import com.one2tribe.recruitment.exception.IncorrectMessageInputException;
import com.one2tribe.recruitment.exception.MessageNotFoundException;
import com.one2tribe.recruitment.model.Message;
import com.one2tribe.recruitment.model.MessageInput;
import com.one2tribe.recruitment.repository.MessageEntity;
import com.one2tribe.recruitment.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public Message createMessage(MessageInput messageInput) {
        validateMessageInput(messageInput);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(messageInput.getContent());
        MessageEntity savedMessage = messageRepository.save(messageEntity);
        return Message.builder()
                .id(savedMessage.getId())
                .content(savedMessage.getContent())
                .build();
    }

    List<Message> getAllMessages() {
        return messageRepository.findAll()
                .stream()
                .map(e -> Message.builder()
                        .id(e.getId())
                        .content(e.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public Message updateMessage(UUID uuid, MessageInput messageInput) {
        validateMessageInput(messageInput);

        MessageEntity messageEntity = messageRepository.findById(uuid)
                .orElseThrow(MessageNotFoundException::new);
        messageEntity.setContent(messageInput.getContent());

        return Message.builder()
                .id(messageEntity.getId())
                .content(messageEntity.getContent())
                .build();
    }

    List<Message> findRandomMessages(int messageNumber) {
        return messageRepository.findRandomMessages(messageNumber)
                .stream()
                .map(e -> Message.builder()
                        .id(e.getId())
                        .content(e.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    void validateMessageInput(MessageInput messageInput) {
        if (messageInput.getContent().isBlank()) {
            throw new IncorrectMessageInputException();
        }
    }

}
