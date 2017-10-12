package phuc.controller;

import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import phuc.controller.service.user.IAuthenService;
import phuc.controller.service.user.IProfileService;
import phuc.controller.service.user.ProfileService;
import phuc.entity.Customer;

@RestController
public class MainController {
	
//	@RequestMapping("/test")
//	public String getContent(@RequestParam(value="name", defaultValue="World") String name){
//		System.out.println("Hello: " + name);
//		String a = info.getInfo();
//		return a;		
//	}
//	
//	@RequestMapping("/insert")
//	public String insertTecher(@RequestParam(value="name", defaultValue="Phuc") String name,
//								@RequestParam(value="age", defaultValue="18") String age){		
//		info.insertTeacher(name, age);
//		return "INSERT OK";		
//	}
	
}
