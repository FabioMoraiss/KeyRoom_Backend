package com.br.keyroom.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "secrets")
@Entity(name = "secrets")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Secret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String title;
    private String login;
    private String password;
    private String url;
    private String notes;
    private String tag;
    private String OTPCode;

    public Secret(User user,String title, String login, String password, String url, String notes, String OTPCode) {
        this.user = user;
        this.title = title;
        this.login = login;
        this.password = password;
        this.url = url;
        this.notes = notes;
        this.OTPCode = OTPCode;
    }
}
