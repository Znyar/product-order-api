package com.znyar.ecommerce.order;

import com.znyar.ecommerce.customer.CustomerClient;
import com.znyar.ecommerce.customer.CustomerResponse;
import com.znyar.ecommerce.exception.BusinessException;
import com.znyar.ecommerce.kafka.OrderConfirmation;
import com.znyar.ecommerce.kafka.OrderProducer;
import com.znyar.ecommerce.orderline.OrderLineRequest;
import com.znyar.ecommerce.orderline.OrderLineService;
import com.znyar.ecommerce.payment.PaymentClient;
import com.znyar.ecommerce.payment.PaymentRequest;
import com.znyar.ecommerce.product.ProductClient;
import com.znyar.ecommerce.product.PurchaseRequest;
import com.znyar.ecommerce.product.PurchaseResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(OrderRequest request) {
        CustomerResponse customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No Customer exists with ID:: " + request.id()));
        List<PurchaseResponse> purchasedProducts = productClient.purchaseProducts(request.products());
        Order order = orderRepository.save(mapper.toOrder(request));
        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }
        PaymentRequest paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(mapper::fromOrder)
                .toList();
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No order found with ID::  %d", orderId)
                ));
    }

}
