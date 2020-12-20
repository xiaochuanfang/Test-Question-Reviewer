import java.awt.image.BufferedImage;
import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.aspose.cells.Cell;

import com.aspose.cells.ImageOrPrintOptions;
import com.aspose.cells.PageSetup;
import com.aspose.cells.SheetRender;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

public class Readexcel {

	final Number number=new Number();
	
	//Constructors
	public Readexcel() {}

	public ArrayList<Question> createQuestionList(File file, int page, 
			String typeColumn, String quesColumn, String ansColumn, String choiStartColumn, 
			String choiEndColumn, String startRow, String endRow) throws Exception {

		//Open the excel file with target page
		String sheetDir=file.getAbsolutePath();
		Workbook book=new Workbook(sheetDir);
		Worksheet sheet = book.getWorksheets().get(page);

		//ArrayList to store list of Question object
		ArrayList<Question> qList=new ArrayList<Question>();
		int currentQID=0;

		//Add all possible input values for single answer type set
		Set<String> singleAnsType=new HashSet<String>();
		singleAnsType.add("single answer");
		singleAnsType.add("true/false");
		singleAnsType.add("单选题");
		singleAnsType.add("判断题");

		//Add all possible input values for multiple answers type set
		Set<String> multiAnsType=new HashSet<String>();
		multiAnsType.add("multiple answer");
		multiAnsType.add("多选题");

		//Keep track of current row
		int currentRow=Integer.parseInt(startRow);
		int lastRow=Integer.parseInt(endRow);
		
		//Get the maximum total number of answer choices
		int choiEndColumnInt=number.alphaToInt(choiEndColumn, 1);
		int choiStartColumnInt=number.alphaToInt(choiStartColumn, 1);
		int numOfChoice=choiEndColumnInt-choiStartColumnInt+1;

		//Read current row until the last row
		while(currentRow<=lastRow) {

			currentQID++;

			//Create a Question object
			Question question=new Question();

			//Get the cell for type, question and answer for current row
			Cell type=sheet.getCells().get(typeColumn+currentRow);
			Cell statement=sheet.getCells().get(quesColumn+currentRow);
			Cell answer=sheet.getCells().get(ansColumn+currentRow);

			//Set the Question object's id, question
			question.setId(Integer.toString(currentQID));
			question.setStatement(statement.getStringValue());

			//Set the Question object's type
			String qType=type.getStringValue().toLowerCase();
			if(singleAnsType.contains(qType)) {
				question.setType("single answer");
			}
			else if(multiAnsType.contains(qType)){
				question.setType("multi answer");
			}
			
			//Report error if no match type found
			else {
				currentRow+=1;
				JOptionPane.showMessageDialog(null, "Invalid cell in row "+currentRow+" column "
						+typeColumn+": "+qType,"Invalid Input", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}

			//Set the Question object's answer
			Set<String> answers=new HashSet<String>();
			String qAnswer=answer.getStringValue().trim();

			//Add all correct answers into the answers set
			for(int i=0;i<qAnswer.length();i++) {
				answers.add(qAnswer.substring(i, i+1));
			}
			question.setCorrectAns(answers);

			//Set the Question object's multiple choices
			ArrayList<String> choices=new ArrayList<String>();
			
			for(int i=0;i<numOfChoice;i++) {
				
				//Iterate all the answer choices' string value
				String currentColumn=number.intToAlpha(choiStartColumnInt+i, 1);
				Cell nextChoice=sheet.getCells().get(currentColumn+currentRow);
				String choice=nextChoice.getStringValue();
			
				//Only add the choice if the choice is not empty
				if(!(choice.isEmpty())) {
					choices.add(choice);
				}
			}
			question.setChoices(choices);

			//Add the Question object into the question list and increment the question number
			qList.add(question);
			currentRow++;
		}

		return qList;
	}

	public File createExcelPreview(File file, int page) throws Exception {

		//Open the excel file with target page
		String sheetDir=file.getAbsolutePath();
		Workbook book=new Workbook(sheetDir);
		Worksheet sheet = book.getWorksheets().get(page);

		//Show row and column header 
		PageSetup pageSetup=sheet.getPageSetup();
		pageSetup.setPrintHeadings(true);

		//Define ImageOrPrintOptions
		ImageOrPrintOptions imgOptions = new ImageOrPrintOptions();
		imgOptions.setVerticalResolution(170);			// Set resolution
		imgOptions.setHorizontalResolution(170);
		imgOptions.setOnePagePerSheet(true);

		//imgOptions.setImageFormat(ImageFormat.getJpeg());  //Set desired image extension
		//imgOptions.setDesiredSize(2818, 1754);		//Set desire size

		// Render the sheet with respect to specified image/print options
		SheetRender render = new SheetRender(sheet, imgOptions);
		String sheetPreview="temp.jpg";		//Name of the temporary image
		render.toImage(0,sheetPreview);

		//Crop image to remove unnecessary border from Aspose
		String currentDir=System.getProperty("user.dir")+System.getProperty("file.separator");
		File sheetImg=new File(currentDir+sheetPreview);	
		BufferedImage bufferImg=ImageIO.read(sheetImg);		//Read rendered image
		BufferedImage cropImg=bufferImg.getSubimage(100, 135, bufferImg.getWidth()-170, bufferImg.getHeight()-135);	//Crop image
		File previewImg=new File(currentDir+"Preivew.jpg");	
		ImageIO.write(cropImg, "jpg", previewImg);			//Create image

		//Delete temporary generate image
		sheetImg.delete();

		return previewImg;
	}
}
