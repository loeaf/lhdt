package lhdt.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * extrusion model 속성
 * @author Cheon JeongDae
 *
 */
@ToString(callSuper = true)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtrusionModelAttribute {
	
	// extrusion model Attribute 고유번호
	private Long extrusionModelAttributeId;
	// extrusion model 고유번호
	private Long extrusionModelId;
	// 속성
	private String attributes;
	// 수정일 
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateDate;
	// 등록일
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime insertDate;
}
