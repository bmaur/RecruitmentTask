package com.one2tribe.recruitment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.one2tribe.recruitment.model.MessageInput;
import com.one2tribe.recruitment.repository.MessageEntity;
import com.one2tribe.recruitment.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MessageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageRepository messageRepository;

    @Test
    void isCreatingMessageCorrectly() throws Exception {
        String messageContent = "test content";
        MessageInput messageInput = new MessageInput(messageContent);
        mockMvc.perform(post("/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageInput)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(messageContent))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    void isGettingAllMessagesCorrectly() throws Exception {
        String content = "Random content";
        MessageEntity messageEntity = new MessageEntity();
        MessageEntity messageEntity1 = new MessageEntity();
        MessageEntity messageEntity2 = new MessageEntity();
        messageEntity.setContent("some test content");
        messageEntity1.setContent(content);
        messageEntity2.setContent(content);
        messageRepository.save(messageEntity);
        messageRepository.save(messageEntity1);
        messageRepository.save(messageEntity2);
        mockMvc.perform(get("/message")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)));
    }

    @Test
    void isUpdateMessageCorrectly() throws Exception {
        String testContent = "Random Content";
        String changedContent = "Test content";
        MessageInput messageInput = new MessageInput(changedContent);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(testContent);
        MessageEntity savedEntity = messageRepository.save(messageEntity);
        UUID uuid = savedEntity.getId();

        mockMvc.perform(put("/message/{uuid}", uuid.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageInput)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(changedContent));
    }

    @Test
    void isFindRandomMessagesCorrectly() throws Exception {
        String content = "Random content";
        MessageEntity messageEntity = new MessageEntity();
        MessageEntity messageEntity1 = new MessageEntity();
        MessageEntity messageEntity2 = new MessageEntity();
        messageEntity.setContent("some test content");
        messageEntity1.setContent(content);
        messageEntity2.setContent(content);
        messageRepository.save(messageEntity);
        messageRepository.save(messageEntity1);
        messageRepository.save(messageEntity2);
        mockMvc.perform(get("/message/random/{messageNumber}", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }


}




























