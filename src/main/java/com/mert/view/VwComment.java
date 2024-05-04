package com.mert.view;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //
@AllArgsConstructor //
@NoArgsConstructor // parametreli constructor ların tümü
@Builder //default constructor
@Entity
//@Table sildik gerek yok.
public class VwComment {
    @Id
    Long id;
    Long userid;
    String comment;
    //DB'de gözükmeyecek.
}
