
package com.mucommander.ui;

import com.mucommander.ui.comp.dialog.*;
import com.mucommander.file.AbstractFile;
import com.mucommander.file.SMBFile;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ServerConnectDialog extends FocusDialog implements ActionListener, KeyListener {
	private MainFrame mainFrame;
	
	private JButton okButton;
	private JButton cancelButton;
	private JPanel serverDetailsPanel;

	private EscapeKeyAdapter escapeKeyAdapter;

	// Dialog's width has to be at least 320
	private final static Dimension MINIMUM_DIALOG_DIMENSION = new Dimension(320,0);	
	
	private JComboBox serverTypeCombo;

	/* SMB related fields */
	private JTextField smbServerField;
	private JTextField smbUsernameField;
	private JPasswordField smbPasswordField;
	private JLabel smbURLLabel;
	
	private static String lastSmbServer = "";
	private static String lastSmbUsername = "";
	private static String lastSmbPassword = "";
	
	public ServerConnectDialog(MainFrame mainFrame) {
		super(mainFrame, "Connect to Server", mainFrame);
		this.mainFrame = mainFrame;
		
		Container contentPane = getContentPane();
		escapeKeyAdapter = new EscapeKeyAdapter(this);
		
		YBoxPanel mainPanel = new YBoxPanel();
		JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tempPanel.add(new JLabel("Server type  "));
		serverTypeCombo = new JComboBox();
		serverTypeCombo.addItem("SMB");
		serverTypeCombo.addKeyListener(escapeKeyAdapter);
		tempPanel.add(serverTypeCombo);
		mainPanel.add(tempPanel);

		serverDetailsPanel = getSMBPanel();
		mainPanel.add(serverDetailsPanel);
		contentPane.add(mainPanel, BorderLayout.NORTH);
		
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		// Escape key disposes dialog
		okButton.addKeyListener(escapeKeyAdapter);
		cancelButton.addKeyListener(escapeKeyAdapter);
		contentPane.add(DialogToolkit.createOKCancelPanel(okButton, cancelButton, this), BorderLayout.SOUTH);

		// server field will receive initial focus
		setInitialFocusComponent(smbServerField);		
		
		// Selects OK when enter is pressed
		getRootPane().setDefaultButton(okButton);

		// Packs dialog
		setMinimumSize(MINIMUM_DIALOG_DIMENSION);
	}


	private JPanel getSMBPanel() {
		YBoxPanel smbPanel = new YBoxPanel(10);

		// Server field
		JPanel tempPanel = new JPanel(new BorderLayout());
		tempPanel.add(new JLabel("Server  "), BorderLayout.WEST);
		smbServerField = new JTextField(lastSmbServer);
		smbServerField.addKeyListener(escapeKeyAdapter);
		smbServerField.addKeyListener(this);
		smbServerField.addActionListener(this);
		tempPanel.add(smbServerField, BorderLayout.CENTER);
		smbPanel.add(tempPanel);

		// Username field
		tempPanel = new JPanel(new BorderLayout());
		tempPanel.add(new JLabel("Username  "), BorderLayout.WEST);
		smbUsernameField = new JTextField(lastSmbUsername);
		smbUsernameField.addKeyListener(escapeKeyAdapter);
		smbUsernameField.addKeyListener(this);
		smbUsernameField.addActionListener(this);
		tempPanel.add(smbUsernameField, BorderLayout.CENTER);
		smbPanel.add(tempPanel);

		// Password field
		tempPanel = new JPanel(new BorderLayout());
		tempPanel.add(new JLabel("Password  "), BorderLayout.WEST);
		smbPasswordField = new JPasswordField(lastSmbPassword);
		smbPasswordField.addKeyListener(escapeKeyAdapter);
		smbPasswordField.addKeyListener(this);
		smbPasswordField.addActionListener(this);
		tempPanel.add(smbPasswordField, BorderLayout.CENTER);
		smbPanel.add(tempPanel);

		smbPanel.addSpace(15);
		
		tempPanel = new JPanel(new BorderLayout());
		tempPanel.add(new JLabel("Server URL:  "), BorderLayout.WEST);
		smbURLLabel = new JLabel(getSmbURL());
		tempPanel.add(smbURLLabel, BorderLayout.CENTER);
		smbPanel.add(tempPanel);

		smbPanel.addSpace(10);
		
		return smbPanel;
	}


	private String getSmbURL() {
		String server = smbServerField.getText().trim();
		return "smb://"+server;
	}


	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source==okButton || source==smbServerField || source==smbUsernameField || source==smbPasswordField)  {
			lastSmbServer = smbServerField.getText().trim();
			lastSmbUsername = smbUsernameField.getText().trim();
			lastSmbPassword = new String(smbPasswordField.getPassword());

			dispose();
		
			mainFrame.getLastActiveTable().getBrowser().setCurrentFolder(AbstractFile.getAbstractFile(SMBFile.getPrivateURL(getSmbURL(), lastSmbUsername, lastSmbPassword)), true);
		}
		else if (source==cancelButton)  {
			dispose();			
		}
	}


	/***********************
	 * KeyListener methods *
	 ***********************/

	public void keyPressed(KeyEvent e) {
		smbURLLabel.setText(getSmbURL());
//		okButton.setEnabled(!smbServerField.getText().trim().equals(""));
	}

	public void keyReleased(KeyEvent e) {
		smbURLLabel.setText(getSmbURL());
//		okButton.setEnabled(!smbServerField.getText().trim().equals(""));
	}

	public void keyTyped(KeyEvent e) {
		smbURLLabel.setText(getSmbURL());
//		okButton.setEnabled(!smbServerField.getText().trim().equals(""));
	}
}