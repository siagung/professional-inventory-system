package caseStudy;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**InventorySystem.java
 * 
 * This program will simulate a complete Inventory System on a GUI Applet.
 * 		The program will first ask the user to open/create a new file database.
 * 		The user will then be presented with the workspace for the Professional
 * 		inventory system. The program contains four main buttons that will do the following.
 * 
 * Inquire - The user asks for existent stocks in the inventory by type, brand, color.
 * 				The program will then return a table consisting of the matches together
 * 				with the quantity of the items.
 * Add Item - The user will input a new stock indicating the type, brand and color 
 * 				(which are all predefined) and the quantity of the stocks. The stocks
 * 				are then saved in the file stream.
 * Delete item - The user will delete an existing stockhhh indicating the type, brand, color,
 * 				and quantity of the item(s). The result is then saved in the file stream
 * 
 * Display - The program will show all the existing stocks in a table by type, brand, color,
 * 				and quantity.
 * 
 * The program will also include an exit button that will close the instance of the program.
 */

/**
 * @author h@ck.2slash
 * 
 */
public class InventorySystem extends JFrame implements ActionListener {

	private static final long serialVersionUID = 6227204770867692643L;

	// / other global objects
	File inputFile;
	PrintWriter pwOutStream;
	Scanner frInStream;
	Frame mainFrame = new Frame();
	FileDialog dialog = new FileDialog(mainFrame, "File Database Specification");
	String key = new String("PISystemFile");

	boolean isFileLoaded = false; // check if file has been loaded

	// / Points!

	Point pointWorkspace = new Point(460, 300);

	// / Panel instantiation here
	Panel pnlSideBar = new Panel();
	Panel pnlTitleTop = new Panel();
	Panel pnlWorkSpace = new Panel();
	Panel pnlInquiry = new Panel();
	Panel pnlAddItem = new Panel();
	Panel pnlDelItem = new Panel();
	Panel pnlDisplay = new Panel();

	// / Container instantiation here
	private Container mainContainer;

	// / LayoutManagers
	BorderLayout layoutMain = new BorderLayout(0, 10);
	CardLayout layoutPanelWorkspace = new CardLayout();

	// / Font
	Font fntBtn = new Font("SansSerif", Font.PLAIN, 14);
	Font fntTitle = new Font("SansSerif", Font.BOLD, 26);
	Font fntLabel = new Font("SansSerif", Font.PLAIN, 12);
	Font fntTableHdr = new Font("SansSerif", Font.BOLD, 12);
	Font fntNormal = new Font("SansSerif", Font.PLAIN, 12);

	// / Labels
	Label lblTitle = new Label("Professional Inventory System");

	Label lblInfo = new Label("What would you like to do?");

	// / Buttons
	Button btnOpenFile = new Button("Open File");
	Button btnNewFile = new Button("New Database");
	// universal buttons
	Button btnInquire = new Button("INQUIRE");
	Button btnAddItem = new Button("ADD ITEM");
	Button btnDelete = new Button("DELETE");
	Button btnDisplay = new Button("STOCKS");
	Button btnExit = new Button("EXIT");

	int lineNumber;

	/**
	 * ----------------------------------------------------------------
	 */
	public InventorySystem() {
		super("Professional Inventory System");

		mainContainer = getContentPane();
		mainContainer.setLayout(layoutMain);
		mainContainer.setBackground(Color.white);
		pnlTitleTop.add(lblTitle);
		initializeCustomizations();
		initializeSidebar();

		pnlWorkSpace.setSize(450, 380);
		pnlWorkSpace.add(lblInfo);
		pnlWorkSpace.add(btnOpenFile);
		pnlWorkSpace.add(btnNewFile);

		pnlWorkSpace.setLocation(pointWorkspace);

		mainContainer.add(pnlTitleTop, BorderLayout.NORTH);
		mainContainer.add(pnlSideBar, BorderLayout.WEST);
		mainContainer.add(pnlWorkSpace);

		btnOpenFile.addActionListener(this);
		btnNewFile.addActionListener(this);

		btnInquire.addActionListener(this);
		btnAddItem.addActionListener(this);
		btnDelete.addActionListener(this);
		btnDisplay.addActionListener(this);
		btnExit.addActionListener(this);

		setSize(new Dimension(640, 480));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		InventorySystem mainApp = new InventorySystem();

	}

	private void initializeSidebar() {
		pnlSideBar.setLayout(new BoxLayout(pnlSideBar, BoxLayout.Y_AXIS));
		pnlSideBar.add(btnInquire);
		pnlSideBar.add(btnAddItem);
		pnlSideBar.add(btnDelete);
		pnlSideBar.add(btnDisplay);
		pnlSideBar.add(btnExit);
	}

	private void initializeCustomizations() {

		lblTitle.setFont(fntTitle);

		btnOpenFile.setFont(fntBtn);
		btnNewFile.setFont(fntBtn);

		btnInquire.setFont(fntBtn);
		btnAddItem.setFont(fntBtn);
		btnDelete.setFont(fntBtn);
		btnDisplay.setFont(fntBtn);
		btnExit.setFont(fntBtn);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		/* event btnOpenFile */
		if (e.getSource() == btnOpenFile) {
			openFile();
		}// end event.btnOpenFile

		/* event btnNewFile */
		if (e.getSource() == btnNewFile) {
			newFile();
		}// end event.btnNewFile

		if (e.getSource() == btnInquire) {
			try {
				pnlWorkSpace = inquireInventoryPanel();
			} catch (FileNotFoundException e2) {

				e2.printStackTrace();
			}

		}

		if (e.getSource() == btnAddItem) {
			pnlWorkSpace.setVisible(false);
			addItemInventory();

		}

		if (e.getSource() == btnDelete) {
			pnlWorkSpace.setVisible(false);
			deleteItemsInventory();

		}

		if (e.getSource() == btnDisplay) {
			pnlWorkSpace.setVisible(false);
			try {
				displayInventory();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

		}

		if (e.getSource() == btnExit) {

			System.exit(0);
		}

	}

	/*
	 * class methods
	 */
	public Panel inquireInventoryPanel() throws FileNotFoundException {
		String[] items;
		JTable tblInquireTable;
		DataTable dataTable = new DataTable();
		lineNumber = getTotalLineNumbers();

		frInStream = null;
		frInStream = new Scanner(new FileReader(inputFile));

		frInStream.nextLine();
		// FIXME too!
		items = new String[lineNumber - 1];
		tblInquireTable = new JTable(dataTable);

		for (int a = 0; (a < lineNumber) && frInStream.hasNext(); a++) {
			items[a] = new String(frInStream.next() + frInStream.next()
					+ frInStream.next() + frInStream.next() + frInStream.next());
			// lblInquireTable[a] = new Label(items[a].toString());
			// inquireInventoryPanel().add(lblInquireTable[a]);
		}

		return inquireInventoryPanel();

	}

	public Panel addItemInventory() {

		return addItemInventory();
		// TODO Auto-generated method stub

	}

	public Panel deleteItemsInventory() {
		// TODO Auto-generated method stub

		return deleteItemsInventory();
	}

	public Panel displayInventory() throws FileNotFoundException {
		frInStream = null;
		frInStream = new Scanner(new FileReader(inputFile));

		frInStream.skip(key);
		// while (!frInStream.hasNext() && frInStream.next().equals("END")) {
		// = frInStream.next();
		// FIXME
		return displayInventory();
	}

	/*
	 * tools
	 */
	private boolean isPISFile() throws FileNotFoundException {
		Scanner fileTestRead = new Scanner(inputFile);

		if (!fileTestRead.next().equals(key)) {
			JOptionPane.showMessageDialog(null,
					"Invalid File! Open a valid inventory file.");
			fileTestRead.close();
			return false;
		}
		fileTestRead.close();
		return true;
	}

	public void newFile() {
		dialog.setMode(FileDialog.SAVE);
		dialog.setVisible(true);

		// start try-catch on inputFile creation
		try {
			if (!dialog.getFile().equals("")) {

				String outfile = dialog.getFile();

				inputFile = new File(dialog.getDirectory() + outfile);
				pwOutStream = new PrintWriter(inputFile);
				pwOutStream.print(key);
				pwOutStream.flush();
			}
		} catch (Exception f) {
			System.out.println(f.getMessage());
		}// end try-catch

		pwOutStream = null;
		isFileLoaded = true;

		pnlWorkSpace.setVisible(false);
	}

	public void openFile() {
		do {
			dialog.setMode(FileDialog.LOAD);
			dialog.setVisible(true);

			// start try-catch on inputFile opening
			try {
				if (!dialog.getFile().equals("")) {
					inputFile = new File(dialog.getDirectory()
							+ dialog.getFile());
					if (isPISFile())
						isFileLoaded = true;
					else
						isFileLoaded = false;
				}// end if
			}

			catch (Exception x) {
				JOptionPane.showMessageDialog(null,
						"Invalid File! Open a valid inventory file.");
				isFileLoaded = false;
				System.out.println(x.getMessage());
			}// end try-catch
		} while (!isFileLoaded);

		try {
			lineNumber = getTotalLineNumbers();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		isFileLoaded = true;

		pnlWorkSpace.setVisible(false);
	}

	private int getTotalLineNumbers() throws FileNotFoundException {
		int no = 0;
		Scanner input = new Scanner(new FileReader(inputFile));

		while (input.hasNextLine()) {
			no++;
			input.nextLine();
		}

		input.close();
		return no;
	}
}