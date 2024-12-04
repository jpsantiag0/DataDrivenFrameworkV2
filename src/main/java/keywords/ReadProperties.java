package keywords;

import java.io.FileInputStream;
import java.util.Properties;

public class ReadProperties {
	
	public static void  main(String[] args) {
		// Load properties file
		Properties prop = new Properties();
		
		try {
			FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/Project.properties");
			prop.load(fs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(prop.getProperty("browser_name"));
		System.out.println(prop.getProperty("url"));
		System.out.println(prop.getProperty("signIn_login_linkText"));
		
	}
}
