package com.appcenter.practice.service;

import com.appcenter.practice.domain.Bucket;
import com.appcenter.practice.domain.Member;
import com.appcenter.practice.dto.request.bucket.AddBucketReq;
import com.appcenter.practice.dto.request.bucket.UpdateBucketReq;
import com.appcenter.practice.dto.response.bucket.BucketRes;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.repository.BucketRepository;
import com.appcenter.practice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.appcenter.practice.common.StatusCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BucketService {
    private final BucketRepository bucketRepository;
    private final MemberRepository memberRepository;


    public List<BucketRes> getMyBucketList(Long memberId){
        Member member=findByMemberId(memberId);
        return  member.getBucketList().stream()
                .map(BucketRes::from)
                .collect(Collectors.toList());
    }

    public List<BucketRes> getOtherUserBucketList(String nickname){
        Member member=memberRepository.findByNickname(nickname)
                .orElseThrow(()-> new CustomException(MEMBER_NOT_EXIST));
        return  member.getBucketList().stream()
                .map(BucketRes::from)
                .collect(Collectors.toList());
    }



    @Transactional
    public BucketRes saveBucket(Long memberId, AddBucketReq reqDto){
        Member member=findByMemberId(memberId);
        Bucket bucket = bucketRepository.save(reqDto.toEntity(member));
        if(member.equals(bucket.getMember()))
            return BucketRes.from(bucket);
        else throw new CustomException(AUTHORIZATION_INVALID);
    }

    @Transactional
    public BucketRes updateBucket(Long memberId, Long bucketId, UpdateBucketReq reqDto){
        Member member=findByMemberId(memberId);
        Bucket bucket = findByBucketId(bucketId);
        bucket.changeContent(reqDto.getContent());
        if(member.equals(bucket.getMember()))
            return BucketRes.from(bucket);
        else throw new CustomException(AUTHORIZATION_INVALID);
    }

    @Transactional
    public BucketRes completeBucket(Long memberId,Long bucketId){
        Member member=findByMemberId(memberId);
        Bucket bucket = findByBucketId(bucketId);
        bucket.changeCompleted();

        if(member.equals(bucket.getMember()))
            return BucketRes.from(bucket);
        else throw new CustomException(AUTHORIZATION_INVALID);
    }

    @Transactional
    public void deleteBucket(Long memberId, Long bucketId){
        Member member=findByMemberId(memberId);
        Bucket bucket = findByBucketId(bucketId);

        if(member.equals(bucket.getMember()))
            bucketRepository.deleteById(bucket.getId());
        else throw new CustomException(AUTHORIZATION_INVALID);
    }

    private Bucket findByBucketId(Long bucketId) {
        return bucketRepository.findById(bucketId)
                .orElseThrow(()->new CustomException(BUCKET_NOT_EXIST));
    }

    private Member findByMemberId(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(MEMBER_NOT_EXIST));
    }

}

