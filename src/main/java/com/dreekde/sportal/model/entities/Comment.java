package com.dreekde.sportal.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Desislava Tencheva
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    @Column(nullable = false)
    private LocalDateTime postDate;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Article article;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User owner;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Comment parent;
    @ManyToMany
    @JoinTable(
            name = "users_like_comments",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> likers;
    @ManyToMany
    @JoinTable(
            name = "users_dislike_comments",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> dislikers;
}
