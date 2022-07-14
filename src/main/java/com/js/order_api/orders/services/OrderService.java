package com.js.order_api.orders.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.js.order_api.config.querys.GenericSpecification;
import com.js.order_api.config.querys.Request;
import com.js.order_api.exceptions.NotFoundException;
import com.js.order_api.orders.dtos.responses.CanceledOrderResponse;
import com.js.order_api.orders.dtos.responses.OrderResponse;
import com.js.order_api.orders.enums.StatusOrder;
import com.js.order_api.orders.models.Order;
import com.js.order_api.orders.repositories.OrderRepository;

@Service
public class OrderService {

	private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

	private OrderRepository repository;

	@Autowired
	public OrderService(OrderRepository repository) {
		this.repository = repository;
	}

	public Page<? extends OrderResponse> findAll(Request request) {
		return repository.findAll(new GenericSpecification<>(request.getList()), request.getPageable())
				.map(order -> OrderResponse.from(order));
	}

	public Order findById(Long id) {
		var order = repository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Order not found - id - {}", id)));

		return order;
	}

	public Order save(Order order) {
		return repository.save(order);
	}

	public OrderResponse cancelOrder(Long id) {
		try {

			LOG.info("[ ORDER ] - Iniciando busca de order - ID [{}]", id);
			var p = findById(id);

			LOG.info("[ ORDER ] - Cancelando order - ID [{}]", id);
			p.setStatus(StatusOrder.CANCELED);
			repository.save(p);

			LOG.info("[ ORDER ] - Order cancelada com sucesso - ORDER [{}]", p);
			return CanceledOrderResponse
					.builder()
					.clienteId(p.getClienteId())
					.id(p.getId())
					.message("Pedido cancelado com sucesso").build();
		} catch (Exception e) {
			LOG.info("[ ORDER ] - Erro ao realizar cancelado de order - ID [{}]", id);
			e.printStackTrace();

			LOG.error("[ ORDER ] - Motivo do erro - [{}]", e.getMessage());

			return CanceledOrderResponse
					.builder()
					.id(id)
					.message("Erro ao cancelar pedido").build();

		}
	}
}
