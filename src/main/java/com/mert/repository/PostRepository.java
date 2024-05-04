package com.mert.repository;

import com.mert.entity.Post;
import com.mert.utility.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;

public class PostRepository {

    // normalde Repository var ancak tekrar amaçlı manual yazalım.
    private final EntityManagerFactory emf;
    private EntityManager em;
    private CriteriaBuilder criteriaBuilder;

    public PostRepository(){
        this.emf = Persistence.createEntityManagerFactory("CRM");
        em = emf.createEntityManager();
        this.criteriaBuilder = em.getCriteriaBuilder();
    }

    private void openSession(){
        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.close();
    }
    private void closeSession(){
        em.getTransaction().commit();
        em.close();
    }
    private void rollBack(){
        em.getTransaction().rollback();
        em.close();
    }

    public Post save(Post post){
        openSession();
        em.persist(post);
        em.close();
        return post;
    }




}
