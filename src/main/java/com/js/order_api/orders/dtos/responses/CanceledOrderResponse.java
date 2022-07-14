package com.js.order_api.orders.dtos.responses;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class CanceledOrderResponse extends OrderResponse {

	private String message;
}
