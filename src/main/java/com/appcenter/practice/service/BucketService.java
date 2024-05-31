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

import static com.appcenter.practice.common.StatusCode.BUCKET_NOT_EXIST;
import static com.appcenter.practice.common.StatusCode.MEMBER_NOT_EXIST;

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
        return BucketRes.from(bucket);
    }

    @Transactional
    public BucketRes updateBucket(Long BucketId, UpdateBucketReq reqDto){
        Bucket bucket = findByBucketId(BucketId);
        bucket.changeContent(reqDto.getContent());

        return BucketRes.from(bucket);
    }

    @Transactional
    public BucketRes completeBucket(Long bucketId){
        Bucket bucket = findByBucketId(bucketId);
        bucket.changeCompleted();

        return BucketRes.from(bucket);
    }

    @Transactional
    public void deleteBucket(Long bucketId){
        Bucket bucket = findByBucketId(bucketId);
        bucketRepository.deleteById(bucket.getId());
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

