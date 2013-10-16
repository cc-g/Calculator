import javax.swing.JFrame;

public class MainClass {
	public static void main(String[] args) {
		MainFrame o = new MainFrame();
		o.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		o.setSize(384, 384);
		//MainFrameObj.setResizable(false);
		o.setVisible(true);
	}
}
