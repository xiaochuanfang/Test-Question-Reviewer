import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Font;

public class Result extends JFrame {

	/**
	 * Create the frame.
	 */
	public Result(ArrayList<Score> slist) {
		
		//Create content pane
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		
		//Create text area to show result
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 25));
		textArea.append(calculateScore(slist));
		
		//Create scroll pane for the text area
		JScrollPane scrollPane=new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(15, 0, 795, 372);
		contentPane.add(scrollPane);
		
		//Set frame operation and position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1354, 862);
		setContentPane(contentPane);
		setVisible(true);
	}
	
	public String calculateScore(ArrayList<Score> slist) {
		
		//Count number of correct questions
		double count=0;
		int totalQ=slist.size();
				
		//Store the result to be print
		String result="";
		for(int i=0;i<totalQ;i++) {
			int j=i+1;
			if(slist.get(i).isCorrect()) {
				count++;
			}
			else {
				result=result+"Question "+j+" Answer is "+slist.get(i).getCorrectAns()+"\n";
				result=result+"You selected "+slist.get(i).getSelectAns()+"\n";
			}
		}
		
		//Calculate the score
		double score=count/totalQ*100;
		result=result+"\n";
		
		//Store the score in two decimal place
		Number number=new Number();
		score=number.roundDecimal(score, 2);
		
		//Print the final score
		result=result+"Your score on this test is "+score;
		return result;
	}
}
