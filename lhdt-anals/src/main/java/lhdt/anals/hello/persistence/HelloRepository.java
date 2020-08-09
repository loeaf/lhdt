/**
 * 
 */
package lhdt.anals.hello.persistence;

import org.springframework.data.repository.CrudRepository;

import lhdt.anals.hello.domain.SubType0;

/**
 * jpa
 * @author gravity@daumsoft.com
 *
 */
public interface HelloRepository extends CrudRepository<SubType0, Long> {

}
