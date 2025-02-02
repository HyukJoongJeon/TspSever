package blue_walnut.TspSever.repository;

import blue_walnut.TspSever.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Token save(Token token);
    Optional<Token> findBySrl(Long srl);
    Optional<Token> findByUserCiAndIsUsed(String userCi, Boolean isUsed);
    Optional<Token> findBySrlAndUserCi(Long srl, String userCi);
    Optional<Token> findByTokenAndIsUsed(String token, Boolean isUsed);

    List<Token> findAll();
    List<Token> findAllByUserCiAndIsUsed(String userCi, Boolean isUsed);
}
