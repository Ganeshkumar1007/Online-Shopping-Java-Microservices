package com.ganesh.orderservice.service;

import com.ganesh.orderservice.dto.request.OrderLineItemsDto;
import com.ganesh.orderservice.dto.request.OrderRequest;
import com.ganesh.orderservice.dto.response.InventoryResponse;
import com.ganesh.orderservice.event.OrderPlacedEvent;
import com.ganesh.orderservice.model.Order;
import com.ganesh.orderservice.model.OrderLineItems;
import com.ganesh.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
//Transactions ensure that a series of related operations either complete successfully
// as a single unit (commit) or fail entirely (rollback) to maintain data consistency.

//Method-Level Transactional Annotation:
//
//When applied at the method level, @Transactional ensures that the entire method is executed within a single transaction.
// If any exception occurs during the method execution, the transaction will be rolled back.

//Class-Level Transactional Annotation:
//
//When applied at the class level, @Transactional indicates that all public methods of the class should run within a transaction.
// If a method-level @Transactional annotation is also present, it can override the class-level annotation.
public class OrderService {


    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsListDto().stream().map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).toList();

        order.setOrderLineItems(orderLineItemsList);

        List<String> skuCodes = order.getOrderLineItems().stream().map(orderLineItem -> orderLineItem.getSkuCode()).toList();

        //call inventory service and place order if product is in stock
        InventoryResponse[]  inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)//able to ready the response from inventory
                .block();//we use block to make a synchronous request because by default it will run as asynchronous...


        boolean allProductInStock = Arrays.stream(inventoryResponseArray).allMatch(inventoryResponse -> inventoryResponse.isInStock());

        if(allProductInStock){
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "Order Placed Successfully";

        }
        else{
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
