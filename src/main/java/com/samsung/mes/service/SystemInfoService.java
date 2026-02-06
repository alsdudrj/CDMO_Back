package com.samsung.mes.service;

import com.samsung.mes.dto.SystemInfoRequest;
import com.samsung.mes.dto.SystemInfoResponse;
import com.samsung.mes.entity.SystemInfo;
import com.samsung.mes.repository.SystemInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SystemInfoService {
    private final SystemInfoRepository repo;

    //목록조회
    public Page<SystemInfoResponse> list (String useYn, String searchType, String keyword, String status, Pageable pageable) {
        String u = (useYn == null || useYn.isBlank()) ? "Y" : useYn;
        String k = (keyword == null) ? "" : keyword.trim();
        String st = (status == null) ? "" : status.trim();

        Page<SystemInfo> page;

        // status + name keyword 조합(많이 쓰면)
        if (!st.isBlank() && !k.isBlank() && (searchType == null || "NAME".equalsIgnoreCase(searchType))) {
            page = repo.findByUseYnAndStatusAndSystemNameContainingIgnoreCase(u, st, k, pageable);
            return page.map(this::toRes);
        }

        if (k.isBlank()) {
            // keyword 없을 때
            if (!st.isBlank()) page = repo.findByUseYnAndStatus(u, st, pageable);
            else page = repo.findByUseYn(u, pageable);
            return page.map(this::toRes);
        }

        // keyword 있을 때 searchType에 따라 분기
        String type = (searchType == null || searchType.isBlank()) ? "NAME" : searchType.toUpperCase();

        switch (type) {
            case "GROUP" -> page = repo.findByUseYnAndSystemGroupContainingIgnoreCase(u, k, pageable);
            case "OWNER" -> page = repo.findByUseYnAndOwnerContainingIgnoreCase(u, k, pageable);
            case "STATUS" -> page = repo.findByUseYnAndStatus(u, k.toUpperCase(), pageable); // keyword를 status로 사용
            default -> page = repo.findByUseYnAndSystemNameContainingIgnoreCase(u, k, pageable);
        }

        return page.map(this::toRes);
    }

    //단건조회
    public SystemInfoResponse getOne(Long id){
        SystemInfo e = repo.findById(id).orElseThrow(() -> new NoSuchElementException("시스템정보가 없습니다. id: " + id));

        return toRes(e);
    }

    //등록
    @Transactional
    public SystemInfoResponse create(SystemInfoRequest req) {
        String code = req.getSystemCode().trim();
        if (repo.existsBySystemCode(code)) {
            throw new IllegalArgumentException("이미 존재하는 systemCode 입니다: " + code);
        }

        SystemInfo e = SystemInfo.builder()
                .systemCode(code)
                .systemName(req.getSystemName().trim())
                .systemGroup(req.getSystemGroup().trim())
                .owner(req.getOwner())
                .version(req.getVersion())
                .status(req.getStatus()) // null이면 prePersist가 ACTIVE
                .useYn(req.getUseYn())   // null이면 prePersist가 Y
                .remark(req.getRemark())
                .build();

        return toRes(repo.save(e));
    }

    //수정
    @Transactional
    public SystemInfoResponse update(Long id, SystemInfoRequest req) {
        SystemInfo e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SystemInfo가 없습니다. id=" + id));

        String newCode = req.getSystemCode().trim();
        if (!e.getSystemCode().equals(newCode) && repo.existsBySystemCode(newCode)) {
            throw new IllegalArgumentException("이미 존재하는 systemCode 입니다: " + newCode);
        }

        e.setSystemCode(newCode);
        e.setSystemName(req.getSystemName().trim());
        e.setSystemGroup(req.getSystemGroup().trim());
        e.setOwner(req.getOwner());
        e.setVersion(req.getVersion());

        if (req.getStatus() != null && !req.getStatus().isBlank()) e.setStatus(req.getStatus().trim());
        if (req.getUseYn() != null && !req.getUseYn().isBlank()) e.setUseYn(req.getUseYn().trim());

        e.setRemark(req.getRemark());

        return toRes(e);
    }

    //삭제
    @Transactional
    public void delete(Long id){
        if(!repo.existsById(id)){
            throw new NoSuchElementException("시스템정보가 없습니다 id: " + id);
        }

        repo.deleteById(id);
    }

    //비활성
    @Transactional
    public SystemInfoResponse disable(Long id) {
        SystemInfo e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SystemInfo가 없습니다. id=" + id));
        e.setUseYn("N");
        return toRes(e);
    }

    private SystemInfoResponse toRes(SystemInfo e) {
        return SystemInfoResponse.builder()
                .id(e.getId())
                .systemCode(e.getSystemCode())
                .systemName(e.getSystemName())
                .systemGroup(e.getSystemGroup())
                .owner(e.getOwner())
                .version(e.getVersion())
                .status(e.getStatus())
                .useYn(e.getUseYn())
                .remark(e.getRemark())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
