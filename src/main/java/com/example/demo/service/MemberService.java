package com.example.demo.service;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.Member;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> findAll() {
        return memberDao.findAll();
    }

    public Member findById(Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 사용자입니다: " + id));
    }

    @Transactional
    public Member create(Member request) {
        if (memberDao.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }
        return memberDao.save(new Member(null, request.getName(), request.getEmail(), request.getPassword()));
    }

    @Transactional
    public Member update(Long id, Member request) {
        Member member = findById(id);
        if (request.getEmail() != null && !request.getEmail().equals(member.getEmail())
                && memberDao.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }
        if (request.getName() != null) member.setName(request.getName());
        if (request.getEmail() != null) member.setEmail(request.getEmail());
        if (request.getPassword() != null) member.setPassword(request.getPassword());
        return memberDao.save(member);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        memberDao.deleteById(id);
    }
}