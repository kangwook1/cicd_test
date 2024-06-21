package com.appcenter.practice.controller;

import com.appcenter.practice.dto.request.member.LoginMemberReq;
import com.appcenter.practice.dto.request.member.SignupMemberReq;
import com.appcenter.practice.dto.request.member.UpdateMemberReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.ErrorResponse;
import com.appcenter.practice.dto.response.member.MemberRes;
import com.appcenter.practice.service.MemberService;
import com.appcenter.practice.swagger.CommonResponseMemberRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

import static com.appcenter.practice.common.StatusCode.*;


@Tag(name = "Member", description = "Member API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;


    @Operation(summary = "멤버 회원가입", description =
            "1. 모든 값은 비어있거나 null이면 유효성 검사에 실패합니다.<br>"+
            "2. email값은 중복이 허용되지 않습니다.<br>"+
            "3. 비밀번호는 8자 이상의 영어, 숫자, @,$,!,%,*,#,?,& 중 하나 이상을 포함한 특수문자로 이루어져야 합니다.<br>"+
            "4. 닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "멤버 생성 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseEntity<CommonResponse<Object>> signup(@RequestBody @Valid SignupMemberReq reqDto){
        memberService.signup(reqDto);
        return ResponseEntity
                .status(MEMBER_CREATE.getStatus())
                .body(CommonResponse.from(MEMBER_CREATE.getMessage()));
    }


    @Operation(summary = "멤버 로그인", description ="로그인 합니다. 응답 헤더로 jwt토큰을 전달합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "/login")
    public ResponseEntity<CommonResponse<Object>> login(@RequestBody @Valid LoginMemberReq signInMemberReqDto){

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,  memberService.login(signInMemberReqDto))
                .body(CommonResponse.from(MEMBER_LOGIN.getMessage()));
    }

    @Operation(summary = "멤버 조회", description ="해당 멤버를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponseMemberRes.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public ResponseEntity<CommonResponse<MemberRes>> getMember(Principal principal){
        // 요청에서 인증 정보를 추출하고, 이 정보를 이용해 서비스 계층을 호출
        Long memberId=Long.parseLong(principal.getName());
        return ResponseEntity.ok()
                .body(CommonResponse.from(MEMBER_FOUND.getMessage(),memberService.getMember(memberId)));
    }


    @Operation(summary = "닉네임으로 멤버 조회", description ="닉네임으로 해당 멤버를 조회합니다.",
            parameters = @Parameter(name = "nickname", description = "닉네임", example = "냄B뚜껑"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponseMemberRes.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "by-nickname")
    public ResponseEntity<CommonResponse<MemberRes>> getMemberByNickname(@RequestParam String nickname){
        return ResponseEntity.ok()
                .body(CommonResponse.from(MEMBER_FOUND.getMessage(),memberService.getMemberByNickname(nickname)));
    }

    @Operation(summary = "랜덤 멤버 조회", description ="자기 자신을 제외한 랜덤 멤버를 조회합니다.<br>"+
            "limit에 값이 없으면 기본적으로 3명을 조회하고 실제 db에 있는 멤버 수가 limit보다 작으면 전부 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랜덤 멤버리스트 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponseMemberRes.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "/random")
    public ResponseEntity<CommonResponse<List<MemberRes>>> getRandomMemberListExcludingMyself(Principal principal, @RequestParam(defaultValue = "3") int limit){
        Long memberId=Long.parseLong(principal.getName());
        return ResponseEntity.ok()
                .body(CommonResponse.from(MEMBER_FOUND.getMessage(),memberService.getRandomMemberListExcludingMyself(memberId,limit)));
    }

    @Operation(summary = "멤버 수정", description ="해당 멤버를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 수정 성공",content= @Content(schema = @Schema(implementation = CommonResponseMemberRes.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping
    public ResponseEntity<CommonResponse<MemberRes>> updateMember(Principal principal, @RequestBody @Valid UpdateMemberReq updateMemberReq){

        Long memberId=Long.parseLong(principal.getName());
        return ResponseEntity.ok()
                .body(CommonResponse.from(MEMBER_UPDATE.getMessage(),memberService.updateMember(memberId, updateMemberReq)));
    }

    @Operation(summary = "멤버 삭제", description ="회원탈퇴합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping
    public ResponseEntity<CommonResponse<Object>> deleteMember(Principal principal){
        Long memberId=Long.parseLong(principal.getName());
        memberService.deleteMember(memberId);
        return ResponseEntity.ok()
                .body(CommonResponse.from(MEMBER_DELETE.getMessage()));
    }

    @Operation(summary = "멤버 프로필 업로드", description ="멤버 프로필을 업로드합니다.<br>"+
    "프로필 이미지는 경로로 응답을 줍니다.<br>"+
    "이미지는 http://na2ru2.me:5152/profile/경로를 fetch하면 얻을 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 업로드 성공",content= @Content(schema = @Schema(implementation = CommonResponseMemberRes.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})

    @PostMapping(value = "/profile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<MemberRes>> uploadProfile(Principal principal, @RequestParam MultipartFile file){
        Long memberId=Long.parseLong(principal.getName());
        return ResponseEntity.ok()
                .body(CommonResponse.from(MEMBER_PROFILE_UPLOAD.getMessage(),memberService.uploadProfile(memberId,file)));
    }

    @Operation(summary = "멤버 프로필 삭제", description ="멤버 프로필을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 삭제 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping(value = "/profile")
    public ResponseEntity<Object> deleteProfile(Principal principal){
        Long memberId=Long.parseLong(principal.getName());
        memberService.deleteProfile(memberId);
        return ResponseEntity.ok()
                .body(CommonResponse.from(MEMBER_PROFILE_DELETE.getMessage()));

    }

}
