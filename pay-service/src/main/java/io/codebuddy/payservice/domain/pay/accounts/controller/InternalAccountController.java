package io.codebuddy.payservice.domain.pay.accounts.controller;

import io.codebuddy.payservice.domain.pay.accounts.model.vo.AccountCreateRequest;
import io.codebuddy.payservice.domain.pay.accounts.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/accounts")
@RequiredArgsConstructor
@Tag(name = "InternalAccount", description = "내부 통신 용 계좌 Api")
public class InternalAccountController {

    private final AccountService accountService;

    @Operation(
            summary = "계좌 생성",
            description = "회원 가입 시 회원의 계좌를 생성합니다."
    )
    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody AccountCreateRequest request) {
        accountService.createAccount(request.memberId());
        return ResponseEntity.ok().build();
    }
}
