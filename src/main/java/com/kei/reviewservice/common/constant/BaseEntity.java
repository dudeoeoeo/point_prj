package com.kei.reviewservice.common.constant;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Data
public abstract class BaseEntity {

    @CreatedBy
    @Column(name = "create_by", updatable = false)
    private String createBy;

    @LastModifiedBy
    @Column(name = "update_by", insertable = false)
    private String updateBy;

    @Column(name = "delete_by", insertable = false)
    private String deleteBy;

    @Column(name = "delete_yn")
    private Boolean deleteYn = false;
}
