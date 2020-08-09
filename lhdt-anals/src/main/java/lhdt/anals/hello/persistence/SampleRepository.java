package lhdt.anals.hello.persistence;

import lhdt.anals.hello.domain.SubType0;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SampleRepository extends CrudRepository<SubType0, Long> {
    boolean existsByHelloName(String Name);
    SubType0 findByHelloName(String Name);
    List<SubType0> findAllByHelloName(String Name);
}