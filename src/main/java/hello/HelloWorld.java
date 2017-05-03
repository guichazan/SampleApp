package hello;

import totalcross.ui.MainWindow;
import totalcross.ui.Toast;
import totalcross.ui.Button;

public class HelloWorld extends MainWindow {
	@Override
    public void initUI() {
		Button greetings = new Button("Greetings");
        Greeter greeter = new Greeter();
        add(greetings, CENTER, CENTER);
        greetings.addPressListener((e) -> {
        	Toast.show(greeter.sayHello(), 1000);
        });
    }
}
