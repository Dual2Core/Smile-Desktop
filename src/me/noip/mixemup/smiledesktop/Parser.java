package me.noip.mixemup.smiledesktop;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.FindCallback;

public class Parser implements Runnable
{
	/**
	 * Core.
	 */
	private ParsingComponent prscmp;
	private ChatBackbone chatskel;
	private SmileDesktop chat;
	/**
	 * Message queues.
	 */
	public Queue<ChatMessage> sendMsgQueue;
	private Queue<ChatMessage> dispatchMsgQueue;

	public Parser(SmileDesktop chat, ParsingComponent prscmp, ChatBackbone chatskel)
	{
		this.chat = chat;
		this.prscmp = prscmp;
		this.chatskel = chatskel;
		sendMsgQueue = new LinkedList<ChatMessage>();
		dispatchMsgQueue = new LinkedList<ChatMessage>();
		LoadHistory();
	}

	private void LoadHistory()
	{
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
		query.addAscendingOrder("createdAt");
		query.findInBackground(new FindCallback<ParseObject>()
		{
			@Override
			public void done(List<ParseObject> historicalMsgs, ParseException e)
			{
				System.out.println("Retrieving chat history.");
					for (int i = 0; i < historicalMsgs.size(); i++)
					{
						ParseObject row = historicalMsgs.get(i);
						String txt = row.getString("text");
						Date date = row.getCreatedAt();
						boolean isMe;
						if (row.getString("sender").contains("Boyfriend"))
							isMe = true;
						else
							isMe = false;

						dispatchMsgQueue.add(new ChatMessage(txt, isMe, date));
					}
			}

		});

	}

	@Override
	public void run()
	{
		try
		{
			for (;; Thread.sleep(1000))
			{
				/**
				 * Straight way to cloud.
				 */
				SendMessagesFromQueue();

				/**
				 * Straight way from cloud to device.
				 */
				GatherMessagesFromCloudToQueue();
				DispatchMessagesFromQueue();
			}

		}
		catch (InterruptedException e)
		{
		}
	}

	private void DispatchMessagesFromQueue()
	{

		if (dispatchMsgQueue.size() <= 0)
		{
			System.out.println("There are no messages to dispatch!");
			return;
		}
		System.out.println("There are messages to dispatch.");
		for (int i = 1; i <= dispatchMsgQueue.size(); i++)
		{
			ChatMessage msg = dispatchMsgQueue.poll();
			chat.addMessageToContainer(msg);
		}
		System.out.println("Messages dispatched!");
	}

	private void SendMessagesFromQueue()
	{
		if (sendMsgQueue.size() > 0)
		{
			ChatMessage cmsg;
			for (int i = 1; i <= sendMsgQueue.size(); i++)
			{
				cmsg = sendMsgQueue.poll();
				prscmp.parseThis(cmsg);
				System.out.println("I have send a message from queue.");
			}
		}

	}

	private void GatherMessagesFromCloudToQueue()
	{
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
		Date lastmsgtime;
		lastmsgtime = new Date();

		query.whereGreaterThan("createdAt", lastmsgtime);
		query.whereEqualTo("sender", "Girlfriend");
		query.whereEqualTo("readstate", false);

		query.findInBackground(new FindCallback<ParseObject>()
		{
			public void done(List<ParseObject> newMessagesList, ParseException e)
			{
				if (e == null && newMessagesList != null && newMessagesList.size() > 0) // List
																						// is
																						// filled
				{
					System.out.println("New messages found in the cloud!");
					for (int i = 0; i < newMessagesList.size(); i++)
					{
						ParseObject row = newMessagesList.get(i);
						String txt = row.getString("text");
						Date date = row.getCreatedAt();
						dispatchMsgQueue.add(new ChatMessage(txt, false, date));
						newMessagesList.get(i).put("readstate", true);
						newMessagesList.get(i).saveInBackground();
					}
				}
			}
		});
	}

}
