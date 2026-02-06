package com.samsung.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemInfoResponse {
    private Long id;
    private String systemCode;
    private String systemName;
    private String systemGroup;

    private String owner;

    private String version;
    private String status;

    private String useYn;
    private String remark;

    private LocalDateTime updatedAt;
}
