package lhdt.service;

import java.util.List;

import lhdt.domain.LayerGroup;

public interface LayerGroupService {

    /**
     * 레이어 그룹 목록 및 하위 레이어 조회
     * @return
     */
    List<LayerGroup> getListLayerGroupAndLayer();

}
