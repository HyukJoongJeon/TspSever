package blue_walnut.TspSever.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class TokenExceptionHandler {
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleTspServiceException(TokenException ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage());
        return buildErrorResponse(new RuntimeException("알 수 없는 오류가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);

    }
    // 에러 응답 객체 생성
    private ResponseEntity<ErrorResponse> buildErrorResponse(RuntimeException ex, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), getErrorCode(ex));
        log.error("Error occurred: {} - {}", errorResponse.getCode(), errorResponse.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }

    // 예외 객체에서 ErrorCode 추출
    private String getErrorCode(RuntimeException ex) {
        if (ex instanceof TokenException) return ((TokenException) ex).getCode();
        else return "9999"; // 기본 시스템 오류 코드
    }
    // 에러 응답 구조
    public static class ErrorResponse {
        private final String message;
        private final String code;

        public ErrorResponse(String message, String code) {
            this.message = message;
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }
    }
}
