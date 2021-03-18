package com.one2tribe.recruitment;

import com.one2tribe.recruitment.exception.IncorrectMessageInputException;
import com.one2tribe.recruitment.exception.MessageNotFoundException;
import com.one2tribe.recruitment.model.Message;
import com.one2tribe.recruitment.model.MessageInput;
import com.one2tribe.recruitment.repository.MessageEntity;
import com.one2tribe.recruitment.repository.MessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MessageUnitTest {

    private MessageService sut;
    @Mock
    private MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
        sut = new MessageService(messageRepository);
    }

    @Captor
    private ArgumentCaptor<MessageEntity> messageEntityArgumentCaptor;

    @Test
    void shouldAddNewMessageCorrectly() {
        //given
        String testContent = "Random Content";
        UUID uuid = UUID.randomUUID();
        MessageInput messageInput = new MessageInput(testContent);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(testContent);
        messageEntity.setId(uuid);

        when(messageRepository.save(any())).thenReturn(messageEntity);
        //when
        final Message result = sut.createMessage(messageInput);

        verify(messageRepository, times(1)).save(messageEntityArgumentCaptor.capture());
        MessageEntity capturedMessageEntity = messageEntityArgumentCaptor.getValue();
        //then
        assertThat(capturedMessageEntity.getContent()).isEqualTo((result).getContent());

    }

    @Test
    void shouldThrowsIncorrectMessageInputExceptionWhenAddNewMessage() {
        //given
        String testContent = "";
        MessageInput messageInput = new MessageInput(testContent);
        final String errorMessage = "Message content can't be blank!";

        //when
        final Throwable result = Assertions.assertThrows(IncorrectMessageInputException.class,
                () -> sut.createMessage(messageInput));

        verify(messageRepository, times(0)).save(any());
        //then
        assertThat(result.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    void shouldGetAllMessagesCorrectly() {
        //given
        String testContent = "Random Content";
        UUID uuid = UUID.randomUUID();
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(uuid);
        messageEntity.setContent(testContent);

        List<MessageEntity> messageEntityList = new ArrayList<>();
        messageEntityList.add(messageEntity);

        when(messageRepository.findAll()).thenReturn(messageEntityList);

        //when
        List<Message> result = sut.getAllMessages();

        //then
        assertThat(messageEntityList.get(0).getContent()).isEqualTo(result.get(0).getContent());
        assertThat(messageEntityList.get(0).getId()).isEqualTo(result.get(0).getId());
    }

    @Test
    void shouldUpdateMessageCorrectly() {
        //given
        String testContent = "Random Content";
        String changedTestContent = "Random Content 2";
        UUID uuid = UUID.randomUUID();
        MessageInput messageInput = new MessageInput(changedTestContent);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(testContent);
        messageEntity.setId(uuid);

        when(messageRepository.findById(uuid)).thenReturn(java.util.Optional.of(messageEntity));
        //when
        sut.updateMessage(uuid, messageInput);
        verify(messageRepository, times(1)).findById(uuid);
        //then
        assertThat(messageEntity.getContent()).isEqualTo(changedTestContent);
    }

    @Test
    void shouldThrowsIncorrectMessageInputExceptionWhenUpdateMessage() {
        //given
        String testContent = "";
        UUID uuid = UUID.randomUUID();
        MessageInput messageInput = new MessageInput(testContent);
        final String errorMessage = "Message content can't be blank!";

        //when
        final Throwable result = Assertions.assertThrows(IncorrectMessageInputException.class,
                () -> sut.updateMessage(uuid, messageInput));

        verify(messageRepository, times(0)).save(any());
        //then
        assertThat(result.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    void shouldThrowsMessageNotFoundExceptionWhenUpdateMessage() {
        //given
        String testContent = "Random Content";
        UUID uuid = UUID.randomUUID();
        MessageInput messageInput = new MessageInput(testContent);
        final String errorMessage = "Message not found!";

        //when
        final Throwable result = Assertions.assertThrows(MessageNotFoundException.class,
                () -> sut.updateMessage(uuid, messageInput));
        //then
        assertThat(result.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    void shouldGetRandomMessagesCorrectly() {
        //given
        String testContent = "Random Content";
        int messageNumber = 1;
        UUID uuid = UUID.randomUUID();
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(uuid);
        messageEntity.setContent(testContent);

        List<MessageEntity> messageEntityList = new ArrayList<>();
        messageEntityList.add(messageEntity);

        when(messageRepository.findRandomMessages(messageNumber)).thenReturn(messageEntityList);

        //when
        List<Message> result = sut.findRandomMessages(messageNumber);

        //then
        assertThat(messageEntityList.get(0).getContent()).isEqualTo(result.get(0).getContent());
        assertThat(messageEntityList.get(0).getId()).isEqualTo(result.get(0).getId());
    }

}
