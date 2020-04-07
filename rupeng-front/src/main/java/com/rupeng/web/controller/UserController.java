package com.rupeng.web.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.Question;
import com.rupeng.pojo.User;
import com.rupeng.service.CardService;
import com.rupeng.service.QuestionsService;
import com.rupeng.service.UserCardsService;
import com.rupeng.service.UsersService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.EmailUtils;
import com.rupeng.util.GetUserInfo;
import com.rupeng.util.ImageCodeUtils;
import com.rupeng.util.SMSUtils;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger logger=LogManager.getLogger(UserController.class);

	@Autowired
	private UsersService userService;
	@Autowired
	private UserCardsService userCardService;
	@Autowired
	private QuestionsService questionService;
	@RequestMapping(value="register.do",method=RequestMethod.GET)
	public String registerdo(){
		return "user/register";
	}
	
	@RequestMapping(value="register.do",method=RequestMethod.POST)
	public String register(String email,String password,String repassword,String emailCode,HttpServletRequest request,Model model){
		//唯一性检查,非空校验，是否已经注册该邮箱
		if(!CommonUtils.isEmpty(email)){
			User user=new User();
			user.setEmail(email);
			int count = userService.count(user);
			if(count>=1){
				model.addAttribute("error", "该邮箱已经注册，请重新输入");
				logger.debug("该邮箱已经注册，请重新输入,email:{}",email);
				return "user/register";
			}
		}
		
		if(!CommonUtils.isEmpty(password)){
			if(CommonUtils.isEmpty(repassword)){
				model.addAttribute("error", "确认密码不能为空，请重新输入");
				logger.debug("确认密码不能为空，请重新输入,password:{}",password);
				return "user/register";
			}
			if(!password.equals(repassword)){
				model.addAttribute("error", "密码输入不一致，请重新输入");
				logger.debug("密码输入不一致，请重新输入,password:{},repassword:{}",password,repassword);
				return "user/register";
			}
		}
		
		if(!CommonUtils.isEmpty(emailCode)){
			int checkEmailCode = EmailUtils.checkEmailCode(request.getSession(), email, emailCode);
			if(checkEmailCode!=1){
				model.addAttribute("error", "注册失败，请重新检查输入是否正确");
				logger.debug("注册失败，请重新检查输入是否正确,emailCode:{}",emailCode);
				return "user/register";
			}
		}
		
		//密码盐
		String salt = UUID.randomUUID().toString();
		String calculateMD5 = CommonUtils.calculateMD5(password+salt);
		
		User frontUser=new User();
		frontUser.setCreateTime(new Date());
		frontUser.setEmail(email);
		frontUser.setIsEmailVerified(true);
		frontUser.setPassword(calculateMD5);
		frontUser.setPasswordSalt(salt);
		userService.insert(frontUser);
		
		//可以把用户名和密码放入到session中，在跳转到登录页面的时候，自动获取
		request.getSession().setAttribute("userEmail",email);
		request.getSession().setAttribute("password", password);
		model.addAttribute("error", "注册成功,请重新登录");
		
		return "user/registerSuccess";
	}
	
	@RequestMapping(value="logout.do",method=RequestMethod.GET)
	public String logout(HttpServletRequest request){
		request.getSession().invalidate();
		return "redirect:/index";
	}
		
	@RequestMapping(value="login.do",method=RequestMethod.GET)
	public String logindo(HttpServletRequest request,Model model){
		String email = (String) request.getSession().getAttribute("userEmail");
		String password = (String) request.getSession().getAttribute("password");
		model.addAttribute("email", email);
		model.addAttribute("password", password);
		
		return "user/login";
	}
	
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	public String login(String loginName,String password,String imageCode,Model model,HttpServletRequest request){
		User user=new User();
		//非空判断，及用户是否存在
		if(!CommonUtils.isEmpty(loginName)){
			user.setEmail(loginName);
			int count = userService.count(user);
			if(count<=0){
				model.addAttribute("message", "密码或登录名错误");
				logger.debug("该邮箱账户不存在,loginName:{}",loginName);
				return "user/login";
			}
		}
		
		if(!CommonUtils.isEmpty(password)){
			User selectOne = userService.selectOne(user);
			String passwordSalt = selectOne.getPasswordSalt();
			String calculateMD5 = CommonUtils.calculateMD5(password+passwordSalt);
			if(!calculateMD5.equals(selectOne.getPassword())){
				model.addAttribute("message", "密码或登录名错误");
				logger.debug("密码错误,password:{}",password);
				return "user/login";
			}
		}
		
		if(CommonUtils.isNotEmpty(imageCode)){
			if(!ImageCodeUtils.checkImageCode(request.getSession(), imageCode)){
				model.addAttribute("message", "验证码错误");
				logger.debug("验证码错误,imageCode:{}",imageCode);
				return "user/login";
			}
		}
		User selectOne = userService.selectOne(user);
		//登录成功
		request.getSession().setAttribute("frontUser",selectOne);

		//	此处用 return "user/userInfo"; 发现个人中心保存信息的时候，发送ajax后重新加载页面，会把login.do的地址刷新一遍，跳到登陆页面
		return "redirect:/user/userInfo.do";
	}
	
	@RequestMapping(value="userInfo.do",method=RequestMethod.GET)
	public String userinforedirect(HttpServletRequest request,Model model){
		User user = (User) request.getSession().getAttribute("frontUser");
		//查出来的user是修改个人信息前的服务器缓存中的user信息，修改后ajax刷新需要获取数据库中最新的数据,更新缓存。
		User selectOne = userService.selectOne(user.getId());
		request.getSession().setAttribute("frontUser",selectOne);
		model.addAttribute("user",selectOne);
		return "user/userInfo";
	}
	
	@RequestMapping(value="userInfo.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult userinfo(User user,HttpServletRequest request){
		User userInsert=new User();
		User frontUser=(User) request.getSession().getAttribute("frontUser");
		userInsert.setEmail(frontUser.getEmail());
		User selectOne = userService.selectOne(userInsert);
		selectOne.setName(user.getName());
		selectOne.setSchool(user.getSchool());
		selectOne.setIsMale(user.getIsMale());
		int update = userService.update(selectOne);
		
		return AjaxResult.successInstance(update==1?"个人信息保存成功":"个人信息保存失败");
	}
	
	@RequestMapping("userPhone.do")
	@ResponseBody
	public AjaxResult userPhone(String phone,String smsCode,HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("frontUser");
		if(CommonUtils.isNotEmpty(phone)){
			user.setPhone(phone);
		}
		
		if(CommonUtils.isNotEmpty(smsCode)){
			SMSUtils.checkSMSCode(request.getSession(), phone, smsCode);
		}
		
		user.setIsPhoneVerified(true);
		userService.update(user);
		return AjaxResult.successInstance("手机号码绑定成功");
	}
	
	@RequestMapping(value="passwordUpdate.do",method=RequestMethod.GET)
	public String passwordUpdatedo(Long id,Model model){
		User selectOne = userService.selectOne(id);
		model.addAttribute("user", selectOne);
		return "user/passwordUpdate";
	}
	
	@RequestMapping(value="passwordUpdate.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult passwordUpdate(Long id,String password,String newpassword,String renewpassword){
		User selectOne =null;
		if(id!=null){
			selectOne = userService.selectOne(id);
		}
		if(CommonUtils.isNotEmpty(password)){
			String passwordSalt = selectOne.getPasswordSalt();
			String calculateMD5 = CommonUtils.calculateMD5(password+passwordSalt);
			if(!calculateMD5.equals(selectOne.getPassword())){
				logger.debug("密码错误，password:{},oldPassword:{}",password,selectOne.getPassword());
				return AjaxResult.errorInstance("密码错误");
			}
		}
		
		if(CommonUtils.isEmpty(newpassword)||CommonUtils.isEmpty(renewpassword)){
			if(!newpassword.equals(renewpassword)){
				logger.debug("新密码两次输入不一致,newpassword:{},renewpassword:{}",newpassword,renewpassword);
				return AjaxResult.errorInstance("新密码两次输入不一致");
			}
		}
		
		selectOne.setPassword(CommonUtils.calculateMD5(password+selectOne.getPasswordSalt()));
		userService.update(selectOne);
		return AjaxResult.successInstance("密码修改成功");
	}
	
	@RequestMapping(value="passwordRetrieve.do",method=RequestMethod.GET)
	public String passwordRetrievedo(){
		return "user/passwordRetrieve";
	}
	@RequestMapping(value="passwordRetrieve.do",method=RequestMethod.POST)
	public String passwordRetrieve(String email,String password,String repassword){
		User selectOne =null;
		if(CommonUtils.isNotEmpty(email)){
			User user=new User();
			user.setEmail(email);
			selectOne = userService.selectList(user).get(0);
			
			if(CommonUtils.isNotEmpty(password)&&CommonUtils.isNotEmpty(repassword)){
				if(password.equals(repassword)){
					selectOne.setPassword(CommonUtils.calculateMD5(password+selectOne.getPasswordSalt()));
					return "user/passwordRetrieveSuccess";
				}
			}
		}
		
		logger.debug("email,password:{},repassword:{}",email,password,repassword);
		return "";
	}
	
	@RequestMapping("studentHome.do")
	public String studentHome(Model model,HttpServletRequest request){
		User  user= (User) request.getSession().getAttribute("frontUser");
		model.addAttribute("user",user );
	    List<Card> selectSecondListByFirstId = userCardService.selectSecondListByFirstId(user.getId());
		model.addAttribute("cardList", selectSecondListByFirstId);
		
		//当前用户所提问的所有的未解决的问题，以及所参与回复的所有的未解决的问题
		List<Question> questionList = questionService.selectUnResolvedQuestionByStudentId(user.getId());
		model.addAttribute("questionList", questionList);
		return "user/studentHome";
	}
	
	@RequestMapping("teacherHome")
	public String teacherHome(HttpServletRequest request,Model model){
		User user = (User) GetUserInfo.getUserAttribute(request);
		if(user.getIsTeacher()==null || user.getIsTeacher()==false){
			model.addAttribute("message", "你不是老师");
			return "message";
		}
		//查询出当前老师所在的所有班级的所有学生所提问的未解决的问题
	List<Question> questionList=questionService.selectUnResolvedQuestionByTeacherId(user.getId());
	model.addAttribute("questionList", questionList);
	return "user/teacherHome";
		
	}
}
