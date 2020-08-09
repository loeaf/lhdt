/**
 * 
 */
package lhdt.anals.common;

import lhdt.anals.hello.domain.Domain;

import java.util.List;

/**
 * 응용 CRUD 인터페이스
 * @author break8524@daumsoft.com
 * @param <T>
 */
public abstract class CRUDService<T extends Domain> implements CRUDInterface<T> {
    /**
     *  식별자가 존재하는지 확인 후 저장합니다
     * @param vo
     * @return
     */
    public T registByUk(T vo) {
        if(existVoByUk(vo)) {
            return null;
        } else {
            return save(vo);
        }
    }

    /**
     * UK 조건에 부합하는 모든 데이터를 삭제합니다
     * @param vo
     */
    public void deleteAllByUk(T vo) {
        var vos = findAllByUk(vo);
        vos.forEach(p -> deleteByVo(p));
    }

    public T update(T vo) {
        return save(vo);
    }

    public List<T> updateAll(List<T> vos) {
        vos.forEach(p -> save(p));
        return vos;
    }
}
