package cloudsim;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GuiTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String Data = JOptionPane.showInputDialog(new JFrame(),"Enter Data centers"); 
		int intData = Integer.parseInt(Data);
		
		JOptionPane.showMessageDialog(new JFrame(), intData);
		
	}

}
