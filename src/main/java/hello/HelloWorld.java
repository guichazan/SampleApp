package hello;

import totalcross.ui.MainWindow;
import totalcross.ui.Label;

public class HelloWorld extends MainWindow {

    public void initUI() {
        Greeter greeter = new Greeter();
        add(new Label(greeter.sayHello()), CENTER, CENTER);
    }
}
