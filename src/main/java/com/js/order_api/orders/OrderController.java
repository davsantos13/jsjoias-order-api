package com.js.order_api.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.js.order_api.config.querys.Request;
import com.js.order_api.orders.dtos.responses.OrderResponse;
import com.js.order_api.orders.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Order EndPoint")
@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {

	private OrderService service;
	
	@Autowired
	public OrderController(OrderService service) {
		this.service = service;
	}
	
	
	@Operation(summary = "retorna uma busca de ordens baseada na request")
	@PostMapping(value = "/search")
	public ResponseEntity<Page<? extends OrderResponse>> findAllOrders(@RequestBody Request request) {
		return ResponseEntity.ok(service.findAll(request));
	}
	
	@Operation(summary = "retorna order pelo id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(OrderResponse.from(service.findById(id)));
	}
}
