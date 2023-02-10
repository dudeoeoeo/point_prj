package com.kei.reviewservice.common.constant;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Data
public abstract class BaseTimeEntity extends BaseEntity {

    @CreatedDate
    @Column(name = "create_dt", updatable = false)
    private LocalDateTime createDt;

    @LastModifiedDate
    @Column(name = "update_dt", insertable = false)
    private LocalDateTime updateDt;

    @Column(name = "delete_dt", insertable = false)
    private LocalDateTime deleteDt;
}
