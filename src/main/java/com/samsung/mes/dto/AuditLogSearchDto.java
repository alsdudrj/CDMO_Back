package com.samsung.mes.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditLogSearchDto {

    private String keyword;
    private String action;
    private String startDate;
    private String endDate;

}