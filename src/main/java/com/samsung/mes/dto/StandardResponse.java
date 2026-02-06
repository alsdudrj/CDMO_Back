package com.samsung.mes.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StandardResponse {
    private Long id;

    private String stdCode;
    private String stdName;
    private String stdGroup;

    private String unit;
    private String useYn;

    private String remark;

    private LocalDateTime updatedAt;
}
