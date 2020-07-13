package lhdt.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import lhdt.domain.LayerGroup;

@Repository
public interface LayerGroupMapper {

	/**
     * 레이어 그룹 목록
     * @return
     */
    List<LayerGroup> getListLayerGroup();
}
