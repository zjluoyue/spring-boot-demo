package com.homethy.mls.listing.tools;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.realtors.rets.client.RetsException;
import org.realtors.rets.client.RetsSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HomethyMlsListingToolsApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void loginTest() throws RetsException {
		String loginUrl = "http://retsgw.flexmls.com:80/rets2_3/Login";
		String username = "mt.rets.chime";
		String password = "broom-saur44";

		RetsSession retsSession = new RetsSession(loginUrl);
		retsSession.setMethod("POST");
		try {
			retsSession.login(username, password);
			System.out.println(retsSession.getSessionId());
		} catch (RetsException e) {
			e.printStackTrace();
		} finally {
			if (retsSession != null) {
				retsSession.logout();
			}
		}
	}
}
