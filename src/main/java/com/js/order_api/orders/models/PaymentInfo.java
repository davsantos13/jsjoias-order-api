package com.js.order_api.orders.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.js.order_api.orders.enums.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments_info")
public class PaymentInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tx_installments")
	private Integer installments;

	@Column(name = "tx_card_id")
	private String cardId;

	@Enumerated(EnumType.STRING)
	@Column(name = "tx_method_payment")
	private PaymentMethod paymentMethod;

}
