package com.samsung.mes.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "sales_ORDER", indexes = {
		@Index(name = "idx_sales_order_order_date", columnList = "order_date"),
		@Index(name = "idx_sales_order_customer_code", columnList = "customer_code"),
		@Index(name = "idx_sales_order_item_code", columnList = "item_code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "order_date", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate orderDate;
	
	@Column(name = "customer_code", nullable = false, length = 30)
	private String customerCode;
	
	@Column(name = "customer_name", nullable = false, length = 100)
	private String customerName;
	
	@Column(name = "item_code", nullable = false, length = 30)
	private String itemCode;
	
	@Column(name = "item_name", nullable = false, length = 200)
	private String itemName;
	
	@Column(name = "order_qty", nullable = false)
	private BigDecimal orderQty;

    @Column(name = "delivered_qty", nullable = false, precision = 18, scale = 3)
    @Builder.Default
    private BigDecimal deliveredQty = BigDecimal.ZERO;
	
	@Column(name= "price", nullable = false, precision = 18, scale = 2) //18자리에 소수점 이하 2자리 (3자리면 자동 반올림 오류가 생김)
	private BigDecimal price; //double은 소수점 계산에 오차가 발생할 수 있음
	
	@Column(name = "amount", nullable = false, precision = 18, scale = 2)
	private BigDecimal amount;
	
	@Column(name = "delivery_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate deliveryDate;
	
	@Column(name = "remark", length= 1000)
	private String remark;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void calcAmount() {
        BigDecimal qty = (orderQty == null) ? BigDecimal.ZERO : orderQty;
        BigDecimal pr = (price == null) ? BigDecimal.ZERO : price;

        // amount가 null이거나, 정책상 항상 재계산하고 싶으면 무조건 재계산
        this.amount = qty.multiply(pr);

        // deliveredQty null 방어
        if (this.deliveredQty == null) this.deliveredQty = BigDecimal.ZERO;
    }
}
