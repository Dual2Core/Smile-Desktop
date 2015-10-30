package me.noip.mixemup.smiledesktop;

import java.text.DateFormat;
import java.util.Date;

import org.parse4j.ParseObject;

import me.noip.mixemup.smiledesktop.ParsingComponent.Parsable;

public class ChatMessage implements Parsable
{
	/**
	 * Parameters
	 */
	private String message;
	private boolean isMe;
	private String now;
	private Date date;

	public ChatMessage(String msgText, boolean isMe, Date dt)
	{
		this.message = msgText;
		this.isMe = isMe;
		this.now = DateFormat.getDateTimeInstance().format(dt);
		this.date = dt;
	}

	@Override
	public void parse(ParsingComponent cloud)
	{
		ParseObject msg = new ParseObject("Messages");
		msg.put("text", message);
		msg.put("sender", "Boyfriend");
		msg.put("readstate", false);
		msg.saveInBackground();
	}

	public boolean isMe()
	{
		return isMe;
	}

	public String getMessage()
	{
		return message;
	}

	public String getDate()
	{
		return now;
	}

	public Date getDateObject()
	{
		return date;
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("[" + getDate() + "] ");
		if(isMe())
			str.append("Boyfriend");
		else
			str.append("Girlfriend");
		str.append(" ] ");
		str.append(getMessage());
		
		return str.toString();
	}
}
