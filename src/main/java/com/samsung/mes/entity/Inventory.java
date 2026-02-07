//package com.samsung.mes.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//@Entity
//@ToString
//@Getter
//@Setter
//public class Inventory {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "material_id", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "material_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
//    private Material materialId;
//
//    @Column(nullable = false)
//    private String quantity;
//
//    @Column(nullable = false)
//    private String location;
//
//    @Column(nullable = false)
//    private String status;
//}
