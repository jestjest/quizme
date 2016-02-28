package quizme;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.*;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import quizme.database.*;
import quizme.links.*;

/**
 * Servlet implementation class HomePageServlet
 */
@WebServlet("/HomePageServlet")
public class HomePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final long recentDuration = 30 * 60 * 1000; // recent mean last 30mins
	private static final int resultNumLimit = 5;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HomePageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// get the user
		User user = (User) request.getSession().getAttribute("user");
		
		// determine the time atter which is considered "recent"
		Calendar calendar = Calendar.getInstance();		
		Timestamp recentTime = new Timestamp( calendar.getTime().getTime() + recentDuration );
		
		// recently created quizzes by everyone
		QuizTable quizTable = (QuizTable) request.getSession().getAttribute("quizTable");
		List<QuizLink> allRecentQuizzesCreated = quizTable.getRecentQuizzesCreated( resultNumLimit, recentTime );
		request.getSession().setAttribute("allRecentQuizzesCreated ", allRecentQuizzesCreated );
		
		// recently created quizzes by this user
		List<QuizLink> myRecentQuizzesCreated  = quizTable.getRecentQuizzesCreated(
				user.getName(), resultNumLimit, recentTime);
		request.getSession().setAttribute("myRecentQuizzesCreated ", myRecentQuizzesCreated ); 
		
		// popular quizzes
		QuizResultsTable quizResultTable = (QuizResultsTable) 
				request.getSession().getAttribute("quizResultTable");
		List<QuizLink> popularQuizzes = quizResultTable.getPopularQuizzes(
				resultNumLimit, new Timestamp(0) ); // no time constraint.
		request.getSession().setAttribute("popularQuizzes ", popularQuizzes );
		
		// recently taken quizzes by this user
		List<QuizLink> myRecentQuizzesTaken  = quizResultTable.getRecentQuizzesTaken(
				user.getName(), resultNumLimit, recentTime);
		request.getSession().setAttribute("myRecentQuizzesTaken ", myRecentQuizzesTaken ); 
		
		// announcements
		AnnouncementsTable announcementsTable = (AnnouncementsTable) 
				request.getSession().getAttribute("announcementsTable");
		LinkedHashMap<Timestamp, String> announcements = announcementsTable.getAllAnnouncementsMap();
		request.getSession().setAttribute("announcements ", announcements );
		
		// achievements
		AchievementsTable achievementsTable = (AchievementsTable) 
				request.getSession().getAttribute("achievementsTable");
		List<AchievementLink> myAchievements = achievementsTable.getAllUserAchievementsLinkList( user.getName() );
		request.getSession().setAttribute("myAchievements ", myAchievements );
		
		// Friend activities
		FriendTable friendTable = (FriendTable) request.getSession().getAttribute("friendTable");
		List<String> friendsList = friendTable.friendsList( user.getName() );
		
		List<QuizLink> friendsRecentQuizzesCreated = new ArrayList<QuizLink>();
		List<QuizLink> friendsRecentQuizzesTaken = new ArrayList<QuizLink>();
		List<AchievementLink> friendsAchievements = new ArrayList<AchievementLink>();

		for ( String username: friendsList ) {
			friendsRecentQuizzesCreated.addAll( 
					quizTable.getRecentQuizzesCreated(username, resultNumLimit, recentTime));
			friendsRecentQuizzesTaken.addAll( 
					quizResultTable.getRecentQuizzesTaken(username, resultNumLimit, recentTime));
			friendsAchievements.addAll( achievementsTable.getAllUserAchievementsLinkList(username) );
		}
		
		request.getSession().setAttribute("friendsRecentQuizzesCreated ", friendsRecentQuizzesCreated );
		request.getSession().setAttribute("friendsRecentQuizzesTaken ", friendsRecentQuizzesTaken );
		request.getSession().setAttribute("friendsAchievements ", friendsAchievements );

		
	}


}