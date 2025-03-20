package mango.security.basic_login.dto;

import lombok.Getter;
import lombok.Setter;
import mango.security.basic_login.type.ApiResponseType;

@Setter
@Getter
public class BaseResponseDto<T> {
    // Getter & Setter
    private String status;
    private String message;
    private T data;

    public BaseResponseDto() {
    }

    public BaseResponseDto(ApiResponseType responseType, T data) {
        this.status = responseType.getStatus();
        this.message = responseType.getMessage();
        this.data = data;
    }

}