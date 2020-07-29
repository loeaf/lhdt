/**
 * 
 */
package lhdt.anals.hello.persistence;

import org.springframework.data.repository.CrudRepository;

import lhdt.anals.hello.domain.Hello;

/**
 * jpa
 * @author gravity@daumsoft.com
 *
 */
public interface HelloRepository extends CrudRepository<Hello, Long> {

}
