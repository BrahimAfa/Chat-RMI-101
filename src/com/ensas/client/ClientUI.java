package com.ensas.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

/**
 * 
 * @author Daragh Walshe 	B00064428
 * RMI Assignment 2		 	April 2015
 *
 */
public class ClientUI extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;	
	private JPanel textPanel, inputPanel;
	private JTextField textField;
	private String name, message;
	private Font meiryoFont = new Font("Meiryo", Font.PLAIN, 14);
	private Border blankBorder = BorderFactory.createEmptyBorder(10,10,20,10);//top,r,b,l
	private ChatClientImpl chatClient;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    
    protected JTextArea textArea, userArea;
    protected JFrame frame;
    protected JButton startButton, sendButton;
    protected JPanel clientPanel, userPanel;

	public static void main(String args[]){
		try{
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
				if("Nimbus".equals(info.getName())){
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch(Exception e){
			}
		new ClientUI();
		}

	public ClientUI(){
			
		frame = new JFrame("Client Chat Console");
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {

				if(chatClient != null){
					try {
						chatClient.serverIF.quite(name);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		});
		Container c = getContentPane();
		JPanel outerPanel = new JPanel(new BorderLayout());
		
		outerPanel.add(getInputPanel(), BorderLayout.CENTER);
		outerPanel.add(getTextPanel(), BorderLayout.NORTH);
		
		c.setLayout(new BorderLayout());
		c.add(outerPanel, BorderLayout.CENTER);
		c.add(getUsersPanel(), BorderLayout.WEST);

		frame.add(c);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setLocation(150, 150);
		textField.requestFocus();
	
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public JPanel getTextPanel(){
		String welcome = "Welcome enter your name and press Start to begin\n";
		textArea = new JTextArea(welcome, 14, 34);
		textArea.setMargin(new Insets(10, 10, 10, 10));
		textArea.setFont(meiryoFont);
		
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		textPanel = new JPanel();
		textPanel.add(scrollPane);
	
		textPanel.setFont(new Font("Meiryo", Font.PLAIN, 14));
		return textPanel;
	}

	public JPanel getInputPanel(){
		inputPanel = new JPanel(new GridLayout(1, 1, 5, 5));
		inputPanel.setBorder(blankBorder);	
		textField = new JTextField();
		textField.setFont(meiryoFont);
		inputPanel.add(textField);
		return inputPanel;
	}

	public JPanel getUsersPanel(){
		
		userPanel = new JPanel(new BorderLayout());
		String  userStr = " Current Users      ";
		
		JLabel userLabel = new JLabel(userStr, JLabel.CENTER);
		userPanel.add(userLabel, BorderLayout.NORTH);	
		userLabel.setFont(new Font("Meiryo", Font.PLAIN, 16));

		String[] noClientsYet = {"No other users"};
		setClientPanel(noClientsYet);

		clientPanel.setFont(meiryoFont);
		userPanel.add(makeButtonPanel(), BorderLayout.SOUTH);		
		userPanel.setBorder(blankBorder);

		return userPanel;		
	}

    public void setClientPanel(String[] currClients) {  	
    	clientPanel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<String>();
        
        for(String s : currClients){
        	listModel.addElement(s);
        }

        list = new JList<String>(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(8);
        list.setFont(meiryoFont);
        JScrollPane listScrollPane = new JScrollPane(list);

        clientPanel.add(listScrollPane, BorderLayout.CENTER);
        userPanel.add(clientPanel, BorderLayout.CENTER);
    }

	public JPanel makeButtonPanel() {		
		sendButton = new JButton("Send ");
		sendButton.addActionListener(this);
		sendButton.setEnabled(false);
		
		startButton = new JButton("Start ");
		startButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(startButton);
		buttonPanel.add(sendButton);
		
		return buttonPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e){

		try {
			//get connected to chat service
			if(e.getSource() == startButton){
				name = textField.getText();				
				if(name.length() != 0){
					frame.setTitle(name + "'s console ");
					textField.setText("");
					textArea.append("username : " + name + " connecting to chat...\n");							
					getConnected(name);
					if(!chatClient.connectionProblem){
						startButton.setEnabled(false);
						sendButton.setEnabled(true);
						}
				}
				else{
					JOptionPane.showMessageDialog(frame, "Enter your name to Start");
				}
			}

			//get text and clear textField
			if(e.getSource() == sendButton){
				message = textField.getText();
				textField.setText("");
				sendMessage(message);
				System.out.println("Sending message : " + message);
			}
		}
		catch (RemoteException remoteExc) {			
			remoteExc.printStackTrace();	
		}
		
	}

	private void sendMessage(String chatMessage) throws RemoteException {
		chatClient.serverIF.Send(name, chatMessage);
	}

	private void getConnected(String userName) throws RemoteException{
		String cleanedUserName = userName.replaceAll("\\W+","_");
		try {		
			chatClient = new ChatClientImpl(this, cleanedUserName);
			chatClient.startClient();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}










