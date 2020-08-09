package lhdt.anals.hello.domain;

import dev.hyunlab.core.vo.PpVO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class Domain extends PpVO {
    /**
     * 안녕 아이디
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @CreationTimestamp
    @Column(name = "regist_datetime")
    protected Date registDt;

    @UpdateTimestamp
    @Column(name = "modified_datetime")
    protected Date updtDt;
}
