package lhdt.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lhdt.LhdtUserApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LhdtUserApplication.class)
@Import(RestdocsConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class BaseControllerTest {
	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected MockHttpSession session;
	@Autowired
	protected ObjectMapper objectMapper;
}
