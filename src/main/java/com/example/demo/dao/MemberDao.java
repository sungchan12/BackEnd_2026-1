package com.example.demo.dao;

import com.example.demo.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {

    @PersistenceContext
    private EntityManager em;

    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public Optional<Member> findByEmail(String email) {
        return em.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findFirst();
    }

    public Member save(Member member) {
        if (member.getId() == null) {
            em.persist(member);
            return member;
        }
        return em.merge(member);
    }

    public boolean existsById(Long id) {
        Long count = em.createQuery("SELECT COUNT(m) FROM Member m WHERE m.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }

    public boolean existsByEmail(String email) {
        Long count = em.createQuery("SELECT COUNT(m) FROM Member m WHERE m.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

    public void deleteById(Long id) {
        Member member = em.find(Member.class, id);
        if (member != null) {
            em.remove(member);
        }
    }
}