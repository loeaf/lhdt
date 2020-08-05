package lhdt.persistence;

import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepositoryMapper {
	AttributeRepository getDataAttribute(String buildName);
}
