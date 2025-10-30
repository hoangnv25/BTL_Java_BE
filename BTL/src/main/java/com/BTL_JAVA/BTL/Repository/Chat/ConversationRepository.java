package com.BTL_JAVA.BTL.Repository.Chat;

import com.BTL_JAVA.BTL.Entity.Chat.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    Optional<Conversation> findByUser_IdAndAdmin_Id(int user_id, int admin_id);
}
