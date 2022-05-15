package com.gabriel.tfg.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabriel.tfg.entity.constants.MEDIA_TYPE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Entity
public class Post extends GenericEntity<Post> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 1024)
    private String title;

    @Column(name = "body", length = 1024)
    private String body;

    @Column(name = "source", length = 1024)
    private String source;

    @Column(name = "mediaUrl", length = 1024)
    private String mediaUrl;

    private MEDIA_TYPE type;

    private LocalDateTime postedDate;

    private int likes;

    @JsonIgnore
    @ManyToOne
    private FeedNode feedNode;

}
