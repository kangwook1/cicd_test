package com.appcenter.practice.controller;

import com.appcenter.practice.dto.request.bucket.AddBucketReq;
import com.appcenter.practice.dto.request.bucket.UpdateBucketReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.ErrorResponse;
import com.appcenter.practice.dto.response.bucket.BucketRes;
import com.appcenter.practice.service.BucketService;
import com.appcenter.practice.swagger.CommonResponseBucketRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.appcenter.practice.common.StatusCode.*;

@Tag(name = "Bucket", description = "Bucket API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/buckets")
public class BucketController {
    private final BucketService bucketService;


    @Operation(summary = "나의 버킷 리스트 조회", description = "사용자의 버킷 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "버킷 리스트 조회 성공", content = @Content(schema = @Schema(implementation = CommonResponseBucketRes.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt 토큰입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public ResponseEntity<CommonResponse<List<BucketRes>>> getMyBucketList(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return ResponseEntity.ok(CommonResponse.from(BUCKET_LIST_FOUND.getMessage(), bucketService.getMyBucketList(memberId)));
    }

    @Operation(summary = "닉네임으로 버킷 리스트 조회", description = "닉네임을 통해 회원의 버킷 리스트를 조회합니다.",
            parameters = @Parameter(name = "nickname", description = "닉네임", example = "냄B뚜껑"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "버킷 리스트 조회 성공", content = @Content(schema = @Schema(implementation = CommonResponseBucketRes.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt 토큰입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/by-nickname")
    public ResponseEntity<CommonResponse<List<BucketRes>>> getOtherUserBucketList(@RequestParam String nickname) {
        return ResponseEntity.ok(CommonResponse.from(BUCKET_LIST_FOUND.getMessage(), bucketService.getOtherUserBucketList(nickname)));
    }

    @Operation(summary = "버킷 생성", description = "버킷을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "버킷 생성 성공", content = @Content(schema = @Schema(implementation = CommonResponseBucketRes.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 날짜 형식입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseEntity<CommonResponse<BucketRes>> addBucket(Principal principal, @RequestBody @Valid AddBucketReq reqDto) {
        Long memberId = Long.parseLong(principal.getName());
        return ResponseEntity
                .status(BUCKET_CREATE.getStatus())
                .body(CommonResponse.from(BUCKET_CREATE.getMessage(), bucketService.saveBucket(memberId, reqDto)));
    }

    @Operation(summary = "버킷 수정", description = "버킷을 수정합니다.",
            parameters = @Parameter(name = "bucketId", description = "버킷 Id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "버킷 수정 성공", content = @Content(schema = @Schema(implementation = CommonResponseBucketRes.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 날짜 형식입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 버킷입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping(value = "/{bucketId}")
    public ResponseEntity<CommonResponse<BucketRes>> updateBucket(Principal principal,@PathVariable Long bucketId, @RequestBody @Valid UpdateBucketReq reqDto) {
        Long memberId = Long.parseLong(principal.getName());
        return ResponseEntity
                .ok(CommonResponse.from(BUCKET_UPDATE.getMessage(), bucketService.updateBucket(memberId,bucketId, reqDto)));
    }

    @Operation(summary = "버킷 완료", description = "버킷의 완료 상태가 토글됩니다.",
            parameters = @Parameter(name = "bucketId", description = "버킷 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "버킷 완료 상태 변경 성공", content = @Content(schema = @Schema(implementation = CommonResponseBucketRes.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 버킷입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping(value = "/{bucketId}/complete")
    public ResponseEntity<CommonResponse<BucketRes>> completeBucket(Principal principal,@PathVariable Long bucketId) {
        Long memberId = Long.parseLong(principal.getName());
        return ResponseEntity
                .ok(CommonResponse.from(BUCKET_COMPLETE.getMessage(), bucketService.completeBucket(memberId,bucketId)));
    }

    @Operation(summary = "버킷 삭제", description = "버킷을 삭제합니다",
            parameters = @Parameter(name = "bucketId", description = "버킷 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "버킷 삭제 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 버킷입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping(value = "/{bucketId}")
    public ResponseEntity<CommonResponse<Object>> deleteBucket(Principal principal,@PathVariable Long bucketId) {
        Long memberId = Long.parseLong(principal.getName());
        bucketService.deleteBucket(memberId,bucketId);
        return ResponseEntity
                .ok(CommonResponse.from(TODO_DELETE.getMessage()));
    }
}

