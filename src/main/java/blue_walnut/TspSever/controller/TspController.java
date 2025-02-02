package blue_walnut.TspSever.controller;

import blue_walnut.TspSever.model.CardInfo;
import blue_walnut.TspSever.model.TokenReq;
import blue_walnut.TspSever.service.TspService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/tsp")
public class TspController {

    public final TspService tspService;

    @PostMapping("/tokenRegistry")
    public Long tokenRegistry(@RequestBody CardInfo cardInfo) {
        return tspService.tokenRegistry(cardInfo);
    }

    @GetMapping("/requestToken")
    public String requestToken(@RequestParam Long srl,
                               @RequestParam String userCi) {
        return tspService.requestToken(srl, userCi);
    }

    @PatchMapping("/verifyToken")
    public Boolean verifyToken(@RequestBody TokenReq token) {
        return token.isCancel() ? tspService.cancelToken(token) : tspService.verifyToken(token);
    }
}
