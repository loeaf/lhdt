package lhdt.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import lhdt.domain.Issue;

/**
 * 이슈
 * @author jeongdae
 *
 */
@Repository
public interface IssueMapper {
	
	/**
	 * 최근 이슈 목록
	 * @return
	 */
	List<Issue> getListRecentIssue();
}
