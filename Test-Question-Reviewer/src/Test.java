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
	private JButton btnChoice5;
	private ArrayList<JButton> blist;
	private ArrayList<Question> qlist;
	private ArrayList<Score> slist;
	private int qNumber;
	
	private final int MAX_CHOICES=5;

	/**
	 * Create the frame.
	 */
	public Test(ArrayList<Question> qlist) {
		
		//Initialized question number,score list and question list
		qNumber=0;
		blist=new ArrayList<JButton>();
		slist=new ArrayList<Score>();
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
		btnChoice1.setName("A");
		btnChoice1.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice1.setFont(new Font("Monospaced", Font.PLAIN, 25));
		btnChoice1.setBounds(15, 401, 795, 81);
		blist.add(btnChoice1);
		contentPane.add(btnChoice1);
		
		btnChoice2 = new JButton("");
		btnChoice2.setName("B");
		btnChoice2.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice2.setFont(new Font("Monospaced", Font.PLAIN, 25));
		btnChoice2.setBounds(15, 498, 795, 81);
		blist.add(btnChoice2);
		contentPane.add(btnChoice2);
		
		btnChoice3 = new JButton("");
		btnChoice3.setName("C");
		btnChoice3.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice3.setFont(new Font("Monospaced", Font.PLAIN, 25));
		btnChoice3.setBounds(15, 595, 795, 81);
		blist.add(btnChoice3);
		contentPane.add(btnChoice3);
		
		btnChoice4 = new JButton("");
		btnChoice4.setName("D");
		btnChoice4.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice4.setFont(new Font("Monospaced", Font.PLAIN, 25));
		btnChoice4.setBounds(15, 692, 795, 81);
		blist.add(btnChoice4);
		contentPane.add(btnChoice4);
		
		btnChoice5 = new JButton("");
		btnChoice5.setName("E");
		btnChoice5.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice5.setFont(new Font("Monospaced", Font.PLAIN, 25));
		btnChoice5.setBounds(15, 787, 795, 81);
		blist.add(btnChoice5);
		contentPane.add(btnChoice5);
		
		//Set frame operation and position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1530, 932);
		setContentPane(contentPane);
		setVisible(true);
		
		//Create action listener for the button
		ActionListener listener=new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				
				//Record the score after user push the choice button
				recordScore(event);
				
				//Increment the question number
				qNumber++;
				
				//If didn't finish last question in question list, load another question
				if(qNumber<qlist.size()) {
					loadQuestion();
				}
				//Otherwise print the test result
				else {
					Result result=new Result(slist);
					dispose();
					//printScore();
					//System.exit(0);				
				}
			}	
		};
		
		//Add action listener for the buttons
		btnChoice1.addActionListener(listener);
		btnChoice2.addActionListener(listener);
		btnChoice3.addActionListener(listener);
		btnChoice4.addActionListener(listener);
		btnChoice5.addActionListener(listener);
	
		//Load text on the text area and buttons
		loadQuestion();
	}
	
	public ArrayList<Score> recordScore(ActionEvent event){
		
		//Get user's choice
		JButton button=(JButton) event.getSource();
		String choiceSelected=button.getName();
		String answer=qlist.get(qNumber).getAnswer();
		
		//Create the score
		Score score=new Score(qNumber,choiceSelected,answer);
		if(checkAnswer(choiceSelected)) {
			score.setCorrect(true);
		}
		else {
			score.setCorrect(false);
		}
		
		//Add to score list
		slist.add(score);
		return slist;
	}
	
	public boolean checkAnswer(String choiceSelected) {
	
		//Check if user had chose the correct answer
		String answer=qlist.get(qNumber).getAnswer();
		if(choiceSelected.equals(answer)) {
			return true;
		}
		return false;
	}

	public void loadQuestion() {
		
		//Get current question number
		Question question=qlist.get(qNumber);
			
		//Load the text for the statement
		taStatement.setText(question.getStatement());
		
		//Load the button and text for the multiple choices
		int NumOfChoice=question.getChoices().size();
		int count=0;
		while(count!=NumOfChoice) {
			blist.get(count).setVisible(true);
			blist.get(count).setText(question.getChoices().get(count));
			count++;
		}
		for(int i=count;i<MAX_CHOICES;i++) {
			blist.get(i).setVisible(false);
		}
	}
}
