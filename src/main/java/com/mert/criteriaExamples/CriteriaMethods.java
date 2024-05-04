package com.mert.criteriaExamples;

import com.mert.entity.Post;
import com.mert.view.VwComment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import javax.xml.stream.events.Comment;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CriteriaMethods {
    private EntityManager em;
    private EntityManagerFactory emf;
    private CriteriaBuilder criteriaBuilder;

    public CriteriaMethods() {
        emf = Persistence.createEntityManagerFactory("CRM");;
        em = emf.createEntityManager();
        criteriaBuilder = em.getCriteriaBuilder();
    }

    /**
     * select * from tblcomment ->>> ilgili entity içinde ki
     * tüm alanlar gelir.
     * Ancak bazen tek bir alan almak ihtiyacı olacaktır.Böyle
     * durumlarda ->> select comment from tblcomment yapmak isteriz.
     *
     * Sorgu yapabilmek için ihtiyacımız olan:
     *  1. CriteriaBuilder
     *  2. CriteriaQuery<T></T>
     */

    /**
     * Tüm veritabanındaki yorumları çeker.
     * @return
     */
    public List<String> selectOneColumn(){
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        // select from yapmak için root kullanılır.
        // root'un temel mantığı entity seçmek
        Root<Comment> root = criteriaQuery.from(Comment.class);
        criteriaQuery.select(root.get("comment"));
      return   em.createQuery(criteriaQuery).getResultList();
        // sonuca müdahale edilmeyecekse genelde return yapılır. List değil!
        // List<String> result = em.createQuery(criteriaQuery).getResultList();

    }

    /**
     * Post idsine göre kısıtlarak tüm yorumları değil sadece ilgili yorumları
     * almak için
     * select commment from tblcomment where postid = ?
     */
    public List<String> selectOneColumnByPostId(Long postId){
        CriteriaQuery<String> criteriaQuery=criteriaBuilder.createQuery(String.class);
        // ben hangi tabloya varlığa istek atacaksam ona karşılık gelen sınıfa ihtiyacım var.
        Root<Comment> root = criteriaQuery.from(Comment.class);
        //select comment
        criteriaQuery.select(root.get("comment"));
        //where postid = ?
        // reflection ile parçalanır ve class içinde postid alınır. Sonrasında bizim
        //verdiğimiz postId ile eşitliğe bakılır.
        criteriaQuery.where(criteriaBuilder.equal(root.get("postid"),postId));
        return em.createQuery(criteriaQuery).getResultList();
    }

    /**
     * select * from tblcomment where id=?
     * Bur sorguda tek bir değer alınır veya sonuç alınmaz.
     * Bu sorguda entity varlığının kendisi döner.
     */
    public Optional<Comment> findById(Long id){
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> root = criteriaQuery.from(Comment.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"),id));
        Comment result = em.createQuery(criteriaQuery).getSingleResult();
        if(result != null){
            return Optional.of(result);
        }else {
            return Optional.empty();
        }
        /**
         * Bu tarz sorgularda, vermiş olduğumuz comment bilgisi ya gelecektir ya gelmeyecektir.
         * Direk comment olarak dönersek, başka biri direckt kullanır ve null döner
         * ve bu null'sa uygulama patlar.
         * Bir değer null dönme ihtimali varsa Optional kullanılır.
         *   // em.createQuery(criteriaQuery).getSingleResult();
         */
    }
    /**
     * select * from tblcomment where postid = ?
     * Tabloda ihtiyaç duyduğumuz kadar alan alırız.
     * select userid,comment from tblcomment where postid = ?
     */
    public List<Object[]> selectManyColumn (Long postid){
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Comment> root = criteriaQuery.from(Comment.class);
        //select userid, comment from
        // Opsiyon 1:
      //  criteriaQuery.select(criteriaBuilder.array(root.get("userid"),root.get("comment")));

        // Opsiyon 2:
        Path<Long> userid = root.get("userid");
        Path<String> comment = root.get("comment");
        criteriaQuery.select(criteriaBuilder.array(userid,comment));
        criteriaQuery.where((criteriaBuilder.equal(root.get("postid"),postid)));
        return em.createQuery(criteriaQuery).getResultList();
    }
    /**
     * select * from tblcomment where postid = ? and userid>60 and comment like'%ankara%'
     */

    // predicate dışardan alınmaz. predicate oluşturulur.
    public List<Comment> findAllByPostIdAndUserIdGreaterThanAndCommentLike(Long postId,Long userId,String comment){
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> root = criteriaQuery.from(Comment.class);
        criteriaQuery.select(root);
        Predicate predicatePostId = criteriaBuilder.equal(root.get("postid"),postId);
        Predicate predicateUserId = criteriaBuilder.greaterThan(root.get("userid"),userId);
        Predicate predicateCommentLike = criteriaBuilder.like(root.get("comment"),"%"+comment+"%");
        // birden fazla parametrenin iç içe girmesi lazım
        criteriaQuery.where(criteriaBuilder.and(predicatePostId,predicateUserId,predicateCommentLike));
        //predicate istiyor. istediğimiz sırada alabilir predicate'leri sorun olmaz.
        return em.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Hibernate ile zor ve karmaşık olan sorgularda.
     *  Native Query kullanılır.
     *  Hibernate üzerinden direkt SQL komutlarını çalıştırabildiğimiz yapıdır.
     */
    //bağlantıyı yöneten EntityManager

    public List<Comment> findAllNativeSQL(){
        List<Comment> result = em.createNativeQuery("select * from tblcomment",Comment.class).getResultList();
        return result;
    }
//Native SQL kullanarak tek bir değer almak istersek.
    public List<String> getOneColumnNativeSQL(){
        return em.createNativeQuery("select comment from tblcomment",String.class).getResultList();
    }
    /**
     * select userid,comment from tblcomment
     * burada view kullanmak mantık olacaktır. Bir class açıp gerekli olan değişkenleri oradan alırız.
     * VwComment adında bir varlık tanımlarız. view package içine
     *
     */
    public List<VwComment> getViewNativeSQL(){
        return em.createNativeQuery("select id,userid,comment from tblcomment", VwComment.class).getResultList();
    }

    /**
     * Named Query
     */
    public List<Post> findAllNameQuery(){
        return em.createNamedQuery("Post.findAll",Post.class).getResultList();
    }

    public BigDecimal countPostSize(){
        return em.createNamedQuery("Post.countAll",BigDecimal.class).getSingleResult();
    }

    public List<Post> findAllByUserId(Long userId){
        TypedQuery<Post> typedQuery = em.createNamedQuery("Post.findAllByUserId",Post.class);
        typedQuery.setParameter("userid",userId);
        return typedQuery.getResultList();
    }
}
