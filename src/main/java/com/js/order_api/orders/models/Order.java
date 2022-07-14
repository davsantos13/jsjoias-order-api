package com.js.order_api.orders.models;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.js.order_api.orders.enums.StatusOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tx_order_id")
	private String orderId;

	@Column(name = "tx_code")
	private String code;

	@Column(name = "dt_created_at")
	private LocalDateTime createdAt;

	@Column(name = "dt_paid_at")
	private LocalDateTime paidAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "tx_status")
	private StatusOrder status;

	@Column(name = "tx_status_description")
	private String statusDescription;

	@Column(name = "nr_total_value")
	private Double totalValue;

	@Column(name = "nr_sub_total")
	private Double subTotal;

	@Column(name = "nr_shipping_value")
	private Double shippingValue;

	@Column(name = "nr_off_value")
	private Double offValue;

	@ManyToMany
	private List<Cart> carts;

	@Column(name = "cliente_id")
	private Long clienteId;

	@OneToOne(cascade = CascadeType.ALL)
	private PaymentInfo paymentInfo;
}
