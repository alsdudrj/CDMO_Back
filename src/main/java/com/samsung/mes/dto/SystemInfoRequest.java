package com.samsung.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemInfoRequest {
    @NotBlank(message = "systemCode는 필수입니다")
    private String systemCode;

    @NotBlank(message = "systemName은 필수입니다")
    private String systemName;

    @NotBlank(message = "systemGroup은 필수입니다")
    private String systemGroup;

    private String owner;

    private String version;

    @Pattern(regexp = "ACTIVE|INACTIVE|MAINTENANCE", message = "status는 ACTIVE/INACTIVE/MAINTENANCE")
    private String status;

    @Pattern(regexp = "Y|N", message = "useYn은 Y 또는 N")
    private String useYn;

    private String remark;
}
