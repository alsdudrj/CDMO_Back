package com.samsung.mes.entity;

import jakarta.persistence.*;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name="inventory_item",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_inventory_item_code", columnNames = "item_code")
        },indexes ={
        @Index(name = "ix_inventory_item_name", columnList="item_name")
}
)
@Getter
@Setter
@NoArgsConstructor//기본 생성자
@AllArgsConstructor//모든 필드 생성자
@Builder//객체를 편하게 생성 예 덕분에 new
public class InventoryItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(name="stock_qty", nullable = false)
    @Min(0)
    private Integer stockQty;

    @Column(name="location", length=100)
    @NotBlank
    private String location;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "USABLE|QUARANTINE|EXPIRED")
    private String status;

    /////////////////////////////////////////////

    @Column(name="item_code", length=50, nullable = false)
    @NotBlank
    private String itemCode;

    @Column(name="item_name", length=200, nullable = false)
    @NotBlank
    private String itemName;

    @Column(name="item_group", length=100)
    @NotBlank
    private String itemGroup;

    @Column(name="spec", length=200)
    @NotBlank
    private String spec;

    @Column(name="warehouse", length=100)
    @NotBlank
    private String warehouse;

    @Column(name="safety_stock")
    @Min(0)
    private Integer safetyStock;

    @Column(name="in_price", precision = 18, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal inPrice;

    @Column(name="out_price", precision = 18, scale = 2)
    @DecimalMin("0.0") //소수점 2자리 에 음수 불가
    private BigDecimal outPrice;

    @Column(name="use_yn", length = 1, nullable = false)
    @Pattern(regexp = "Y|N")
    private String useYn;

    @Column(name="remark", length =500)
    private String remark;

    @Column(name ="updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        if(stockQty == null) stockQty = 0;
        if(useYn == null || useYn.isBlank()) useYn = "Y";
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}