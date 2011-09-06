package caseStudyII;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class About extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5435838063608110149L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public About() {
		setTitle("About");
		setResizable(false);
		setBounds(100, 100, 240, 285);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel panel = new JPanel();
			panel.setBounds(10, 137, 214, 109);
			contentPanel.add(panel);
			panel.setLayout(new BorderLayout(0, 0));

			JTextPane txtpnProfessionalInventorySystem = new JTextPane();
			txtpnProfessionalInventorySystem.setBackground(SystemColor.menu);
			txtpnProfessionalInventorySystem.setFont(new Font("Tahoma",
					Font.PLAIN, 10));
			txtpnProfessionalInventorySystem.setEditable(false);
			txtpnProfessionalInventorySystem
					.setText("Version 3.0 (Stable)\r\nPISGUI version 4.6a\r\nby:\r\nawkwardusername (Mark Jayson Fuentes)\r\nDesigned in WindowBuilder 1.1.0.r37 using Eclipse Indigo 3.7 IDE");
			panel.add(txtpnProfessionalInventorySystem, BorderLayout.CENTER);

			JTextPane txtpnProfessionalInventorySystem_1 = new JTextPane();
			txtpnProfessionalInventorySystem_1.setFont(new Font("Tahoma",
					Font.BOLD, 10));
			txtpnProfessionalInventorySystem_1.setBackground(SystemColor.menu);
			txtpnProfessionalInventorySystem_1
					.setText("Professional Inventory System");
			panel.add(txtpnProfessionalInventorySystem_1, BorderLayout.NORTH);
		}
		{
			JLabel lblNewLabel = new JLabel("New label");
			lblNewLabel
					.setIcon(new ImageIcon(
							"C:\\Users\\h@ck.2slash\\git\\InventorySystem\\Professional Inventory System\\src\\Angry-Birds-52680282.jpg"));
			lblNewLabel.setBounds(10, 11, 214, 115);
			contentPanel.add(lblNewLabel);
		}
	}
}
