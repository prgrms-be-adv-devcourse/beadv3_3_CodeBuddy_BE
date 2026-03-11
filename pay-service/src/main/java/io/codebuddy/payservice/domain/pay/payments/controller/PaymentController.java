package io.codebuddy.payservice.domain.pay.payments.controller;


import io.codebuddy.payservice.domain.common.web.CurrentUser;
import io.codebuddy.payservice.domain.common.web.CurrentUserInfo;
import io.codebuddy.payservice.domain.pay.payments.model.vo.PaymentResponse;
import io.codebuddy.payservice.domain.pay.payments.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결제 내역 단건 조회 및 전체 조회 API")
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 단건 조회
    @Operation(
            summary = "결제 단건 조회",
            description = "특정 결제 내역을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 단건 내역 조회 성공",
                    content = @Content(schema = @Schema(implementation = PaymentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "결제 정보를 찾을 수 없거나 접근 권한이 없습니다.",
                    content = @Content
            )
    })
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentDetail(
            @CurrentUser CurrentUserInfo currentUser,
            @PathVariable Long paymentId
    ) {
        return ResponseEntity.ok(paymentService.getPayment(Long.parseLong(currentUser.userId()), paymentId));
    }

    // 결제 내역 전체 조회
    @Operation(
            summary = "전체 결제 내역 조회",
            description = "회원의 전체 결제 내역을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 내역 조회 성공",
                    content = @Content(schema = @Schema(implementation = PaymentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "결제 정보를 찾을 수 없거나 접근 권한이 없습니다.",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getPaymentList(
            @CurrentUser CurrentUserInfo currentUser
    ) {
        return ResponseEntity.ok(paymentService.getPayments(Long.parseLong(currentUser.userId())));
    }

}
