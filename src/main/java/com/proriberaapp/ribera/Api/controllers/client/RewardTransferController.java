package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.RewardTransferRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.TransferRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.PagedResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserRewardTransferHistoryResponse;
import com.proriberaapp.ribera.services.client.UserRewardTrasferHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/rewards")
public class RewardTransferController {

    private final UserRewardTrasferHistoryService userRewardTrasferHistoryService;


    @PostMapping("/transfer")
    public Mono<ResponseEntity<Map<String, Object>>> transfer(@RequestBody TransferRequest request) {
        return userRewardTrasferHistoryService.transferRewards(request)
                .then(Mono.just(successResponse("Transferencia realizada con éxito")))
                .onErrorResume(e -> Mono.just(errorResponse(e.getMessage())));
    }

    private ResponseEntity<Map<String, Object>> successResponse(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("message", message);
        return ResponseEntity.ok(body);
    }

    private ResponseEntity<Map<String, Object>> errorResponse(String errorMessage) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", errorMessage);
        return ResponseEntity.badRequest().body(body);
    }


    @GetMapping("/transfers")
    public Mono<PagedResponse<UserRewardTransferHistoryResponse>> getAllTransfers(
            @RequestParam(value = "subcategory", required = false) String subcategory,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        return userRewardTrasferHistoryService.getFilteredTransfers(subcategory, status, dateFrom, dateTo, page, pageSize);
    }
}