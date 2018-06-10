package hibernate.dao;

import java.util.Date;

/**
 * class of message type objects
 * @deprecated
 */
public class Message {

	private String user;
	private String message;
	private Date date;
	private boolean isEncrypted;

	/**
	 * constructor of message type objects
	 * @author Marcin Lesniewski
	 * @param user nickname of the person creating the message
	 * @param message content of the message
	 * @param date date the message was created
	 * @param isEncrypted property that specifies whether the message is encrypted or not
	 * @deprecated
	 */
	public Message(String user, String message, Date date, boolean isEncrypted) {
		this.user = user;
		this.message = message;
		this.date = date;
		this.isEncrypted = isEncrypted;
	}

	/**
	 * method returning the value of parameter user
	 * @author Marcin Lesniewski
	 * @return nickname of the person creating the message
	 * @deprecated
	 */
	public String getUser() {
		return user;
	}

	/**
	 * method returning the value of parameter message
	 * @author Marcin Lesniewski
	 * @return content of the message
	 * @deprecated
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * method returning the value of parameter date
	 * @author Marcin Lesniewski
	 * @return date the message was created
	 * @deprecated
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * method returning the value of parameter isEncrypted
	 * @author Marcin Lesniewski
	 * @return property that specifies whether the message is encrypted or not
	 * @deprecated
	 */
	public boolean isEncrypted() {
		return isEncrypted;
	}

}