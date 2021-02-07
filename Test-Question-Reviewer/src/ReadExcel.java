import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import com.aspose.cells.Cell;
import com.aspose.cells.ImageOrPrintOptions;
import com.aspose.cells.PageSetup;
import com.aspose.cells.SheetRender;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

public class ReadExcel {

	final Number number=new Number();
	private final String PREVIEW_SUFFIX="_preview";
	private final String IMG_EXTENSION=".png";
	
	//Constructors
	public ReadExcel() {}

	/**
	 * Populate the ArrayList with Question objects.
	 * @param qlist  ArrayList to be fill with Question object
	 * @param file  Excel File to be read in order to collect information to create Question objects
	 * @param page  work sheet number of the Excel workbook to be read
	 * @param typeColumn  String that store which column that contain Question type
	 * @param quesColumn  String that store which column that contain Question statement
	 * @param ansColumn  String that store which column that contain Question answer
	 * @param choiStartColumn  String that store which column that contain Question choices start column
	 * @param choiEndColumn  String that store which column that contain Question choices end column
	 * @param startRow  String that store which column that contain Question start row
	 * @param endRow  String that store which column that contain Question end row
	 * @return   String that stores error message while creating Question object, if any
	 * @throws Exception  if file not found
	 */
	public String fillQuestionList(ArrayList<Question> qlist, File file, int page, 
			String typeColumn, String quesColumn, String ansColumn, String choiStartColumn, 
			String choiEndColumn, String startRow, String endRow) throws Exception {

		String message="";
		int id=1;
		
		//Open the excel file with target page
		String sheetDir=file.getAbsolutePath();
		Workbook book=new Workbook(sheetDir);
		Worksheet sheet = book.getWorksheets().get(page);

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
			
			//Create a Question object
			Question question=new Question();
			question.setId(Integer.toString(id));

			//Get the cell for type, question and answer for current row
			Cell type=sheet.getCells().get(typeColumn+currentRow);
			Cell statement=sheet.getCells().get(quesColumn+currentRow);
			Cell answer=sheet.getCells().get(ansColumn+currentRow);

			//Set the Question object's id, question
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
				message="Invalid cell in row "+currentRow+" column "+typeColumn+": "+qType;
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
			qlist.add(question);
			currentRow++;
			id++;
		}
		return message;
	}

	/**
	 * Create Excel document preview Image file.
	 * @param file  Excel document to be read
	 * @param page  work sheet number of the Excel document
	 * @return  generated preview Image file for the Excel document
	 * @throws Exception    if file not found
	 */
	public File createExcelPreview(File file, int page) throws Exception {

		//Open the excel file with target page
		String sheetDir=file.getAbsolutePath();
		Workbook book=new Workbook(sheetDir);
		Worksheet sheet = book.getWorksheets().get(page);

		//Show row and column header 
		PageSetup pageSetup=sheet.getPageSetup();
		pageSetup.setPrintHeadings(true);

		//Set resolution and limit one page per work sheet 
		ImageOrPrintOptions imgOptions = new ImageOrPrintOptions();
		imgOptions.setVerticalResolution(170);
		imgOptions.setHorizontalResolution(170);
		imgOptions.setOnePagePerSheet(true);

		//Render the sheet with respect to specified image/print options
		SheetRender render = new SheetRender(sheet, imgOptions);
		String sheetPreview="temp.png";		//Name of the temporary image
		render.toImage(0,sheetPreview);

		//Crop image to remove unnecessary side space
		String currentDir=System.getProperty("user.dir")+System.getProperty("file.separator");
		File sheetImg=new File(currentDir+sheetPreview);	
		BufferedImage bufferImg=ImageIO.read(sheetImg);		//Read rendered image
		BufferedImage cropImg=bufferImg.getSubimage(100, 0, 
				bufferImg.getWidth()-170, bufferImg.getHeight());	//Crop image
		String imgFilename=(StringMaster.getFileName(file)+PREVIEW_SUFFIX+IMG_EXTENSION);
		File previewImg=new File(imgFilename);
		ImageIO.write(cropImg, "png", previewImg);			//Create image

		//Delete temporary generate image
		sheetImg.delete();

		return previewImg;
	}
}
