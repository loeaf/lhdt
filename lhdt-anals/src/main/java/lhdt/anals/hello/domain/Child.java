package lhdt.anals.hello.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

/**
 * Parents Child Test
 * @author gravity@daumsoft.com
 *
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Child extends Domain {
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hello_id")
    private SubType0 helloId;

    @Column(name = "text")
    String text;

}
