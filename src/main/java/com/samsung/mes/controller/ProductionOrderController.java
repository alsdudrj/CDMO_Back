package com.samsung.mes.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsung.mes.dto.ProductionOrderDTO;
import com.samsung.mes.service.ProductionOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/production/orders")
@RequiredArgsConstructor
public class ProductionOrderController {
	
	private final ProductionOrderService service;
	
//	@GetMapping
//	public List<ProductionOrderDTO> getOrders(){
//		return service.getAllOrders();
//	}
	
	@GetMapping
	public Page<ProductionOrderDTO> getOrders(
	    @RequestParam(name="page", defaultValue = "0") int page,
	    @RequestParam(name="size", defaultValue = "10") int size
	) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
	    return service.getOrders(page, size);
	}
	
	
	@PostMapping
	public ProductionOrderDTO createOrder(@RequestBody ProductionOrderDTO dto) {
		return service.createOrder(dto);
	}
	
	//상세조회
	@GetMapping("/{id}")
	public ProductionOrderDTO getOne(@PathVariable("id") Long id) {
		return service.getOne(id);
	}
	
	//수정
	@PutMapping("/{id}")
	public ProductionOrderDTO update(@PathVariable("id") Long id, @RequestBody ProductionOrderDTO dto) {
		return service.update(id, dto);
	}
	
	//삭제
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}
}