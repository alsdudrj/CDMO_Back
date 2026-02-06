package com.samsung.mes.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.samsung.mes.dto.ProductionOrderDTO;
import com.samsung.mes.entity.ProductionOrder;
import com.samsung.mes.repository.ProductionOrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductionOrderService {
	
	private final ProductionOrderRepository repository;//Repository 주입 (DB 접근)
	// 이 객체로 DB 조회 / 저장
	
	//public List<ProductionOrderDTO> getAllOrders(){//전체 생산지시 조회
	// return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
	////:: -> 메서드 참조 (Method Reference)
	// /*
	//return repository.findAll()   // 1️⃣ DB에서 전체 조회
	//.stream()             // 2️⃣ 리스트를 하나씩 처리
	//.map(this::toDto)     // 3️⃣ Entity → DTO 변환
	//.collect(Collectors.toList()); // 4️⃣ 다시 리스트로 
	// */
	//}
	
	//페이징 조회
	@Transactional
	public Page<ProductionOrderDTO> getOrders(int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		return repository.findAll(pageable).map(this::toDto);
	}
		
	public ProductionOrderDTO createOrder(ProductionOrderDTO dto) {
		if(repository.existsByWorkOrderNo(dto.getWorkOrderNo())) {
			throw new RuntimeException("이미 존재하는 지시번호 입니다");
		}
		
		ProductionOrder entity = ProductionOrder.builder() //2️⃣ DTO → Entity 변환
			.workOrderNo(dto.getWorkOrderNo())
			.orderDate(dto.getOrderDate())
			.itemCode(dto.getItemCode())
			.itemName(dto.getItemName())
			.planQty(dto.getPlanQty())
			.startDate(dto.getStartDate())
			.endDate(dto.getEndDate())
			.status(dto.getStatus() != null ? dto.getStatus() : "대기")
			//👉 상태가 없으면 기본값 "대기"
			.remark(dto.getRemark())
			.build();
			ProductionOrder saved = repository.save(entity);			
			return toDto(saved);
		}
	
	private ProductionOrderDTO toDto(ProductionOrder entity) {
		return ProductionOrderDTO.builder()
			.id(entity.getId())
			.workOrderNo(entity.getWorkOrderNo())
			.orderDate(entity.getOrderDate())
			.itemCode(entity.getItemCode())
			.itemName(entity.getItemName())
			.planQty(entity.getPlanQty())
			.startDate(entity.getStartDate())
			.endDate(entity.getEndDate())
			.status(entity.getStatus())
			.remark(entity.getRemark())
			.build();
	}
		
	//상세
	public ProductionOrderDTO getOne(Long id) {
		ProductionOrder e = repository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("생산지시가 없습니다. id=" + id));
		
		return toDto(e);
	}
	
	//수정
	public ProductionOrderDTO update(Long id, ProductionOrderDTO dto) {
		ProductionOrder e = repository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("생산지시가 없습니다. id=" + id));
				
		//지시번호 수정 (주석으로 비활성)
		//e.setWorkOrderNo(dto.getWorkOrderNo());
		e.setOrderDate(dto.getOrderDate());
		e.setItemCode(dto.getItemCode());
		e.setItemName(dto.getItemName());
		e.setPlanQty(dto.getPlanQty());
		e.setStartDate(dto.getStartDate());
		e.setEndDate(dto.getEndDate());
		e.setStatus(dto.getStatus());
		e.setRemark(dto.getRemark());
		
		return toDto(repository.save(e));
	}
	
	//삭제
	public void delete(Long id) {
		if(!repository.existsById(id)) {
			throw new NoSuchElementException("생산지시가 없습니다. id=" + id);
		}
		repository.deleteById(id);
	}
}