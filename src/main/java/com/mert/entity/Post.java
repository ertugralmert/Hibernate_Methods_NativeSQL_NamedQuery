package com.mert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Named Query
 * Bir sorgu yazıldığın bu sorgu sunucuya gider veritabanından çeker. Bu işlem Harddiskten yapılır.
 * bu işlem hantal bir işlemdir.
 * Bu işlemi kısaltabilmek iiçin harddiskten değil de ram üzerinden yapılır.
 * Named Query ile yapıldığında ilk başta sorugu çeker, tekrar sorguda önbellekten veriler çekilir.
 * Sıklıkla kullanılır. Entity üzerine yazılır. Hibernate üzerinden criteriaQuery ile çekilirler.
 *
 * Name query yazmak için 3 farklı dil kullanılır.
 * HQL -> hibernate'in kendi dili -> Hibernate Query Language
 * JPQL -> Jakarta Persistence Query Language
 * Native SQL
 *   Sorgu Yazma Şekilleri
 *   NativeSQL -> select * from tblpost
 *   JPQL -> select p from Post p (Post p -> alians kullanılır. Yani Post kısaca p ile gösterilir.)
 *   HQL -> from Post
 *   --------------------
 *   Named Query ilgili sınıfın üzerine bir anotasyon yardımı ile yazılırlar.
 *   Eğer tek bir Query kullanılacak ise tek tek yazılabilir. Birden fazla sorgu yazılacak ise
 *   array şeklinde query'ler eklenebilir.
 */
// birden fazla Named Query için @NamedQueries

@NamedQueries({
        /**
         * NamedQuery lere isimlendirme yaparken şu formatta yazmak uygundur. [Entity_Name].[Query_Name]
         */
     @NamedQuery(name = "Post.findAll", query ="select p from Post p"), // tüm sorgular buralarda yazılır ve çekilir.
        @NamedQuery(name = "Post.countAll",query = "select count(p) from Post p"),
        /**
         * Dikkat!!!! Eğer NamedQuery içerisine bir değer girmemiz gerekiyor ise bunu
         * eklemek için değişken tanımlamalıyız. NamedQuery içerisine değişken tanımlamak
         * için :[Değişken_Adı] şeklinde kullanılır.
         */
        @NamedQuery(name = "Post.findAllByUserId",query = "select p from Post p where p.userid =:userid")
})
@Data //
@AllArgsConstructor //
@NoArgsConstructor // parametreli constructor ların tümü
@Builder //default constructor
@Entity
@Table(name = "tblpost")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Id için otomatik artan HB sequence oluşturur
    Long id;
    Long userid;
    Long shareddate;
    String comment;
    String imageurl;
    Integer likes;
    Integer commentcount;
    String location;
}
