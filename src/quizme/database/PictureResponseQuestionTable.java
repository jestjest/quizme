package quizme.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import quizme.DBConnection;

public class PictureResponseQuestionTable {
private DBConnection db;
	
	public PictureResponseQuestionTable(DBConnection db) {
		this.db = db;
		createPictureResponseQuestionTable();
	}
	
	private void createPictureResponseQuestionTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS pictureresponse (quizid INT, questionOrder INT, question TEXT, correctAnswer TEXT, pictureURL TEXT)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addQuestion(int quizid, int questionOrder, String question, String correctAnswer, String pictureURL) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO pictureresponse VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.setString(3, question);
			pstmt.setString(4, correctAnswer);
			pstmt.setString(5, pictureURL);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeQuestion(int quizid, int questionOrder) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM pictureresponse WHERE quizid = ? AND questionOrder = ?");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getQuizID(int quizid, int questionOrder) {
		return getInt(quizid, questionOrder, "quizid");
	}
	
	public void setQuestionOrder(int quizid, int questionOrder, int newQuestionOrder) {
		setInt(quizid, questionOrder, "questionOrder", newQuestionOrder);
	}
	
	public int getQuestionOrder(int quizid, int questionOrder) {
		return getInt(quizid, questionOrder, "questionOrder");
	}
	
	public void setQuestion(int quizid, int questionOrder, String question) {
		setString(quizid, questionOrder, "question", question);
	}
	
	public String getQuestion(int quizid, int questionOrder) {
		return getString(quizid, questionOrder, "question");
	}
	
	public void setCorrectAnswer(int quizid, int questionOrder, String correctAnswer) {
		setString(quizid, questionOrder, "correctAnswer", correctAnswer);
	}
	
	public String getCorrectAnswer(int quizid, int questionOrder) {
		return getString(quizid, questionOrder, "correctAnswer");
	}
	
	public void setPictureURL(int quizid, int questionOrder, String pictureURL) {
		setString(quizid, questionOrder, "pictureURL", pictureURL);
	}
	
	public String getPictureURL(int quizid, int questionOrder) {
		return getString(quizid, questionOrder, "pictureURL");
	}
	
	public ResultSet getAllQuizEntries(int quizid) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT * FROM pictureresponse WHERE quizid = ?");
			pstmt1.setInt(1, quizid);
			return pstmt1.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
	/* helper functions */
	private void setString(int quizid, int questionOrder, String field, String value) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE pictureresponse SET " + field + " = ? WHERE quizid = ? AND questionOrder = ?");
			pstmt.setString(1, value);
			pstmt.setInt(2, quizid);
			pstmt.setInt(3, questionOrder);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getString(int quizid, int questionOrder, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " +  field + " FROM pictureresponse WHERE quizid = ? AND questionOrder = ?");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
	private void setInt(int quizid, int questionOrder, String field, int value) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE pictureresponse SET " + field + " = ? WHERE quizid = ? AND questionOrder = ?");
			pstmt.setInt(1, value);
			pstmt.setInt(2, quizid);
			pstmt.setInt(3, questionOrder);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int getInt(int quizid, int questionOrder, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " +  field + " FROM pictureresponse WHERE quizid = ? AND questionOrder = ?");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
}