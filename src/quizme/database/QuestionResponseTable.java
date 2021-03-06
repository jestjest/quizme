package quizme.database;

import java.sql.*;
import java.util.List;

import quizme.DBConnection;

public class QuestionResponseTable {
	private DBConnection db;
	
	public QuestionResponseTable(DBConnection db) {
		this.db = db;
		createQuestionResponseTable();
	}
	
	
	private void createQuestionResponseTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS questionresponse (quizid INT, questionOrder INT, question TEXT, correctAnswers TEXT, preferredAnswer INT)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String answersToString(List<String> answers) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < answers.size(); i++) {
			sb.append(answers.get(i) + "~~~ ");
		}
		return sb.toString();
	}
	
	public void addQuestion(int quizid, int questionOrder, String question, List<String> correctAnswer, int preferredAnswer) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO questionresponse VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.setString(3, question);
			pstmt.setString(4, answersToString(correctAnswer));
			pstmt.setInt(5, preferredAnswer);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeQuestion(int quizid, int questionOrder) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM questionresponse WHERE quizid = ? AND questionOrder = ?");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeQuizQuestions(int quizid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM questionresponse WHERE quizid = ?");
			pstmt.setInt(1, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet getAllQuizEntries(int quizid) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT * FROM questionresponse WHERE quizid = ?");
			pstmt1.setInt(1, quizid);
			return pstmt1.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
}