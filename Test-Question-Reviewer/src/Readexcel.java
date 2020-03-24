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
	
	public ArrayList<Question> createQuestionList(int idColumn, int quesColumn, int ansColumn, int choiStartColumn, int choiEndColumn, int startRow) throws IOException {

		//ArrayList to store list of Question object
		ArrayList<Question> qList=new ArrayList<Question>();
		
		int lastRow=sheet.getLastRowNum();
		int rowNum=startRow;
		
		while(rowNum<=lastRow) {
			
			//Get the cell of id, question and answer for current row
			Cell id=sheet.getRow(rowNum).getCell(idColumn);
			Cell startQuestion=sheet.getRow(rowNum).getCell(quesColumn);
			Cell startAnswer=sheet.getRow(rowNum).getCell(ansColumn);
			
			//Create a question object
			Question question=new Question();
			
			//Set the question object's id, question and answer for the question object
			question.setId((int)id.getNumericCellValue());
			question.setStatement(startQuestion.getStringCellValue());
			question.setAnswer(startAnswer.getStringCellValue());
			
			//Set the multiple choices for the question object
			ArrayList<String> choices=new ArrayList<String>();
			int numOfChoice=choiEndColumn-choiStartColumn+1;
			for(int i=0;i<numOfChoice;i++) {
				Cell nextChoice=sheet.getRow(rowNum).getCell(choiStartColumn+i);
				String choice=nextChoice.toString();
				if(!(choice.isEmpty())) {
					choices.add(choice);
				}
			}
			question.setChoices(choices);
			
			//Add the question object into the question list
			qList.add(question);
			rowNum++;
		}
		
		//Close the wordbook and file reader
		wordbook.close();
		fi.close();
		
		return qList;
	}
}
