package hello;

import hello.HelloWorld;
import totalcross.TotalCrossApplication;

public class HelloWorldApplication {
	public static void main(String[] args) {
		String tcKey = System.getenv("TOTALCROSS_KEY");
		TotalCrossApplication.run(HelloWorld.class, "/r", tcKey, "/scr", "android", "/fontsize", "20", "/fingertouch");
	}
}
