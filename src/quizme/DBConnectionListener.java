package quizme;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import quizme.database.*;

/**
 * Application Lifecycle Listener implementation class DBConnectListener
 *
 */
@WebListener
public class DBConnectionListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public DBConnectionListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     * 
     * Initializes a DBConnection instance to be shared by all servlets.
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         DBConnection con = new DBConnection();
         arg0.getServletContext().setAttribute("connection", con);
         
         QuizTable quizTable = new QuizTable(con);
         arg0.getServletContext().setAttribute("quizTable", quizTable);
         
         QuestionResponseTable qrTable = new QuestionResponseTable(con);
         arg0.getServletContext().setAttribute("qrTable", qrTable);
         
         FillInTheBlankTable blankTable = new FillInTheBlankTable(con);
         arg0.getServletContext().setAttribute("blankTable", blankTable);
         
         MultipleChoiceQuestionTable mcTable = new MultipleChoiceQuestionTable(con);
         arg0.getServletContext().setAttribute("mcTable", mcTable);
         
         PictureResponseQuestionTable prTable = new PictureResponseQuestionTable(con);
         arg0.getServletContext().setAttribute("pictureTable", prTable);
         
         MultipleAnswersQuestionTable multipleAnswersTable = new MultipleAnswersQuestionTable(con);
         arg0.getServletContext().setAttribute("multipleAnswersTable", multipleAnswersTable);
         
         TrueFalseQuestionTable trueFalseTable = new TrueFalseQuestionTable(con);
         arg0.getServletContext().setAttribute("trueFalseTable", trueFalseTable);
         
         QuizResultsTable quizResultTable = new QuizResultsTable(con);
         arg0.getServletContext().setAttribute("quizResultTable", quizResultTable);
         
         AnnouncementsTable announcementsTable = new AnnouncementsTable(con);
         arg0.getServletContext().setAttribute("announcementsTable", announcementsTable);
         
         AchievementsTable achievementsTable = new AchievementsTable(con);
         arg0.getServletContext().setAttribute("achievementsTable", achievementsTable);
         
         AchievementGuidelinesTable achievementGuidelinesTable = new AchievementGuidelinesTable(con);
         arg0.getServletContext().setAttribute("achievementGuidelinesTable", achievementGuidelinesTable);
         
         MessagesTable messagesTable = new MessagesTable(con);
         arg0.getServletContext().setAttribute("messagesTable", messagesTable);
         
         FriendTable friendTable = new FriendTable(con);
         arg0.getServletContext().setAttribute("friendTable", friendTable);
         
         UsersTable usersTable = new UsersTable(con);
         arg0.getServletContext().setAttribute("usersTable", usersTable);
         
         FriendRequestTable requestTable = new FriendRequestTable(con);
         arg0.getServletContext().setAttribute("requestTable", requestTable);

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     * 
     * Closes the connection once the context is destroyed.
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         DBConnection con = (DBConnection) arg0.getServletContext().getAttribute("connection");
         con.closeConnection();
    }
	
}
