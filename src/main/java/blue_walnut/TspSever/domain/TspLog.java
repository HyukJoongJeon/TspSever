package blue_walnut.TspSever.domain;

import blue_walnut.TspSever.model.enums.ServiceType;
import blue_walnut.TspSever.model.enums.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TspLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long srl;
    private String userCi;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    private String errMsg;

    private LocalDateTime createdAt;
}
