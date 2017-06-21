package hello;

import hello.HelloTC;
import totalcross.TotalCrossApplication;

public class HelloWorldApplication {
    public static void main(String[] args) {
    	TotalCrossApplication.run(HelloTC.class, "/r", "ACTIVATION_KEY", "/scr", "android", "/fontsize", "20", "/fingertouch");
    }
}
