package com.appcenter.practice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description= "공통 응답 DTO")
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)// Null 값인 필드 json으로 보낼 시 제외
@NoArgsConstructor
//@RequiredArgsConstructor(staticName = "of")
public class CommonResponse<T>{

    @Schema(title = "성공 메시지",description = "성공 메시지", example = "성공 메시지")
    private  String message;

    private  T response;

    @Builder
    private CommonResponse(String message, T response) {
        this.message = message;
        this.response = response;
    }

    //<제네릭 타입> 반환타입 메소드이름()
    //클래스에 있는 T와 메소드의 T는 다른 타입이다.
    //이를 통해 메소드 호출 시 원하는 타입의 객체를 생성할 수 있다.
    public static <T> CommonResponse<T> of(String message, T response){
        return new CommonResponse<T>(message,response);
    }
}
