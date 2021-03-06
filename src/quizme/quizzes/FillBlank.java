package quizme.quizzes;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.jsp.JspWriter;

public class FillBlank extends Question {

	/**
	 * A string showing the question text before blank.
	 */
	private String leftText;

	/**
	 * A string showing the question text after blank.
	 */
	private String rightText;

	/**
	 * Unprocessed string question.
	 * It should include the "_" determining a blank space.
	 */
	private String questionText;

	/**
	 * A string showing the response text.
	 */
	private String responseText;

	/**
	 * A list that stores all correct text answers.
	 */
	private List<String> correctAnswers;
	
	/**
	 * Lower-case correct answers
	 */
	private List<String> correctAnswersLowerCase;
	
	/**
	 * A String that stores the correct response text.
	 */
	private String correctResponseText;

	/**
	 * List of column names in the corresponding data base.
	 */
	private static final String[] columnNames = {
			"quizid",
			"questionOrder",
			"preQuestion",
			"postQuestion",
			"correctAnswers",
			"preferredAnswer"
	};

	/**
	 * List of column types in the corresponding data base.
	 */
	private static final String[] columnTypes = {
			"INT",
			"INT",
			"TEXT",
			"TEXT",
			"TEXT",
			"INT"
	};

	/**
	 * The type of this question.
	 */
	private static final TYPE type = TYPE.BLANK;

	/**
	 * The maximum achievable points of this question.
	 */
	private static final int maxPoints = 1;

	/**
	 * Constructor: create an instance of a question using one row
	 * of the corresponding data base.
	 * @param rs a ResultSet object pointing to a row in the table
	 * @throws SQLException
	 */
	public FillBlank( ResultSet rs ) throws SQLException {
		super( rs );
		quizID = rs.getInt( columnNames[0] );
		order = rs.getInt( columnNames[1] );
		leftText = rs.getString( columnNames[2] );
		rightText = rs.getString(columnNames[3]);
		String answers = rs.getString( columnNames[4] );
		correctAnswers = Arrays.asList(answers.split("\\s*~~~\\s*"));
		correctAnswersLowerCase = new ArrayList<String>();
		for ( String str: correctAnswers ) {
			correctAnswersLowerCase.add(str.toLowerCase());
		}
		int preferredAnswer = rs.getInt(columnNames[5]);
		correctResponseText = correctAnswers.get(preferredAnswer);
		responseText = "";
	}

	/**
	 * Constructor for debugging.
	 * @param QID
	 * @param ord
	 * @param QT
	 * @param CRT
	 */
	public FillBlank( int QID, int ord, String QT, List<String> answers, String CRT) {
		quizID = QID;
		order = ord;
		questionText = QT;
		correctAnswers = answers;
		correctResponseText = CRT;
		responseText = "";
		parseText();
	}
	
	@Override
	public void show( JspWriter out, int questionIndex ) throws IOException {
		out.append("<b>");
		out.append(leftText);
		out.append("</b>");
		out.append("<input type='text' name='response_" + questionIndex + "_0'>");
		out.append("<b>");
		out.append(rightText);
		out.append("</b>");
	}

	@Override
	public void answerSummary( JspWriter out, int questionIndex) throws IOException  {
		if (points == maxPoints) {
			out.append("<p>Good job! You got it right!</p>");
		} else {
			out.append("<p>Looks like you didn't get question " + questionIndex + " completely right.</p>");
		}
		
		out.append("<b>Question " + questionIndex + ": </b>");
		out.append("Question filled with preferred answer: <br>");
		out.append(leftText+" <b>"+correctResponseText+"</b> " + rightText);
		out.append("<br>");
		out.append("<b>Your answer: </b>");
		out.append(responseText);
		out.append("<br>");
		out.append("<b>Points: </b>");
		out.append( Integer.toString(points) + " out of " + maxPoints);
		out.append("<br>");
	}

	/**
	 * This function use [questionText] to find the 
	 * [leftText] and [rightText].
	 */
	private void parseText() {
		String[] output = questionText.split("_");
		switch ( output.length ) {
		case 0:
			leftText = "";
			rightText = "";
			break;
		case 1:
			leftText = output[0];
			rightText = "";
			break;
		default:
			leftText = output[0];
			rightText = output[1];				
		}
	}

	@Override
	public String[] columnNames() {
		return columnNames;
	}

	@Override
	public String[] columnTypes() {
		return columnTypes;
	}

	@Override
	public TYPE type() {
		return type;
	}

	@Override
	public int maxPoints() {
		return maxPoints;
	}

	@Override
	public void setResponse(String response) {
		responseText = response;
		if ( correctAnswersLowerCase.contains( responseText.toLowerCase() ) ) {
			points = 1;
		}
		else {
			points = 0;
		}
		
	}
}
