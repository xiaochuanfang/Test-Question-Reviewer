import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Readexcel {
	
	//Default constructor
	public Readexcel() {}
	
	public ArrayList<Question> createQuestionList(File file) throws IOException {
		
		//Read excel file at sheet 0
		FileInputStream fi=new FileInputStream(file);
		HSSFWorkbook wordbook=new HSSFWorkbook(fi);
		HSSFSheet sheet=wordbook.getSheetAt(0);
		
		//Set the starting row and ending row
		int startRow=2;
		int lastRow=sheet.getLastRowNum();
		
		//Set the column for each fields
		int idColumn=0;
		int questionColumn=3;
		int answerColumn=8;
		int choiceColumn=4;
		int numOfChoice=4;
		
		//ArrayList to store list of Question object
		ArrayList<Question> qList=new ArrayList<Question>();
		
		int rowNum=startRow;
		while(rowNum<=lastRow) {
			
			//Get the cell of id, question and answer for current row
			Cell id=sheet.getRow(rowNum).getCell(idColumn);
			Cell startQuestion=sheet.getRow(rowNum).getCell(questionColumn);
			Cell startAnswer=sheet.getRow(rowNum).getCell(answerColumn);
			
			//Create a question object
			Question question=new Question();
			
			//Set the question object's id, question and answer for the question object
			question.setId((int)id.getNumericCellValue());
			question.setQuestion(startQuestion.getStringCellValue());
			question.setAnswer(startAnswer.getStringCellValue());
			
			//Set the multiple choices for the question object
			String[] choices=new String[numOfChoice];
			for(int i=0;i<choices.length;i++) {
				Cell nextChoice=sheet.getRow(rowNum).getCell(choiceColumn+i);
				choices[i]=nextChoice.toString();
			}
			question.setChoices(choices);
			
			//Add the question object into the question list
			qList.add(question);
			rowNum++;
		}
		
		//for(int i=0;i<qList.size();i++) {
		for(int i=0;i<3;i++) {
			Question question=qList.get(i);
			System.out.println("Id: "+question.getId());
			System.out.println("Question: "+question.getQuestion()+"");
			for(int j=0;j<question.getChoices().length;j++) {
				System.out.println("Choice "+j+": "+question.getChoices()[j]);
			}
			System.out.println("Answer: "+question.getAnswer());
		}
		
		//Close the wordbook and file reader
		wordbook.close();
		fi.close();
		
		return qList;
	}
}
