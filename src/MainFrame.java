import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;
//import javax.swing.text.DefaultCaret;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextArea textArea1;
	private JTextField textField1;
	private JScrollPane scrollPane1;
	private JPanel p;
	private JRadioButton rb1;
	private JRadioButton rb2;
	private ButtonGroup bg;
	private boolean state = true;
	private JMenuBar mb;
	private JMenu m1;
	private JMenu m2;
	private JMenuItem mi1;
	private JMenuItem mi2;

	public MainFrame() {
		super("cc calc");
		textField1 = new JTextField();
		textArea1 = new JTextArea();
		p = new JPanel(new GridLayout(1,1));
		textArea1.setEditable(false);
		textArea1.setForeground(Color.DARK_GRAY);
		textArea1.setBackground(Color.WHITE);
		textField1.addActionListener(this);
		scrollPane1 = new JScrollPane(textArea1);
		DefaultCaret caret = (DefaultCaret)textArea1.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		caret.setVisible(true);
		bg = new ButtonGroup();
		rb1 = new JRadioButton("expression");
		rb2 = new JRadioButton("result");
		rb1.setSelected(false);
		rb2.setSelected(true);
		bg.add(rb1);
		bg.add(rb2);
		rb1.addActionListener(this);
		rb2.addActionListener(this);
		mb = new JMenuBar();
		m1 = new JMenu("File");
		m2 = new JMenu("Preferences");
		mi1 = new JMenuItem("item1");
		mi2 = new JMenuItem("item2");
		mb.add(m1);
		mb.add(m2);
		m1.add(mi1);
		m1.addSeparator();
		m1.add(mi2);
		m2.add(rb1);
		m2.add(rb2);
		add(scrollPane1, BorderLayout.CENTER);
		add(textField1, BorderLayout.SOUTH);
		p.add(mb);
		add(p, BorderLayout.NORTH);
		//textField1.requestFocus();
	}
	public void actionPerformed(ActionEvent e) {
		try {
			if(e.getSource() == textField1) {
				boolean flt = false;
				String s = textField1.getText();
				s = s.replaceAll("\\s", "");
				if(s.contains("clr")) {
					textArea1.setText("");
					textField1.setText("");
				}
				else {
					if((s.charAt(s.length() - 1)) == 'f') {
						s = s.substring(0, s.length() - 1);
						flt = true;
					}
					RToken rt = new RToken(s);
					Rational r = new Rational(rt.eval());
					if(state) {
						if(flt) {
							textField1.setText(r.convToStringF());
							textArea1.append("> " + s + "\n\t" + r.convToStringF() + "\n\n");
						}
						else {
							textField1.setText(r.convToString());
							textArea1.append("> " + s + "\n\t" + r.convToString() + "\n\n");
						
						}
					}
					else {
						if(flt) {
							textField1.setText(s);
							textArea1.append("> " + s + "\n\t" + r.convToStringF() + "\n\n");
						}
						else {
							textField1.setText(s);
							textArea1.append("> " + s + "\n\t" + r.convToString() + "\n\n");
						}
					}
				}
			}
			else if(e.getSource() == rb1) {
				state = false;
			}
			else if(e.getSource() == rb2) {
				state = true;
			}
		}
		catch(ArithmeticException str) {
			if(str.toString().equals("java.lang.ArithmeticException: zero den")) {
				textArea1.append("\nError: Divide by zero.\n");
			}
			else {
				textArea1.append("\nArithmetic error.\n");
			}
		}
		catch(IllegalArgumentException str) {
			textArea1.append("\nError: Invalid input.\n");
		}
		catch(UnsupportedOperationException str) {
			textArea1.append("\nError: Malformed paranthesis.\n");
		}
		catch(Exception str) {
			textArea1.append("\nUnknown error.\n");
		}
	}
}
