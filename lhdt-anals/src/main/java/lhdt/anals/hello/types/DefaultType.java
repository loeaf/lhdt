package lhdt.anals.hello.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultType {
    First(0),
    Second(1),
    Third(2);

    private Integer code;
}
