package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 사용자입니다: " + id));
    }

    public Member create(Member request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }
        return memberRepository.save(new Member(null, request.getName(), request.getEmail(), request.getPassword()));
    }

    public Member update(Long id, Member request) {
        Member member = findById(id);
        if (request.getEmail() != null && !request.getEmail().equals(member.getEmail())
                && memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }
        if (request.getName() != null) member.setName(request.getName());
        if (request.getEmail() != null) member.setEmail(request.getEmail());
        if (request.getPassword() != null) member.setPassword(request.getPassword());
        return memberRepository.save(member);
    }

    public void delete(Long id) {
        findById(id);
        memberRepository.deleteById(id);
    }
}