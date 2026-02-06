package com.samsung.mes.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@SequenceGenerator(name = "seq_member_id", sequenceName = "SEQ_MEMBER_ID", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
    
    //@Column(name = "repeat_password", nullable = false)
    @Transient
    private String repeatPassword;
    
    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "position")
    private String position;

    @Column(name = "tel")
    private String tel;

    @Column(name = "address")
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Company companyId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "provider")
    private String provider; // kakao, google, naver

    @Column(name = "provider_id")
    private String providerId; // 소셜 서비스에서 주는 고유 ID

    @PrePersist
    public void prePersist() {
    	if(createdAt == null) {
    		createdAt = LocalDateTime.now();
    	}
    }
    
}
