package me.noip.mixemup.smiledesktop;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class ChatBackbone
{
	/**
	 * Container.
	 */
	private final JList<ChatMessage> list;
	private final DefaultListModel<String> dlm;

	/**
	 * Constructor sets main parameters of chat backbone.
	 * 
	 * @param context
	 * @param list
	 */
	public ChatBackbone(JList<ChatMessage> list, DefaultListModel<String> dlm)
	{
		this.list = list;
		this.dlm = dlm;
	}

	public void add(ChatMessage message)
	{
		dlm.addElement(message.toString());
	}

}
