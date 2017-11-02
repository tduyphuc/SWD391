package restcontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import entity.Customer;
import service.user.IAuthenService;
import service.user.IProfileService;
import utils.IJSonHelper;
import utils.ResponseCode;

@RestController
@RequestMapping(value="user")
public class UserRest {
	
	@Autowired
	private IAuthenService authen;
	
	@Autowired
	private IProfileService regist;
	
	@Autowired
	private IJSonHelper json;
	
	@RequestMapping(value = "/login", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String login(@RequestBody MultiValueMap<String, String> params){
		String id = params.getFirst("id");
		String password = params.getFirst("password");
		if(authen.validateUser(id, password)){
			Map<String, String> map = regist.getUserInfo(id);
			return json.toResponseMessage(ResponseCode.OK_CODE, json.convertToJson(map));
		}
		return json.toResponseMessage(ResponseCode.ERROR_CODE_DENIED, "DENIED");		
		
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
			return json.toResponseMessage(ResponseCode.ERROR_CODE_DENIED, "This user has EXISTED !!!");
		}
		boolean result = regist.registUser(createCustomerFromMap(params));
		if(result){
			return json.toResponseMessage(ResponseCode.OK_CODE, "REGIST OK");	
		}
		else{
			return json.toResponseMessage(ResponseCode.ERROR_CODE_DENIED, "REGIST FAILED");
		}
				
	}
	
	@RequestMapping(value = "/update", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String update(@RequestBody MultiValueMap<String, String> params) {
		String id = params.getFirst("id");
		if(!regist.checkExistUser(id)){
			return json.toResponseMessage(ResponseCode.ERROR_CODE_DENIED, "THIS USER NOT EXIST !!!");
		}
		boolean result = regist.updateProfile(createCustomerFromMap(params));
		if(result){
			return json.toResponseMessage(ResponseCode.OK_CODE, "UPDATE OK");	
		}
		else{
			return json.toResponseMessage(ResponseCode.ERROR_CODE_DENIED, "UPDATE FAILED");
		}	
	}
	
	@RequestMapping(value = "/history", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String getHistory(@RequestBody MultiValueMap<String, String> params){
		String id = params.getFirst("id");
		return json.convertToJson(regist.getUserHistory(id));				
	}
	
}
