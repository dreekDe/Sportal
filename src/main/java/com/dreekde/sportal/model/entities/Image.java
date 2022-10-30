package com.dreekde.sportal.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Desislava Tencheva
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "images", indexes = @Index(columnList = "imageurl"))
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String imageURL;
    @ManyToOne()
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Article article;
}
