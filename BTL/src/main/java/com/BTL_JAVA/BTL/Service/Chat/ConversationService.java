package com.BTL_JAVA.BTL.Service.Chat;

import com.BTL_JAVA.BTL.Entity.Chat.Conversation;
import com.BTL_JAVA.BTL.Repository.Chat.ConversationRepository;
import com.BTL_JAVA.BTL.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ConversationRepository conversationRepository;
    UserRepository userRepository;

    public static final int DEFAULT_ADMIN_ID = 1;

    @Transactional
    public Conversation addConversation(int userId) {
        return conversationRepository.findByUser_IdAndAdmin_Id(userId,DEFAULT_ADMIN_ID).
                orElseGet(() ->{
                    Conversation conversation = Conversation.builder()
                            .user(userRepository.findById(userId).orElseThrow())
                            .admin(userRepository.findById(DEFAULT_ADMIN_ID).orElseThrow())
                                    .build();
                    return conversationRepository.save(conversation);
                }
        );
    }
}
