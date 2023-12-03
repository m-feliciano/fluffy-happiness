package br.com.microservices;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification")
@Entity
public class Notification {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_id_sequence")
    @SequenceGenerator(
            name = "notification_id_sequence",
            sequenceName = "notification_id_sequence")
    private Long id;

    @Column(name = "to_costumer_id")
    private Integer toCustomerId;

    @Column(name = "to_customer_email")
    private String toCustomerEmail;

    @Column(name = "from_sender_id")
    private Integer fromSenderId;

    @Column(name = "from_sender_email")
    private String fromSenderEmail;

    @Column(name = "message")
    private String message;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;
}
