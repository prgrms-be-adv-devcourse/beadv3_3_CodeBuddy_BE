package io.codebuddy.payservice.domain.pay.payments.kafka;

import io.codebuddy.closetbuddy.event.PaymentRequestEvent;
import io.codebuddy.closetbuddy.event.PaymentResultEvent;
import io.codebuddy.closetbuddy.event.PaymentRollbackRequest;
import io.codebuddy.payservice.domain.pay.exception.PayException;
import io.codebuddy.payservice.domain.pay.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 결제 요청 Listener
     * @param event 결제 요청 event
     * order-service가 발행한 결제 요청 수신
     */
    @KafkaListener(topics = "order.payment.request", groupId = "pay-service-group")
    public void handlePaymentRequest(PaymentRequestEvent event){

        log.info("결제 요청 이벤트 수신 : orderId = {}, memberId = {}", event.orderId(), event.memberId());

        try{
            // 결제 수행
            paymentService.payOrder(event);

            // 성공 이벤트 발행
            PaymentResultEvent resultEvent = new PaymentResultEvent(event.orderId(),true,null);

            kafkaTemplate.send("order.payment.result",resultEvent);
            log.info("결제 성공 이벤트 발행 : orderId = {}", event.orderId());

        } catch (PayException e){
            log.warn("결제 실패 - 비즈니스 오류 : {}", e.getMessage());

            PaymentResultEvent resultEvent = new PaymentResultEvent(event.orderId(), false, e.getMessage());

            kafkaTemplate.send("order.payment.result", resultEvent);


        } catch (Exception e) {
            // 결제 실패 이벤트 발행
            log.error("결제 실패 : {}", e.getMessage());
            PaymentResultEvent resultEvent = new PaymentResultEvent(event.orderId(), false, e.getMessage());

            kafkaTemplate.send("order.payment.result", resultEvent);
        }

    }

    /**
     * 결제 롤백 요청 Listener
     * @param event 롤백 요청 event
     * 주문이 최종 실패하였거나 취소되었을 때 이미 결제된 내역을 환불
     */
    @KafkaListener(topics = "order.payment.rollback", groupId = "pay-service-group")
    public void handlePaymentRollback(PaymentRollbackRequest event){

        log.info("결제 롤백(환불) 요청 수신: orderId = {}, memberId = {}", event.orderId(), event.memberId());

        try {
            // 결제 취소
            paymentService.payCancel(event);
            log.info("결제 롤백 완료");
        } catch (Exception e) {
            // TO-DO : 롤백 실패 시 어떻게 처리할 지
            log.error("결제 롤백 실패 : orderId = {}, message = {}", event.orderId(),e.getMessage());

        }

    }


}
