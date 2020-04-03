import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Readexcel {
	
	private File file;
	private FileInputStream fi;
	private HSSFWorkbook wordbook;
	private HSSFSheet sheet;
	
	//Constructors
	public Readexcel() {}
	
	public Readexcel(File file) throws IOException {
		
		//Read excel file at sheet 0
		this.file=file;
		fi=new FileInputStream(file);
		wordbook=new HSSFWorkbook(fi);
		sheet=wordbook.getSheetAt(0);
	}
	
	public ArrayList<Question> createQuestionList(int idColumn, int typeColumn, int quesColumn, int ansColumn, int choiStartColumn, int choiEndColumn, int startRow) throws IOException {

		//ArrayList to store list of Question object
		ArrayList<Question> qList=new ArrayList<Question>();
		
		//Add all possible input values for single answer type set
		Set<String> singleAnsType=new HashSet<String>();
 		singleAnsType.add("single answer");
		singleAnsType.add("true/false");
		singleAnsType.add("单选题");
		singleAnsType.add("判断题");

		//Add all possible input values for single answer type set
		Set<String> multiAnsType=new HashSet<String>();
		multiAnsType.add("multiple answer");
		multiAnsType.add("多选题");
		
		//Get the first working row and last row
		int lastRow=sheet.getLastRowNum();
		int rowNum=startRow-1;		//-1 for current working row since exel starts with 0
		
		//Read current row until the last row
		while(rowNum<=lastRow) {
			
			//Create a Question object
			Question question=new Question();
			
			//Get the cell of id, type, question and answer for current row
			Cell id=sheet.getRow(rowNum).getCell(idColumn);
			Cell type=sheet.getRow(rowNum).getCell(typeColumn);
			Cell statement=sheet.getRow(rowNum).getCell(quesColumn);
			Cell answer=sheet.getRow(rowNum).getCell(ansColumn);
			
			//Set the Question object's id, question
			question.setId((int)id.getNumericCellValue());
			question.setStatement(statement.getStringCellValue());
			
			//Set the Question object's type
			String qType=type.getStringCellValue().toLowerCase();
			if(singleAnsType.contains(qType)) {
				question.setType("single answer");
			}
			else if(multiAnsType.contains(qType)){
				question.setType("multi answer");
			}
			//Report error if no match type found
			else {
				rowNum+=1;
				JOptionPane.showMessageDialog(null, "Invalid cell in row "+rowNum+" column "
				+typeColumn+": "+qType,"Invalid Input", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			
			//Set the Question object's answer
			Set<String> answers=new HashSet<String>();
			String qAnswer=answer.getStringCellValue().trim();
			
			//Add all correct answers into the answers set
			for(int i=0;i<qAnswer.length();i++) {
				answers.add(qAnswer.substring(i, i+1));
			}
			question.setAnswer(answers);
			
			//Set the Question object's multiple choices
			ArrayList<String> choices=new ArrayList<String>();
			int numOfChoice=choiEndColumn-choiStartColumn+1;
			for(int i=0;i<numOfChoice;i++) {
				Cell nextChoice=sheet.getRow(rowNum).getCell(choiStartColumn+i);
				String choice=nextChoice.toString();
				
				//Only add the choice if the choice is not empty
				if(!(choice.isEmpty())) {
					choices.add(choice);
				}
			}
			question.setChoices(choices);
			
			//Add the Question object into the question list and increment the question number
			qList.add(question);
			rowNum++;
		}
		
		//Close the wordbook and file reader
		wordbook.close();
		fi.close();
		
		return qList;
	}
}
