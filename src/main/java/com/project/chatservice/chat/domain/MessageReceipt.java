package com.project.chatservice.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_receipts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReceipt {

    // Task 1: bảng lưu trạng thái đọc/đã nhận theo từng user.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    public MessageReceipt(Message message, String userId, String status) {
        this.message = message;
        this.userId = userId;
        this.status = status;
    }

    public void markStatus(String status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }
}
