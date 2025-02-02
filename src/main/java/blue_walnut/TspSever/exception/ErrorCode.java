package blue_walnut.TspSever.exception;

public enum ErrorCode {
    CARDINFO_DECRYPTION_FAILED("TKN_001", "카드 정보 복호화에 실패했습니다"),
    CARDINFO_ENCRYPTION_FAILED("TKN_002", "카드 정보 암호화에 실패했습니다"),
    TOKEN_REGISTRATION_FAILED("TKN_003", "토큰 발급 실패"),
    TOKEN_VERIFICATION_FAILED("TKN_004", "토큰 처리 실패"),
    TOKEN_ALREADY_USED("TKN_005", "이미 사용된 토큰입니다"),
    TOKEN_NOT_FOUND("TKN_006", "토큰을 찾을 수 없습니다"),
    UNKNOWN_ERROR("9999", "알 수 없는 오류");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
