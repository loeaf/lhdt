package gaia3d.controller.view;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import gaia3d.domain.CacheManager;
import gaia3d.domain.DataGroup;
import gaia3d.domain.DataInfo;
import gaia3d.domain.Key;
import gaia3d.domain.PageType;
import gaia3d.domain.Pagination;
import gaia3d.domain.RoleKey;
import gaia3d.domain.SharingType;
import gaia3d.domain.UserPolicy;
import gaia3d.domain.UserSession;
import gaia3d.service.DataGroupService;
import gaia3d.service.DataService;
import gaia3d.service.UserPolicyService;
import gaia3d.support.RoleSupport;
import gaia3d.support.SQLInjectSupport;
import gaia3d.utils.DateUtils;

@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {

	private static final long PAGE_ROWS = 5l;
	private static final long PAGE_LIST_COUNT = 5l;

	@Autowired
	private DataGroupService dataGroupService;
	@Autowired
	private DataService dataService;

	@Autowired
	private UserPolicyService userPolicyService;

	/**
	 * converter job 목록
	 * @param request
	 * @param membership_id
	 * @param pageNo
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/test")
	public String test(	HttpServletRequest request,
						DataInfo dataInfo,
						@RequestParam(defaultValue="1") String pageNo,
						Model model) {

		log.info("@@ TestController list dataInfo = {}, pageNo = {}", dataInfo, pageNo);

		UserSession userSession = (UserSession)request.getSession().getAttribute(Key.USER_SESSION.name());
		
		String roleCheckResult = roleValidator(request, userSession.getUserGroupId(), RoleKey.USER_DATA_READ.name());
		if(roleCheckResult != null) return roleCheckResult;

		UserPolicy userPolicy = userPolicyService.getUserPolicy(userSession.getUserId());
		
		model.addAttribute("userPolicy", userPolicy);

		return "/test/test";
	}

	private String roleValidator(HttpServletRequest request, Integer userGroupId, String roleName) {
		List<String> userGroupRoleKeyList = CacheManager.getUserGroupRoleKeyList(userGroupId);
        if(!RoleSupport.isUserGroupRoleValid(userGroupRoleKeyList, roleName)) {
			log.info("---- Role 이 존재하지 않습니다. 확인 하세요. ");
			request.setAttribute("httpStatusCode", 403);
			return "/error/error";
		}
		return null;
	}
}
