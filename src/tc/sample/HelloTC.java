package tc.sample;

import java.sql.SQLException;

import totalcross.phone.Dial;
import totalcross.sql.Connection;
import totalcross.sql.DriverManager;
import totalcross.sql.Statement;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.ui.Button;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.ImageControl;
import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.Spacer;
import totalcross.ui.Toast;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;

public class HelloTC extends MainWindow {
	private Edit edName, edBorn, edPhone;
	private Button btInsert, btClear, btDial;
	private Connection dbcon;

	private final int componentH = fmH * 2;

	private final int FLAT_EDGE_MARGIN = (int) (Math.min(Settings.screenHeight,
			Settings.screenWidth) * 0.10);

	public HelloTC() {
		super("Hello TotalCross", NO_BORDER);
		// sets the default user interface style to Android
		// There are others, like
		setUIStyle(Settings.Android);
		// Use font height for adjustments, not pixels
		Settings.uiAdjustmentsBasedOnFontHeight = true;

		// setting back and foreground colors for the app.
		setBackForeColors(Color.getRGB(240, 240, 240), Color.getRGB(50, 50, 50));
		// Change the color for Color class to see the results.
	}

	// Initialize the user interface
	public void initUI() {

		try {

			// add the logo at top
			ImageControl ic = new ImageControl(new Image("img/logo.png"));
			ic.scaleToFit = ic.centerImage = true;
			add(ic, LEFT, PARENTSIZE + 12, FILL, PARENTSIZE + 15);

			// setting font to bold
			Label lbWelcome = new Label("Welcome to your First TotalCross App!");
			lbWelcome.setFont(lbWelcome.getFont().asBold());

			add(lbWelcome, CENTER, AFTER);

			// regular font is not bold
			add(new Label("Name: "), LEFT + FLAT_EDGE_MARGIN, AFTER + 100, FILL
					- FLAT_EDGE_MARGIN, componentH);
			add(edName = new Edit(), SAME, AFTER, SAME, SAME);

			add(new Label("Born Date: "), SAME, AFTER, PREFERRED, SAME);
			add(edBorn = new Edit(), SAME, AFTER, FILL - FLAT_EDGE_MARGIN, SAME);
			edBorn.setMode(Edit.DATE);

			add(new Label("Phone Number: "), LEFT + FLAT_EDGE_MARGIN, AFTER,
					SAME, SAME);

			add(btDial = new Button("Dial"), RIGHT - FLAT_EDGE_MARGIN, AFTER,
					PARENTSIZE + 20, SAME);
			btDial.setEnabled(Settings.platform.equals(Settings.ANDROID)
					|| Settings.isWindowsDevice() || Settings.isIOS());

			add(edPhone = new Edit(), LEFT + FLAT_EDGE_MARGIN, SAME, FIT
					- FLAT_EDGE_MARGIN, SAME);
			edPhone.setKeyboard(Edit.KBD_NUMERIC);

			Spacer sp = new Spacer(0, 0);
			add(sp, CENTER, BOTTOM - 300, PARENTSIZE + 10, componentH);
			add(btInsert = new Button("Insert"), BEFORE, SAME, PARENTSIZE + 40,
					componentH, sp);
			add(btClear = new Button("Clear"), AFTER, SAME, PARENTSIZE + 40,
					componentH, sp);

			// setting colors for buttons...
			btInsert.setBackForeColors(0x049CEE, Color.WHITE);
			btClear.setBackColor(Color.PINK);

			if (Settings.onJavaSE || Settings.platform.equals(Settings.WIN32))
				add(new Label("Press F11 on date/number to open keypad"),
						CENTER, BOTTOM);

			dbcon = DriverManager.getConnection("jdbc:sqlite:"
					+ Convert.appendPath(Settings.appPath, "test.db"));
			Statement st = dbcon.createStatement();
			st.execute("create table if not exists person (name varchar, born datetime, number varchar)");
			st.close();
		} catch (Exception e) {
			MessageBox.showException(e, true);
			exit(0);
		}

	}

	public void onEvent(Event e) {
		try {
			switch (e.type) {
			case ControlEvent.PRESSED:
				if (e.target == btClear) {
					clear();
					showToast("All fields are clear!");
				} else if (e.target == btInsert)
					doInsert();
				else if (e.target == btDial && edPhone.getTrimmedLength() > 0)
					Dial.number(edPhone.getText());
				break;
			}
		} catch (Exception ee) {
			MessageBox.showException(ee, true);
		}
	}

	private void doInsert() throws SQLException, InvalidDateException {
		if (edName.getLength() == 0 || edBorn.getLength() == 0
				|| edPhone.getLength() == 0)
			showToast("Please fill all fields!");
		else {
			String name = edName.getText();
			Date born = new Date(edBorn.getText());
			String phone = edPhone.getText();

			Statement st = dbcon.createStatement();
			st.executeUpdate("insert into person values('" + name + "','"
					+ born.getSQLString() + "','" + phone + "')");
			st.close();
			clear();
			showToast("Data inserted successfully!");
		}
	}

	public void showToast(String message) {
		// fmh is the font size. Let's set the toast size to 3 times font size
		Toast.height = fmH * 3;
		// You can control position of toast too
		Toast.posY = (int) (Control.BOTTOM - 600);

		Toast.show(message, 2000);
	}
}
