package test;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hand.demo.dto.User;
import com.hand.demo.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(locations = { "classpath:spring/test-applicationContext.xml" })
public class Test {
	
	@Autowired
	private IUserService userService;
	
	@org.junit.Test
	public void test() {
		System.out.println("1111111111");
	}
	
	@org.junit.Test
	public void testGetUser() {
		List<User> list = userService.selectUsers();
		for (User user : list) {
			System.out.println(user.getUsername());
		}
	}

}
