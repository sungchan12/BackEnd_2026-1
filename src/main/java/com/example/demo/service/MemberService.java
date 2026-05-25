package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Member create(Member request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }
        Member member = new Member(null, request.getName(), request.getEmail(), request.getPassword());
        return memberRepository.save(member);
    }

    public Optional<Member> update(Long id, Member request) {
        return memberRepository.findById(id).map(member -> {
            if (request.getName() != null) member.setName(request.getName());
            if (request.getEmail() != null) member.setEmail(request.getEmail());
            if (request.getPassword() != null) member.setPassword(request.getPassword());
            return memberRepository.save(member);
        });
    }

    public boolean delete(Long id) {
        if (!memberRepository.existsById(id)) {
            return false;
        }
        memberRepository.deleteById(id);
        return true;
    }
}