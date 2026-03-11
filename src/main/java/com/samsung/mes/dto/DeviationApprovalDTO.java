package com.samsung.mes.dto;

import lombok.Data;

@Data
public class DeviationApprovalDTO {
    private String approverId;
    private String password;
    private String comments; // 필요시 서명 코멘트 추가
}