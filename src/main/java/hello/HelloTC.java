package hello;

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
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;

/**
 * Simple example to help you create your own app with TotalCross.
 * 
 * If you need more help, reach us at www.totalcross.com
 * 
 * You can find the full explanation of this sample at
 * http://www.totalcross.com/documentation/getting_started.html
 */
public class HelloTC extends MainWindow {
	
   private Edit edName, edBorn, edPhone;
   private Connection dbcon;

   // fmH is the font size. We will set the components to have twice the size
   // of font.
   private final int COMPONENT_H = FONTSIZE+200;

   public TCTestWin() {
      super("Hello TotalCross", NO_BORDER);
      setUIStyle(Settings.MATERIAL_UI);
      Settings.uiAdjustmentsBasedOnFontHeight = true;

      setBackForeColors(0xF0F0F0, Color.getRGB(50, 50, 50)); // two ways to get a color value; first one is better
   }

   @Override
   public void initUI() {
      try {
         setInsets(50,50,50,50);
         
         // setup Toast once
         Toast.height = fmH * 3; // fmh is the font size. Let's set the toast size to 3 times font size
         Toast.posY = (int) (Control.BOTTOM - 600); // You can control position of toast too

         // first define all Control properties, then add to the parent
         ImageControl ic = new ImageControl(new Image("hello/logo.png"));
         ic.scaleToFit = ic.centerImage = true;
         add(ic, LEFT, PARENTSIZE + 12, FILL, PARENTSIZE + 15);

         // setting font to bold
         add(new Label("Welcome to your First TotalCross App!", CENTER, Color.BLACK, true), CENTER, AFTER);

         // regular font is not bold
         edName = new Edit();
         edName.caption = "Name";
         add(edName, LEFT, AFTER + 100, FILL, PREFERRED);

         edBorn = new Edit();
         edBorn.setMode(Edit.DATE);
         edBorn.caption = "Born Date";
         add(edBorn, SAME, AFTER, FILL, SAME);
         
         Button btDial;

         add(btDial = new Button("Dial"), RIGHT, AFTER, PARENTSIZE + 20, SAME);
         btDial.setEnabled(Settings.platform.equals(Settings.ANDROID) || Settings.isWindowsDevice() || Settings.isIOS());
         btDial.addPressListener((event) -> {
            if (edPhone.getTrimmedLength() > 0) {
               try {
                  Dial.number(edPhone.getText());
               } catch (Exception ee) {
                  MessageBox.showException(ee, true);
               }
            }
         });

         edPhone = new Edit();
         edPhone.caption = "Phone Number";
         add(edPhone, LEFT, SAME, FIT, SAME);

         add(new Spacer(0, 0), CENTER, BOTTOM - 400, PARENTSIZE + 10, COMPONENT_H);
         
         Button btInsert = new Button("Insert");
         btInsert.setBackForeColors(0x049CEE, Color.WHITE);
         add(btInsert, CENTER, SAME, PARENTSIZE + 90, COMPONENT_H);

         // setting colors for buttons...
         btInsert.addPressListener((event) -> {
            try {
               doInsert();
            } catch (Exception ee) {
               MessageBox.showException(ee, true);
            }
         });
         
         Button btClear = new Button("Clear");
         btClear.setBackColor(Color.PINK);
         add(btClear, CENTER, AFTER, SAME, SAME);
         
         btClear.addPressListener((e) -> {
            clear();
            showToast("All fields are cleared");
         });

         if (Settings.onJavaSE || Settings.platform.equals(Settings.WIN32))
            add(new Label("Press F11 on date/number to open keypad"), CENTER, BOTTOM);

         dbcon = DriverManager.getConnection("jdbc:sqlite:" + Convert.appendPath(Settings.appPath, "test.db"));
         Statement st = dbcon.createStatement();
         st.execute("create table if not exists person (name varchar, born datetime, number varchar)");
         st.close();
      } catch (Exception e) {
         MessageBox.showException(e, true);
         exit(0);
      }
   }

   private void doInsert() throws SQLException, InvalidDateException {
      if (edName.getLength() == 0 || edBorn.getLength() == 0 || edPhone.getLength() == 0)
         showToast("Please fill all fields!");
      else {
         // simple example of how you can insert data into SQLite..
         String name = edName.getText();
         Date born = new Date(edBorn.getText());
         String phone = edPhone.getText();

         Statement st = dbcon.createStatement();
         st.executeUpdate("insert into person values('" + name + "','" + born.getSQLString() + "','" + phone + "')");
         st.close();
         clear();
         showToast("Data inserted successfully!");
      }
   }

   public void showToast(String message) {
      Toast.show(message, 2000);
   }
}
