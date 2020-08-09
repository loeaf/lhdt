/**
 * 
 */
package lhdt.anals.hello.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.hyunlab.core.vo.PpVO;
import groovy.lang.Delegate;
import lhdt.anals.hello.types.DefaultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 안녕 
 * @author gravity@daumsoft.com
 *
 */
@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubType0 extends SuperType {
	/**
	 * 안녕 명
	 */
	@Column(name = "hello_name", nullable = false, length = 100)
	private String helloName;
	
	/**
	 * 내용
	 */
	@Column(name = "cn")
	private String cn;

	@Enumerated(EnumType.ORDINAL)
	private DefaultType defaultType;
	/**
	 * 검색조건 -  안녕 명
	 */
	private String searchHelloName;

	@JsonManagedReference
	@OneToMany(mappedBy = "helloId", fetch=FetchType.EAGER, cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<Child> childs = new ArrayList<>();

	public void addChild(Child c) {
		this.childs.add(c);
		if(c.getHelloId() != this)
			c.setHelloId(this);
	}
	public void addChilds(List<Child> c) {
		c.forEach(p -> {
			this.childs.add(p);
			if(p.getHelloId() != this)
				p.setHelloId(this);
		});
	}
}
