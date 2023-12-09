package com.example.usermanagementms.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = Token.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
public class Token {
    final static String TABLE_NAME = "tokens";
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    @Column(unique = true)
    String token;
    @Enumerated(EnumType.STRING)
    TokenType tokenType;
    boolean revoked;
    boolean expired;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    User user;
}