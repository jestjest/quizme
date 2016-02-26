package quizme.tests;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import quizme.DBConnection;
import quizme.database.QuizResultsTable;

public class QuizResultsDBTest {
	static DBConnection db;
	static QuizResultsTable resultDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		resultDB = new QuizResultsTable(db);
		resultDB.removeResult(1); /* keep replacing result 1 in basic test */
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		Timestamp today = new Timestamp(System.currentTimeMillis());
		int resultid = resultDB.addResult(300, "fakeuser", 90.5, 60, today);
		assertEquals(resultid, 1);
		
		int quizid = resultDB.getQuizID(resultid);
		assertEquals(quizid, 300);
		
		String username = resultDB.getUsername(resultid);
		assertTrue(username.equals("fakeuser"));
		
		double score = resultDB.getScore(resultid);
		assertEquals(score, 90.5, 0.0001);
		
		long time = resultDB.getTime(resultid);
		assertEquals(time, 60);
		
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Timestamp date = resultDB.getDate(resultid);
		String dateString = df.format(date);
		String todayString = df.format(today);
		System.out.println(dateString);
		System.out.println(todayString);
		assertTrue(dateString.equals(todayString));
	}

}