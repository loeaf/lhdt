package lhdt.anals.common;

import java.util.List;

/**
 * 기본 CRUD 인터페이스
 * @author break8524@daumsoft.com
 * @param <T>
 */
public interface CRUDInterface<T> {
    /**
     * VO를 입력합니다
     * @param vo
     * @return
     */
    T save(T vo);

    /**
     * 모든 VO를 획득합니다
     * @return
     */
    List<T> findAll();

    /**
     * Id를 통해 VO를 가져옵니다
     * @param id
     * @return
     */
    T findById(Long id);

    /**
     * Id를 통해 모든 VO를 가져옵니다
     * @param id
     * @return
     */
    List<T> findAllById(Long id);

    /**
     * 식별자에 대한 VO가 있는지 찾습니다
     * @param vo
     * @return
     */
    boolean existVoByUk(T vo);

    T findByUk(T vo);

    List<T> findAllByUk(T vo);

    /**
     * id를 통해 VO를 삭제합니다
     * @param vo
     */
    void deleteByVo(T vo);
}
