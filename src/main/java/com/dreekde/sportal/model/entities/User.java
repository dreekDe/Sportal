package com.dreekde.sportal.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Desislava Tencheva
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @Column(nullable = false)
    private boolean isActive = true;
    @Column(nullable = false)
    private boolean isAdmin;
    @OneToMany(mappedBy = "author")
    private List<Article> articles;
    @OneToMany(mappedBy = "owner")
    private List<Comment> comments;
    @ManyToMany(mappedBy = "likers")
    private List<Comment> likes;
    @ManyToMany(mappedBy = "dislikers")
    private List<Comment> dislikes;
}

