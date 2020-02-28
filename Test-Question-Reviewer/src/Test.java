import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class Test extends JFrame {

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTextArea taStatement;
	private JButton btnChoice1;
	private JButton btnChoice2;
	private JButton btnChoice3;
	private JButton btnChoice4;
	private ArrayList<Question> qlist;
	private int qNumber;

	/**
	 * Create the frame.
	 */
	public Test(ArrayList<Question> qlist) {
		
		//Initialized question number and question list
		qNumber=0;
		this.qlist=qlist;
		
		//Create content pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		
		//Create text area for statement
		taStatement = new JTextArea();
		taStatement.setFont(new Font("Monospaced", Font.PLAIN, 25));
		taStatement.setWrapStyleWord(true);
		taStatement.setLineWrap(true);
		
		//Create scroll pane for statement
		scrollPane=new JScrollPane(taStatement,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(15, 0, 795, 372);
		contentPane.add(scrollPane);
		
		//Create list of button for multiple choices
		btnChoice1 = new JButton("");
		btnChoice1.setName("Choice1");
		btnChoice1.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnChoice1.setBounds(15, 401, 795, 81);
		contentPane.add(btnChoice1);
		
		btnChoice2 = new JButton("");
		btnChoice2.setName("Choice2");
		btnChoice2.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnChoice2.setBounds(15, 498, 795, 81);
		contentPane.add(btnChoice2);
		
		btnChoice3 = new JButton("");
		btnChoice3.setName("Choice3");
		btnChoice3.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnChoice3.setBounds(15, 595, 795, 81);
		contentPane.add(btnChoice3);
		
		btnChoice4 = new JButton("");
		btnChoice4.setName("Choice4");
		btnChoice4.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnChoice4.setBounds(15, 692, 795, 81);
		contentPane.add(btnChoice4);
		
		//Set frame operation and position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1354, 862);
		setContentPane(contentPane);
		setVisible(true);
		
		//Create action listener for the button
		ActionListener listener=new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				/*
				JButton button=(JButton) event.getSource();
				String choiceSelected=button.getName();
				System.out.println("You selected "+choiceSelected);
				System.out.println(button.getText());
				*/
				loadQuestion();
			}	
		};
		
		//Add action listener for the buttons
		btnChoice1.addActionListener(listener);
		btnChoice2.addActionListener(listener);
		btnChoice3.addActionListener(listener);
		btnChoice4.addActionListener(listener);
	
		//Load text on the text area and buttons
		loadQuestion();
	}

	public boolean loadQuestion() {
		
		//If still didn't finish all question in question list
		if(qNumber<qlist.size()) {
			
			//Get current question number
			Question question=qlist.get(qNumber);
			
			//Load the text for the statement and multiple choices
			taStatement.setText(question.getQuestion());
			btnChoice1.setText(question.getChoices()[0]);
			btnChoice2.setText(question.getChoices()[1]);
			btnChoice3.setText(question.getChoices()[2]);
			btnChoice4.setText(question.getChoices()[3]);
			
			//Increment the question number
			qNumber++;
			
			return true;
		}
		else {
			return false;
		}
	}
}
