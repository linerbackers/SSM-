import org.junit.Test;

import com.rupeng.web.controller.UserController;

public class UserControllerTest {
	
	@Test
	public void test(){
		UserController uc=new UserController();
		uc.passwordRetrieve("479445710@qq.com", "llbbcc-1314", "llbbcc-1314");
	}
}
