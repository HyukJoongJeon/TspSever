package blue_walnut.TspSever.service;

import blue_walnut.TspSever.domain.Token;
import blue_walnut.TspSever.exception.ErrorCode;
import blue_walnut.TspSever.exception.TokenException;
import blue_walnut.TspSever.model.CardInfo;
import blue_walnut.TspSever.model.TokenReq;
import blue_walnut.TspSever.repository.TokenRepository;
import blue_walnut.TspSever.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TspService {
    @Value("${aes.secret-key}")
    private String SECRET_KEY;

    private final TokenRepository tokenRepository;
    @Transactional
    public Long tokenRegistry(CardInfo cardInfo) {
        log.info("토큰 발급 시작: userCi={}, cardNo={}", cardInfo.userCi(), cardInfo.cardNo());

        String encryptedCardInfo = digestSHA256(cardInfo);

        List<Token> tokenList = tokenRepository.findAllByUserCiAndIsUsed(cardInfo.userCi(), false);
        Optional<Token> remainToken = tokenList.stream()
                .filter(d -> d.getCardInfo().equals(encryptedCardInfo))
                .findFirst();

        if (remainToken.isPresent()) {
            log.info("미사용 토큰이 존재하여 리턴: tokenSrl={}", remainToken.get().getSrl());
            return remainToken.get().getSrl();
        }

        Token token = createNewToken(cardInfo, encryptedCardInfo);
        tokenRepository.save(token);

        return token.getSrl();
    }

    @Transactional(readOnly = true)
    public String requestToken(Long srl, String userCi) {
        log.info("토큰 조회 시작: srl={}, userCi={}", srl, userCi);

        return tokenRepository.findBySrlAndUserCi(srl, userCi)
                .map(token -> {
                    if (token.getIsUsed()) throw new TokenException(ErrorCode.TOKEN_ALREADY_USED);
                    else return token.getToken();
                })
                .orElseThrow(() -> new TokenException(ErrorCode.TOKEN_NOT_FOUND));
    }

    @Transactional
    public Boolean verifyToken(TokenReq token) {
        log.info("토큰 사용 처리 시작: token={}", token.toString());
        Token verifyToken = tokenRepository.findByTokenAndIsUsed(token.token(), false)
                    .orElseThrow(() -> new TokenException(ErrorCode.TOKEN_NOT_FOUND));

        verifyToken.setIsUsed(true);
        verifyToken.setUpdatedAt(LocalDateTime.now());
        tokenRepository.save(verifyToken);

        return true;
    }

    @Transactional
    public Boolean cancelToken(TokenReq token) {
        log.info("토큰 사용 취소 처리 시작: token={}", token.toString());
        Token verifyToken = tokenRepository.findByTokenAndIsUsed(token.token(), true)
                .orElseThrow(() -> new TokenException(ErrorCode.TOKEN_NOT_FOUND));

        verifyToken.setIsUsed(false);
        verifyToken.setUpdatedAt(LocalDateTime.now());
        tokenRepository.save(verifyToken);

        return true;
    }

    private Token createNewToken(CardInfo cardInfo, String encryptedCardInfo) {
        // 카드 사를 통한 카드 유효성 검사는 생략, 토큰은 UUID로 대체한다
        return Token.builder()
                .userCi(cardInfo.userCi())
                .cardInfo(encryptedCardInfo)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isUsed(false)
                .build();
    }
    private String decryptCardInfo(String encryptedData) {
        try {
            return EncryptUtil.decryptParam(SECRET_KEY, encryptedData);
        } catch (Exception e) {
            log.error("카드 정보 복호화 실패: {}", e.getMessage());
            throw new TokenException(ErrorCode.CARDINFO_DECRYPTION_FAILED);
        }
    }
    private String digestSHA256(CardInfo cardInfo) {
        String cardNo = decryptCardInfo(cardInfo.cardNo());
        String cardPwd = decryptCardInfo(cardInfo.cardPwd());
        String vldDt = decryptCardInfo(cardInfo.vldDt());

        try {
            return EncryptUtil.digestSHA256(cardNo + "|" + cardPwd + "|" + vldDt);
        } catch (Exception e) {
            throw new TokenException(ErrorCode.CARDINFO_ENCRYPTION_FAILED);
        }
    }
    private void test() {
        throw new TokenException(ErrorCode.UNKNOWN_ERROR);
    }
}
