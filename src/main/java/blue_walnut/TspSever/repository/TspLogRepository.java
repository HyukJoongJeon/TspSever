package blue_walnut.TspSever.repository;

import blue_walnut.TspSever.domain.TspLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TspLogRepository extends JpaRepository<TspLog, Long> {
    @Override
    <T extends TspLog> T save(T history);
    Optional<TspLog> findBySrl(Long srl);
    Optional<TspLog> findByUserCi(String userCi);
    Optional<TspLog> findByCreatedAtBetween(LocalDateTime startDt, LocalDateTime endDt);

    List<TspLog> findAll();
}
