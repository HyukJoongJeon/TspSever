package blue_walnut.TspSever.aspect;

import blue_walnut.TspSever.exception.TokenException;
import blue_walnut.TspSever.model.CardInfo;
import blue_walnut.TspSever.model.TokenReq;
import blue_walnut.TspSever.model.enums.ServiceType;
import blue_walnut.TspSever.model.enums.StatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TspLogAspect {
    private final TspLogService tspLogService;

    @Around("execution(* blue_walnut.TspSever.service..*(..))")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        String userCi = extractUserCi(args);
        ServiceType serviceType = determineServiceType(methodName);

        try {
            result = joinPoint.proceed();
            tspLogService.saveTspLog(serviceType, StatusType.DN, userCi, "0000");
            return result;
        } catch (TokenException e) {
            log.error("트랜잭션 로그 저장 실패: {} - 에러 메시지: {}", methodName, e.getMessage());
            tspLogService.saveTspLog(serviceType, StatusType.FL, userCi, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("예기치 않은 오류 발생: {} - 에러 메시지: {}", methodName, e.getMessage());
            tspLogService.saveTspLog(serviceType, StatusType.FL, userCi, "UNKNOWN_ERROR");
            throw new RuntimeException("트랜잭션 처리 중 알 수 없는 오류 발생", e);
        }
    }

    private String extractUserCi(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof CardInfo) {
                return ((CardInfo) arg).userCi();
            } else if (arg instanceof TokenReq) {
                return ((TokenReq) arg).userCi();
            } else if (arg instanceof String) {
                return String.valueOf(arg);
            }
        }
        return "UNKNOWN_USER";
    }


    private ServiceType determineServiceType(String methodName) {
        if (methodName.contains("tokenRegistry")) {
            return ServiceType.TKN_REG;
        } else if (methodName.contains("verifyToken")) {
            return ServiceType.TKN_USED;
        } else if (methodName.contains("cancelToken")) {
            return ServiceType.TKN_CANCEL;
        } else if (methodName.contains("requestToken")) {
            return ServiceType.TKN_LOOKUP;
        }
        return ServiceType.UNKNOWN;
    }
}