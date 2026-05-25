package com.example.demo.repository;

import com.example.demo.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemberRepository {

    private final Map<Long, Member> store = new HashMap<>(Map.of(
            1L, new Member(1L, "회원0", "member0@example.com", "password0"),
            2L, new Member(2L, "회원1", "member1@example.com", "password1"),
            3L, new Member(3L, "회원2", "member2@example.com", "password2"),
            4L, new Member(4L, "회원3", "member3@example.com", "password3")
    ));
    private final AtomicLong sequence = new AtomicLong(5);

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<Member> findByEmail(String email) {
        return store.values().stream()
                .filter(m -> m.getEmail().equals(email))
                .findFirst();
    }

    public Member save(Member member) {
        if (member.getId() == null) {
            member.setId(sequence.getAndIncrement());
        }
        store.put(member.getId(), member);
        return member;
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    public boolean existsByEmail(String email) {
        return store.values().stream()
                .anyMatch(m -> m.getEmail().equals(email));
    }

    public void deleteById(Long id) {
        store.remove(id);
    }
}