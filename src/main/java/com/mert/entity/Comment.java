package com.mert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //
@AllArgsConstructor //
@NoArgsConstructor // parametreli constructor ların tümü
@Builder //default constructor
@Entity
@Table(name = "tblcomment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Id için otomatik artan HB sequence oluşturur
    Long id;
    Long postid;
    Long userid;
    @Column(length = 500)
    String comment;
    Long date;
}
