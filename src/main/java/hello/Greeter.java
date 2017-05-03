package hello;

import totalcross.sys.Settings;

public class Greeter {
    public String sayHello() {
	return "Hello " + Settings.platform + "!";
    }
}
