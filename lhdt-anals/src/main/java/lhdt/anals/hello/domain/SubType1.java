package lhdt.anals.hello.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubType1 extends SuperType {
    /**
     * 내용
     */
    @Column(name = "cn2")
    private String cn2;
}
