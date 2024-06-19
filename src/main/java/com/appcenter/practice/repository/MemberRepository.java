package com.appcenter.practice.repository;

import com.appcenter.practice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>{
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    @Query(value = "SELECT * FROM member WHERE id != :id ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Member> findRandomMemberListExcludingSelf(@Param("id") Long memberId, @Param("limit") int limit);

}
