package lhdt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = LhdtAgentApplicationTests.class)
class LhdtAgentApplicationTests {

	@Test
	void contextLoads() {
		log.info("테스트!");
	}

}
