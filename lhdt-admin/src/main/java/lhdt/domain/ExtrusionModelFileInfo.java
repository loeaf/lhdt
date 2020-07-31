package lhdt.domain;

import lombok.*;

@ToString(callSuper = true)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtrusionModelFileInfo extends FileInfo {
	
	// 사용자 아이디
	private String userId;
	
	// 고유번호
	private Long extrusionModelFileInfoId;
	// extrusion model 고유번호
	private Long extrusionModelId;
	// shape 파일 팀 아이디. .shp 파일의 extrusion_model_file_info_id 를 team_id로 함
	private Long extrusionModelFileInfoTeamId;

	// 활성화 유무. Y: 활성화, N: 비활성화
	private String enableYn;

	// shape 파일 인코딩
	private String shapeEncoding;
	// shape file version 정보
	private Integer versionId;
}
