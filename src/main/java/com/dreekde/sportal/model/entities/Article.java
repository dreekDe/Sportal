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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    @Column(nullable = false)
    private int views;
    @Column(nullable = false)
    private LocalDateTime postDate;
    @OneToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User author;
    private boolean isAvailable = true;
    @OneToMany(mappedBy = "article")
    private List<Comment> comments;
    @OneToMany(mappedBy = "article")
    private List<Image> images;
}
