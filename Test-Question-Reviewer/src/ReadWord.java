import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.aspose.words.Document;
import com.aspose.words.IPageSavingCallback;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.PageRange;
import com.aspose.words.PageSavingArgs;
import com.aspose.words.PageSet;
import com.aspose.words.SaveFormat;

public class ReadWord {

	private static Pattern ansPattern=Pattern.compile("[a-zA-Z]{1,}.*?\n*?\\s*?\\d{1,}\\.");
	private static Pattern lastAnsPattern=Pattern.compile("[a-zA-Z]{1,}.*?\n*?\\s*?");
	private static Pattern quesPattern=Pattern.compile("\\d+\\..*?[:?.]{1}\\s{2,}");

	private static String previewImage="temp";	
	private static String saveFormat=".png";	

	private String[] quesBlocks;				
	private String errorMessage="";	 

	/**
	 * Create word document preview Image file.
	 * Limit pages due to limitation of unlicensed Aspose product. 
	 * @param file  word document to be read
	 * @return  generated preview Image file for the Word document
	 * @throws Exception  if file not found
	 */
	public File createWordPreview(File file) throws Exception{

		//Open the word document
		String wordDir=file.getAbsolutePath();
		Document doc=new Document(wordDir);

		//Save each word document pages into different image files
		ImageSaveOptions options=new ImageSaveOptions(SaveFormat.TIFF);
		options.setHorizontalResolution(170);
		options.setVerticalResolution(170);
		int pageCount=doc.getPageCount();
		PageRange range=new PageRange(0,pageCount-1);
		options.setPageSet(new PageSet(range));
		options.setPageSavingCallback(new HandlePageSavingCallback());
		doc.save(previewImage, options);

		//Concatenate all images 
		String[] imgList=new String[pageCount];
		for(int i=0;i<pageCount;i++) {
			imgList[i]=previewImage+i+saveFormat;
		}
		appendImages(imgList);

		File previewImg=new File(previewImage+saveFormat);

		return previewImg;
	}
	
	/**
	 * Saves separate pages when saving a document to fixed page formats
	 */
	private class HandlePageSavingCallback implements IPageSavingCallback{

		@Override
		public void pageSaving(PageSavingArgs arg0) throws Exception {
			arg0.setPageFileName(String.format(previewImage+arg0.getPageIndex()+saveFormat, arg0.getPageIndex()));
		}

	}

	/**
	 * Concatenate list of images files to a single image.
	 * Images are concatenate from top to bottom order. 
	 * @param imgList  array of images filename to be concatenate
	 * @return  Image file that concatenate from given images 
	 * @throws IOException  if image file not found
	 */
	private static File appendImages(String[] imgList) throws IOException {

		int size=imgList.length;

		//Create files for each image String
		File[] fimgList=new File[size];
		for(int i=0;i<size;i++) {
			fimgList[i]=new File(imgList[i]);
		}

		//Create BufferedImage when reading each image file
		BufferedImage[] bimgList=new BufferedImage[size];
		for(int i=0;i<size;i++) {
			bimgList[i]=ImageIO.read(fimgList[i]);
		}

		//Calculate the final concatenate image width and height
		int width=0;
		int height=0;
		for(int i=0;i<size;i++) {

			//Find the maximum width among all images
			if(bimgList[i].getWidth()>width) {
				width=bimgList[i].getWidth();
			}

			//Find the total height of all images
			height+=bimgList[i].getHeight();
		}

		//Create a placeholder image for the final concatenate image
		BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D gd=image.createGraphics();

		//Append each image in its corresponding page order
		int currHeight=0;
		for(int i=0;i<bimgList.length;i++) {
			BufferedImage temp=bimgList[i];
			gd.drawImage(temp, 0, currHeight, null);
			currHeight+=temp.getHeight();
		}
		gd.dispose();

		//Save the final concatenate image
		File concaImg=new File(previewImage+saveFormat);
		ImageIO.write(image, "png", concaImg);
		
		return concaImg;
	}

	/**
	 * Populate the ArrayList with Question objects.
	 * @param qlist  ArrayList to be fill with Question object
	 * @param file  Word document to be read in order to collect information to create Question objects
	 * @return  String that stores error message while creating Question object, if any
	 * @throws Exception  if file not found
	 */
	public String fillQuestionList(ArrayList<Question> qlist, File file, String ansPrefix) throws Exception {

		String text=extractText(file);

		//Split the text into blocks base on answer index
		quesBlocks=text.split(ansPrefix);

		/*Looping from first block to n-1 block, since the last block contain only 
		 * answer for the last question
		 */
		for(int i=0;i<quesBlocks.length-1;i++) {
			qlist=createQuestion(qlist,i);	
			qlist=addQuestionAns(qlist,i);

			/*If not error after add the answer, then continue add Question's statement
			 * Otherwise return the error message
			 */
			if(errorMessage.isEmpty()) {
				qlist=addQuestionStatement(qlist,i);
			}
			else {
				return errorMessage;
			}

			/*If not error after add the statement, then continue add Question's choices
			 * Otherwise return the error message
			 */
			if(errorMessage.isEmpty()) {
				qlist=addQuestionChoices(qlist,i);			
			}
			else{
				return errorMessage;
			}
		}

		/*
		for(int k=0;k<qlist.size();k++) {
			Question q=qlist.get(k);
			System.out.println("Question "+k+": "+q.getStatement());
			System.out.println("Choice are ");
			for(int l=0;l<q.getChoices().size();l++) {
				System.out.println(l+": "+q.getChoices().get(l));
			}
			System.out.println("Answer: "+q.getCorrectAns());
		}*/
		return errorMessage;
	}

	/**
	 * Extract text from a word document. Accept both doc and docx file.
	 * @param file  Word document to be read
	 * @return  String that holds the text from the Word document
	 * @throws IOException  if file not found
	 */
	private String extractText(File file) throws IOException {
		String text="";

		/*Get the word document's directory and extension, extract text base on
		 * its extension, either .doc or .docx
		 */
		String wordDir=file.getAbsolutePath();
		String type=StringMaster.getFileExtension(file);

		FileInputStream fis=new FileInputStream(wordDir);

		if(type.equalsIgnoreCase("doc")) {
			HWPFDocument doc=new HWPFDocument(fis);
			WordExtractor we=new WordExtractor(doc);
			String[] paragraph=we.getParagraphText();
			for(String para:paragraph) {
				text=text+para.toString();
			}
			we.close();
		}
		else if(type.equalsIgnoreCase("docx")) {
			XWPFDocument doc=new XWPFDocument(fis);
			List<XWPFParagraph> paragraph=doc.getParagraphs();
			for(XWPFParagraph para:paragraph) {
				text=text+"  "+para.getText();
			}
			doc.close();
		}
		fis.close();

		return text;
	}

	/**
	 * Create Question object and its ID and add to Question object List.
	 * @param qlist  ArrayList that holds the Question object
	 * @param id  integer that represent Question object's ID
	 * @return  ArrayList that holds the Question object
	 */
	private ArrayList<Question> createQuestion(ArrayList<Question> qlist, int id) {

		Question ques=new Question();
		ques.setId(String.valueOf(id));
		qlist.add(ques);

		return qlist;
	}

	/**
	 * Add a Question object's answer. Question objects are store in ArrayList.
	 * @param qlist  ArrayList that holds the Question object
	 * @param id  integer that represent Question object's ID
	 * @return  ArrayList that holds the Question objects
	 */
	private ArrayList<Question> addQuestionAns(ArrayList<Question> qlist, int id){

		//Extract answer from the next block since answer is in the next block
		int j=id+1;

		Set<String> ansSet=scrapeAnswer(j);
		if(ansSet.isEmpty()) {
			writeErrorMessage("answer",j);
		}
		else {
			qlist.get(id).setCorrectAns(ansSet);
		}
		return qlist;
	}

	/**
	 * Add Question object's statement. Question objects are store in ArrayList.
	 * @param qlist  ArrayList that holds the Question object
	 * @param id  integer that represent Question object's ID
	 * @return  ArrayList that holds the Question objects
	 */
	private ArrayList<Question> addQuestionStatement(ArrayList<Question> qlist, int id) {

		Question ques=qlist.get(id);

		//Extract the Question statement from the #id text block
		String statement=scrapeStatement(id);
		if(!statement.isEmpty()) {
			ques.setStatement(statement);
		}

		//If can't find the question, write an error errorMessage
		else {
			id++;
			writeErrorMessage("question",id);
		}
		return qlist;
	}

	/**
	 * Add Question object's choices. Question objects are store in ArrayList. 
	 * Choices are store in ArrayList.
	 * @param qlist  ArrayList that holds the Question object
	 * @param id  integer that represent Question object's ID
	 * @return  ArrayList that holds the Question objects
	 */
	private ArrayList<Question> addQuestionChoices(ArrayList<Question> qlist, int id) {

		String block=quesBlocks[id];

		//Create ArrayList instance to store the choices
		ArrayList<String> choicesList=new ArrayList<String>();

		//Create character for first choice and next choice
		char currLetter='a';
		char nextLetter='b';

		//Create start and end index for the beginning and the end of the choice
		int startIndex=0;
		int endIndex=block.indexOf(currLetter+".");

		//Looping while the next choice is available
		while(block.indexOf(nextLetter+".")!=-1 && isValidChoice(block,block.indexOf(nextLetter+"."))) {

			nextLetter=(char) (currLetter+1);

			/* Update the start and end index (a become b, b become c) 
			 * Search for String between first choice and second choice inclusive
			 * Example: a. XXX b. (result will has a. but not b.)
			 */
			startIndex=endIndex;
			endIndex=block.indexOf(nextLetter+".");

			/* Validate the start index, if not qualify write an error errorMessage
			 * Start index will not qualify only if the first choice 'a' is missing
			 * since start choice index is from the next choice index
			 */
			if(!isValidChoice(block,startIndex)) {
				id++;
				writeErrorMessage("choices",id);
				return qlist;
			}

			/* Validate the end index, if not validate, either 
			 * 1. Not found (return -1), since there is no more next choice, then jump to add 
			 * the last choice
			 * 2. Not the end of String, continue to look for qualify index
			 */
			while(!isValidChoice(block,endIndex)) {
				if(endIndex==-1) {
					break;
				}
				else {
					endIndex=block.indexOf(nextLetter+".",endIndex+1);
				}
			}

			//If both start and end index are validated, scrape the choice, and go to next choice
			if(endIndex!=-1) {
				choicesList.add(scrapeChoice(id,startIndex,endIndex,false));					
				currLetter=nextLetter;
			}
		}

		/*Before adding the last choice, if no other choice had added, write an error errorMessage
		 *since multiple choices question cannot has only one choice 
		 */
		if(choicesList.size()==0) {
			id++;
			writeErrorMessage("choices",id);
			return qlist;
		}

		//Add the last choice and set the Question object's choices list
		String lastChoice=scrapeChoice(id,startIndex,endIndex,true);

		//If the last choice contains the next question statement, write an error message
		Matcher ansMatcher=ansPattern.matcher(lastChoice);
		if(ansMatcher.find()) {
			id++;
			writeErrorMessage("answer",id);
			return qlist;
		}
		else {
			choicesList.add(lastChoice);
		}

		qlist.get(id).setChoices(choicesList);

		return qlist;
	}

	/**
	 * Extract answer from the text block. Block number is base on id.
	 * ansPattern: any one alphabet with or without any character with or without
	 * any new line with or without by any space follow by a digit follow by a period
	 * lastAnsPattern: any one alphabet with or without any character with or without
	 * any new line with or without by any space 
	 * @param id  integer indicates which Question object and which text block
	 * @return  Set of String of extracted correct choices  
	 */
	private Set<String> scrapeAnswer(int id) {
		
		String nextBlock=quesBlocks[id];
		
		//Create Set to store correct answer letter(s)
		Set<String> ansSet=new HashSet<String>();

		/*If matcher find, remove last two characters (which are question # for 
		 * the next question), save as answer and remove the answer out from the block
		 * for further choices extraction
		 */
		Matcher ansMatcher=ansPattern.matcher(nextBlock);
		Matcher lastAnsMatcher=lastAnsPattern.matcher(nextBlock);
		String ansSentence="";
		if(ansMatcher.find()) {
			ansSentence=ansMatcher.group();
			String ansPart=ansSentence.substring(0,ansSentence.length()-2);
			quesBlocks[id]=quesBlocks[id].replace(ansPart, "");
		}

		/*If it's the last question answer, which contain only answer and no
		 * question number, Remove any leading and trailing space and save it as answer
		 */
		else if(lastAnsMatcher.find()) {
			ansSentence=StringMaster.removeFrontAndBackSpace(nextBlock);
		}

		//If can't find answer in the block, write an error errorMessage
		else {
			return ansSet;
		}

		//Extract the choice letter(s)
		String[] ansChunk=ansSentence.split(" ");
		String answer=ansChunk[0].replace(".", "");

		//Add all correct answers into the answers set
		for(int k=0;k<answer.length();k++) {
			String choice=answer.substring(k,k+1);
			if(!StringMaster.isEmptyString(choice)) {
				ansSet.add(choice);
			}
		}

		return ansSet;
	}


	/**
	 * Extract Statement from the text block. Block number is base on id.
	 * quesPattern: one digit follow by dot follow by any character (reluctant mode)
	 * follow by a colon or a question mark or a period follow by two or more space
	 * @param id  integer indicates which Question object and which text block
	 * @return  String of extracted statement
	 */
	private String scrapeStatement(int id) {
		String block=quesBlocks[id];
		String statement="";

		/*If question matcher find, remove the question # from front and choice letter
		 * and save it as question statement 
		 */
		Matcher quesMatcher=quesPattern.matcher(block);
		if(quesMatcher.find()) {
			statement=(quesMatcher.group());
			statement=statement.substring(statement.indexOf(" ")+1, statement.length()-2);
		}

		return statement;
	}

	/**
	 * Extract choices from the text block. Block number is base on id.
	 * @param id  integer indicates which Question object and which text block
	 * @param startIndex  integer indicates the start index of a choice 
	 * @param endIndex  integer indicates the end index of a choice
	 * @param isLastChoice  flag indicates whether the scraping choice is the last choice
	 * @return  String of the extracted choice
	 */
	private String scrapeChoice(int id, int startIndex, int endIndex, boolean isLastChoice) {
		String block=quesBlocks[id];
		String choice="";

		//If it's the last choice, exclude the choice letter and scrape the choice till the end of String
		if(isLastChoice) {
			choice=block.substring(startIndex+2);
		}

		/*If it's not the last choice, exclude the choice letter and scrape the choice in between the two
		 * choice letters
		 */
		else {
			choice=block.substring(startIndex+2,endIndex);
		}

		//Remove any leading and trailing space from the choice and add to choices list
		choice=StringMaster.removeFrontAndBackSpace(choice);

		return choice;
	}

	/**
	 * Record any error on a String while extracting information from the document. 
	 * @param errorType  String value of scraping error. Either "Statement", "Choice" or "Answer"
	 * @param id  integer value indicate which question did the scraping error occur
	 */
	private void writeErrorMessage(String errorType, int id) {
		errorMessage="Error while extracting "+errorType+" for question "+id;
	}

	/**
	 * Check whether the choice letter is exist and valid.  
	 * @param block  String value of text block where the choice letter is scrape on
	 * @param index  integer value for the position of choice on the block 
	 * @return  false if can't the index in the block, or it's not a valid choice letter.
	 * 			True otherwise.
	 */
	private boolean isValidChoice(String block, int index) {

		//If the index cannot be found, then it's not a valid index
		if(index==-1) {
			return false;
		}

		/*Check if the String before the choice letter is space or line feed
		 * To avoid choice description also contain choice letter
		 * Example: a. I had join a club.   b. I went home.
		 */
		String pre=block.substring(index-1,index);
		if(pre.equals(" ") || pre.equals("\n")) {
			return true;
		}

		//If not, then it's within the choice description, not a true choice letter
		return false;
	}

}
