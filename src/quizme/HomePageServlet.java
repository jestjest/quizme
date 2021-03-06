package quizme;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.*;
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
	
	private static final long recentDuration = 30 * 60 * 10000000; // recent mean last 30mins
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
		
		// determine the time after which is considered "recent"
		Timestamp recentTime = new Timestamp(System.currentTimeMillis() - recentDuration );
		
		// recently created quizzes by everyone
		QuizTable quizTable = (QuizTable) getServletContext().getAttribute("quizTable");
		List<QuizLink> allRecentQuizzesCreated = 
				quizTable.getRecentQuizzesCreated( resultNumLimit, recentTime );
		request.setAttribute("allRecentQuizzesCreated", allRecentQuizzesCreated );
		
		// recently created quizzes by this user
		List<QuizLink> myRecentQuizzesCreated  = quizTable.getRecentQuizzesCreated(
				user.getName(), resultNumLimit, recentTime);
		request.setAttribute("myRecentQuizzesCreated", myRecentQuizzesCreated ); 
		
		// popular quizzes
		QuizResultsTable quizResultTable = (QuizResultsTable) 
				getServletContext().getAttribute("quizResultTable");
		List<QuizLink> popularQuizzes = quizResultTable.getPopularQuizzes(
				resultNumLimit, new Timestamp(0) ); // no time constraint.
		request.setAttribute("popularQuizzes", popularQuizzes );
		
		// recently taken quizzes by this user
		List<QuizLink> myRecentQuizzesTaken  = quizResultTable.getRecentQuizzesTaken(
				user.getName(), resultNumLimit, recentTime);
		request.setAttribute("myRecentQuizzesTaken", myRecentQuizzesTaken ); 
		
		// announcements
		AnnouncementsTable announcementsTable = (AnnouncementsTable) 
				getServletContext().getAttribute("announcementsTable");
		List<AnnouncementLink> announcements = announcementsTable.getAllAnnouncementsList();
		request.setAttribute("announcements", announcements );
		
		// achievements
		AchievementsTable achievementsTable = (AchievementsTable) 
				getServletContext().getAttribute("achievementsTable");
		List<AchievementLink> myAchievements = achievementsTable.getAllUserAchievementsLinkList( 
				user.getName() );
		request.setAttribute("myAchievements", myAchievements );
		
		// unseen messages
		MessagesTable messagesTable = (MessagesTable)
				getServletContext().getAttribute("messagesTable");
		List<MessageLink> myUnseenMessages = messagesTable.getAllUnseenMessages( 
				user.getName(), resultNumLimit);
		request.setAttribute("myUnseenMessages", myUnseenMessages );
		
		// Friend activities
		FriendTable friendTable = (FriendTable) getServletContext().getAttribute("friendTable");
		List<String> friendsList = friendTable.friendsList( user.getName() );
		
		List<QuizLink> friendsRecentQuizzesCreated = new ArrayList<QuizLink>();
		List<QuizLink> friendsRecentQuizzesTaken = new ArrayList<QuizLink>();
		List<AchievementLink> friendsRecentAchievements = new ArrayList<AchievementLink>();

		for ( String username: friendsList ) {
			friendsRecentQuizzesCreated.addAll( 
					quizTable.getRecentQuizzesCreated(username, resultNumLimit, recentTime));
			friendsRecentQuizzesTaken.addAll( 
					quizResultTable.getRecentQuizzesTaken(username, resultNumLimit, recentTime));
			friendsRecentAchievements.addAll( 
					achievementsTable.getRecentUserAchievements(username, resultNumLimit, recentTime) );
		}
		
		request.setAttribute("friendsRecentQuizzesCreated", friendsRecentQuizzesCreated );
		request.setAttribute("friendsRecentQuizzesTaken", friendsRecentQuizzesTaken );
		request.setAttribute("friendsRecentAchievements", friendsRecentAchievements );

		request.getRequestDispatcher("home.jsp").forward(request, response);
	}


}
