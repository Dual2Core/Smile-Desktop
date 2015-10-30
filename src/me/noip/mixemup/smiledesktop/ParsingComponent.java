package me.noip.mixemup.smiledesktop;

import org.parse4j.Parse;
import org.parse4j.ParseException;
import org.parse4j.ParseUser;

public class ParsingComponent
{
	public ParsingComponent()
	{
		Parse.initialize("api_key", "android_key");
		ParseUser user = new ParseUser();
		user.setUsername("login");
		user.setPassword("password");

		try
		{
			user.login("login", "password");
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Interface to be sure that the object allows to be parsed.
	 * 
	 * @author DualCore
	 *
	 */
	public interface Parsable
	{
		void parse(ParsingComponent cloud);
	}

	/**
	 * Parses the object to the cloud.
	 * 
	 * @param object
	 */
	public void parseThis(Parsable object)
	{
		object.parse(this);
	}

}
