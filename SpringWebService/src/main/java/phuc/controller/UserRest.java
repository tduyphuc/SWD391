package phuc.controller;

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
import phuc.entity.Customer;

@RestController
@RequestMapping(value="user")
public class UserRest {
	@Autowired
	private IAuthenService authen;
	
	@Autowired
	private IProfileService regist;
	
	@RequestMapping(value = "/login", 
			method = RequestMethod.POST)
	public String login(@RequestParam(value="id") String id,
						@RequestParam(value="password") String password){
		
		String s = "WRONG USER";
		if(authen.validateUser(id, password)){
			s = "User OK";
		}
		return s;		
		
	}
	
	private Customer createCustomerFromMap(MultiValueMap<String, String> params){
		Customer cus = new Customer();
		String id = params.getFirst("id");
		String password = params.getFirst("password");
		String firstName = params.getFirst("firstName");
		String lastName = params.getFirst("lastName");
		String address = params.getFirst("address");
		String postal = params.getFirst("postal");
		String country = params.getFirst("country");
		String email = params.getFirst("email");
		String phone = params.getFirst("phone");
		String link = params.getFirst("imageLink");
		
		cus.setCustomerId(id);
		cus.setPassword(password);
		cus.setFirstName(firstName);
		cus.setLastName(lastName);
		cus.setAddress(address);
		cus.setPostalCode(postal);
		cus.setCountry(country);
		cus.setEmail(email);
		cus.setPhone(phone);
		cus.setImageLink(link);
		return cus;
	}
	
	@RequestMapping(value = "/regist", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String regist(@RequestBody MultiValueMap<String, String> params) {
		String id = params.getFirst("id");
		if(regist.checkExistUser(id)){
			return "Exist !!!";
		}
		boolean result = regist.registUser(createCustomerFromMap(params));
		return "RESULT: " + result;		
	}
	
	@RequestMapping(value = "/update", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String update(@RequestBody MultiValueMap<String, String> params) {
		String id = params.getFirst("id");
		if(!regist.checkExistUser(id)){
			return "This user not exist !!!";
		}
		boolean result = regist.updateProfile(createCustomerFromMap(params));
		return "UPDATE: " + result;		
	}
}
