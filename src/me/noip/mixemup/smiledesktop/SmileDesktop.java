package me.noip.mixemup.smiledesktop;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.http.util.TextUtils;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SmileDesktop
{
	/**
	 * Chat UI components.
	 */
	private JFrame frame;
	private JTextField textField;
	private JList list;
	private JButton btnNewButton;

	/**
	 * Back-end Core components.
	 */
	private static ParsingComponent parseCmp;
	private static ChatBackbone chatskel;
	private static Parser prsr;
	private DefaultListModel<String> dlm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					SmileDesktop window = new SmileDesktop();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public SmileDesktop()
	{
		initialize();
		chatskel = new ChatBackbone(list, dlm);
		parseCmp = new ParsingComponent();
		prsr = new Parser(this, parseCmp, chatskel);
		Thread prsrThread = new Thread(prsr);
		prsrThread.start();

	}

	public void addMessageToContainer(ChatMessage chatmsg)
	{
		chatskel.add(chatmsg);
		list.ensureIndexIsVisible(dlm.size() - 1);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 526, 301);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		list = new JList();
		list.setBounds(12, 12, 426, 219);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setBounds(12, 12, 502, 219);
		frame.getContentPane().add(scroll);
		dlm = new DefaultListModel<>();
		list.setModel(dlm);

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					btnNewButton.doClick();
				}
			}
		});
		textField.setBounds(12, 243, 403, 30);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				System.out.println("Clicked!");
				String msg = textField.getText().toString();
				if (TextUtils.isEmpty(msg))
					return;
				System.out.println("Creating new msg!");
				ChatMessage chatMessage = new ChatMessage(msg, true, new Date());
				addMessageToContainer(chatMessage);

				textField.setText("");
				prsr.sendMsgQueue.add(chatMessage);
			}
		});
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 10));
		btnNewButton.setBounds(425, 243, 89, 30);
		frame.getContentPane().add(btnNewButton);

	}
}
