package com.appcenter.practice.service;


import com.appcenter.practice.domain.Member;
import com.appcenter.practice.dto.request.member.LoginMemberReq;
import com.appcenter.practice.dto.request.member.SignupMemberReq;
import com.appcenter.practice.dto.request.member.UpdateMemberReq;
import com.appcenter.practice.dto.response.member.MemberRes;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.repository.MemberRepository;
import com.appcenter.practice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

import java.util.List;
import java.util.stream.Collectors;

import static com.appcenter.practice.common.StatusCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${profile.default.image}")
    private String defaultProfile;

    @Value("${profile.upload.path}")
    private String uploadFolder;

    public MemberRes getMember(Long memberId){
        Member member=findByMemberId(memberId);
        return MemberRes.from(member);
    }

    public List<MemberRes> getRandomMemberListExcludingMyself(Long memberId, int limit){
        return memberRepository.findRandomMemberListExcludingSelf(memberId,limit).stream()
                .map(MemberRes::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void signup(SignupMemberReq reqDto){
        checkEmailDuplicated(reqDto.getEmail());
        checkNicknameDuplicated(reqDto.getNickname());
        Member member=reqDto.toEntity(passwordEncoder);
        member.uploadProfile(defaultProfile);
        memberRepository.save(member);
    }


    public String login(LoginMemberReq loginMemberReq){
        Member member= memberRepository.findByEmail(loginMemberReq.getEmail())
                .orElseThrow(()->new CustomException(LOGIN_ID_INVALID));
        if(!passwordEncoder.matches(loginMemberReq.getPassword(),member.getPassword())){
            throw new CustomException(PASSWORD_INVALID);
        }

        return jwtTokenProvider.createAccessToken(member.getId(), member.getRole().name());
    }

    @Transactional
    public MemberRes updateMember(Long memberId, UpdateMemberReq updateMemberReq){
        Member member=findByMemberId(memberId);
        member.changePassword(passwordEncoder.encode(updateMemberReq.getPassword()));
        member.changeNickname(updateMemberReq.getNickname());
        return MemberRes.from(member);
    }

    @Transactional
    public void deleteMember(Long memberId){
        Member member=findByMemberId(memberId);
        memberRepository.deleteById(member.getId());
    }

    @Transactional
    public MemberRes uploadProfile(Long memberId, MultipartFile file){
        Member member= findByMemberId(memberId);
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + file.getOriginalFilename();
        File uploadFile = new File(uploadFolder + imageFileName);

        try {
           file.transferTo(uploadFile);
           if(!member.getProfile().equals(defaultProfile)){
               File previousFile=new File(uploadFolder + member.getProfile());
               previousFile.delete();
           }
           member.uploadProfile(imageFileName);
        } catch(Exception e) {
            throw new CustomException(PROFILE_INVALID);
        }
        return MemberRes.from(member);
    }

    @Transactional
    public void deleteProfile(Long memberId){
        Member member=findByMemberId(memberId);
        if(!member.getProfile().equals(defaultProfile)){
            File file=new File(uploadFolder + member.getProfile());
            file.delete();
            member.uploadProfile(defaultProfile);
        }

    }

    private void checkEmailDuplicated(String email){
        if(memberRepository.existsByEmail(email))
            throw new CustomException(EMAIL_DUPLICATED);
    }

    private void checkNicknameDuplicated(String nickname){
        if(memberRepository.existsByNickname(nickname))
            throw new CustomException(NICKNAME_DUPLICATED);
    }

    private Member findByMemberId(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(MEMBER_NOT_EXIST));
    }
}
