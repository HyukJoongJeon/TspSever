package blue_walnut.TspSever.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long srl;

    private String userCi;
    private String token;
    private String cardInfo;
    private Boolean isUsed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
