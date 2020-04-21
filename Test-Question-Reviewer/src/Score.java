import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Score extends JFrame {

	private JPanel contentPane;
	private ArrayList<Question> qlist;
	
	private final Font font=new Font("Monospaced", Font.PLAIN, 25);

	/**
	 * Create the frame.
	 */
	public Score(JFrame frame,ArrayList<Question> q) {
		
		qlist=q;
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
	
		//Create label to show the score
		JLabel txScore = new JLabel();
		txScore.setBounds(262, 152, 469, 67);
		txScore.setFont(font);
		String score=calculateScore();
		txScore.setText(score);	
		contentPane.add(txScore);
		
		//Create button for user to go to the main menu
		JButton btnMenu = new JButton("Menu");
		btnMenu.setBounds(48, 309, 260, 67);
		btnMenu.setFont(font);
		btnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Menu menu=new Menu();
				frame.dispose();
				dispose();
			}
		});
		contentPane.add(btnMenu);
		
		//Create button for user to review all the test questions
		JButton btnReviewAll = new JButton("Review All");
		btnReviewAll.setFont(new Font("Monospaced", Font.PLAIN, 25));
		btnReviewAll.setBounds(373, 309, 260, 67);
		btnReviewAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Review review=new Review(qlist,false);
				frame.dispose();
				dispose();
			}
		});
		contentPane.add(btnReviewAll);
		
		//Create button for user to review questions that they did wrong
		JButton btnReviewMistake = new JButton("Review Mistakes");
		btnReviewMistake.setBounds(696, 309, 260, 67);
		btnReviewMistake.setFont(font);
		btnReviewMistake.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Review review=new Review(qlist,true);
				frame.dispose();
				dispose();
			}
		});
		contentPane.add(btnReviewMistake);
		
		//Set frame operation and position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1011, 521);
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public String calculateScore() {

		//Count number of correct questions
		double count=0;
		int totalQ=qlist.size();
		for(int i=0;i<totalQ;i++) {
			if(qlist.get(i).isCorrect()) {
				count++;
			}
		}
		
		//Calculate the score
		double score=count/totalQ*100;

		//Store the score in one decimal place
		Number number=new Number();
		score=number.roundDecimal(score, 1);

		//Return the final score
		String result="\n";
		result=result+"Your score on this test is "+score;
		return result;
	}
}
