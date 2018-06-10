package hibernate.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class for connecting to a jdbc database and containing methods associated with it
 * @deprecated
 */
public class DBConnection {

	private static Connection con;

	/**
	 * method connecting to the database or displaying the error message
	 * @author Marcin Lesniewski
	 * @param database database address to connect to the database
	 * @param user username to connect to the database
	 * @param password password to connect to the database
	 * @return connection connects to the database or displays an error message
	 * @throws SQLException
	 * @deprecated
	 */
	public static Connection connectToDatabase(String database, String user, String password) {
		con = null;
		try {
			con = DriverManager.getConnection(database, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("failed to connect - check your input values - database address, username and password");
		}
		return con;
	}
	
	/**
	 * method that closes the connection to the jdbc database
	 * @author Marcin Lesniewski
	 * @throws SQLException
	 * @deprecated
	 */
	public static void closeConnectToDatabase() throws SQLException {
		con.close();
	}

	/**
	 * method creating a new user in the jdbc database or displaying an error alert
	 * @author Marcin Lesniewski
	 * @param username nickname of the person registering for the chat
	 * @param password password of the person registering for the chat
	 * @return boolean user creation completed successfully or not
	 * @throws SQLException
	 * @deprecated
	 */
	public boolean createUserButton_Click(String username, String password) throws SQLException {
		String sql = "SELECT * FROM USERS WHERE NAME = '" + username + "'";
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				System.out.println("false");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PreparedStatement statement = con.prepareStatement("INSERT INTO USERS (NAME, PASSWORD) VALUES(?,?)");
		statement.setString(1, username);
		statement.setString(2, password);
		statement.executeUpdate();
		System.out.println("true");
		return true;
	}
	
	/**
	 * chat login method if the user exists in the jdbc database
	 * @author Marcin Lesniewski
	 * @param username nickname of the person logging in to the chat
	 * @param password password of the person logging in to the chat
	 * @return login to chat finished successfully or not
	 * @throws SQLException
	 * @deprecated
	 */
	public boolean loginButton_Click(String username, String password) throws SQLException {
		String sql = "SELECT * FROM USERS WHERE NAME = '" + username + "' AND PASSWORD = '" + password + "'";
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				System.out.println("zalogowano");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("niezalogowano");
		return false;
	}
	
	/**
	 * method that adds a message to the jdbc database
	 * @author Marcin Lesniewski
	 * @param user nickname of the person sending the message
	 * @param message the content of the message that someone sends
	 * @param date date of the message being added
	 * @param isEncrypted property that specifies whether the message is encrypted or not
	 * @throws SQLException
	 * @deprecated
	 */
	public void addMessageToDB(String user, String message, Date date, boolean isEncrypted) throws SQLException {
		int count = checkRowTableDB();
		PreparedStatement statement = con.prepareStatement("INSERT INTO CHAT (M_ID, M_USER, M_MESSAGE, M_DATE, M_ENCRYPTED) VALUES(?,?,?,?,?)");
		Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String formatDate = formatter.format(date);
		System.out.println("sqlDate" + date);
		statement.setInt(1, count+1);
		statement.setString(2, user);
		statement.setString(3, message);
		statement.setString(4, formatDate);
		statement.setString(5, isEncrypted ? "T" : "F");
		statement.executeUpdate();
		System.out.println("added message");
	}

	/**
	 * method that returns how many records the jdbc database contains
	 * @author Marcin Lesniewski
	 * @return number of records in the jdbc database
	 * @throws SQLException
	 * @deprecated
	 */
	public int checkRowTableDB() throws SQLException {
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT COUNT(*) AS COUNT FROM CHAT");
		rs.next();
		int count = rs.getInt(1);
		return count;
	}
	
	/**
	 * method that reads the message from the jdbc database
	 * @author Marcin Lesniewski
	 * @param index index number in the table with messages in the jdbc database
	 * @return message type object that is a message read from the jdbc database
	 * @throws SQLException
	 * @throws ParseException
	 * @deprecated
	 */
	public Message loadMessageFromDB(int index) throws SQLException, ParseException {
		PreparedStatement statement = con.prepareStatement("SELECT M_USER, M_MESSAGE, M_DATE, M_ENCRYPTED FROM CHAT WHERE M_ID = ?");
		statement.setInt(1, index);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			String user = rs.getString("M_USER");
			String message = rs.getString("M_MESSAGE");
			String formatDate = rs.getString("M_DATE");
			boolean isEncrypted = rs.getString("M_ENCRYPTED").equals("T") ? true : false;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			//String dateAsString = sdf.format(date);
			Date dateFromString = sdf.parse(formatDate);
			Message tempmessage = new Message(user, message, dateFromString, isEncrypted);
			return tempmessage;
		}
		return null;
	}

}
