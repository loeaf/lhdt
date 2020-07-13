package lhdt.persistence;

import org.springframework.stereotype.Repository;

import lhdt.domain.AttributeRepository;

@Repository
public interface AttributeRepositoryMapper {
	AttributeRepository getDataAttribute(String buildName);
}
