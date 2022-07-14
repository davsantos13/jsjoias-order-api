package com.js.order_api.orders.dtos.responses;

import com.js.order_api.orders.enums.StatusOrder;
import com.js.order_api.orders.models.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

	private Long id;
	private Double totalValue;
	private StatusOrder status;
	private Long clienteId;
	
	public static OrderResponse from(Order order) {
		return OrderResponse
				.builder()
				.clienteId(order.getClienteId())
				.totalValue(order.getTotalValue())
				.status(order.getStatus())
				.id(order.getId())
				.build();
	}
}
