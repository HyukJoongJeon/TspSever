package blue_walnut.TspSever.aspect;

import blue_walnut.TspSever.domain.TspLog;
import blue_walnut.TspSever.model.enums.ServiceType;
import blue_walnut.TspSever.model.enums.StatusType;
import blue_walnut.TspSever.repository.TspLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TspLogService {
    private final TspLogRepository tspLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveTspLog(ServiceType serviceType, StatusType statusType, String userCi, String errMsg) {
        TspLog tspLog = TspLog.builder()
                .userCi(userCi)
                .errMsg(errMsg)
                .statusType(statusType)
                .serviceType(serviceType)
                .createdAt(LocalDateTime.now())
                .build();

        tspLogRepository.save(tspLog);
    }

}
