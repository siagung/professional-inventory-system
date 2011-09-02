package caseStudyII;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;


public class InventorySystemGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FileDialog dialog = new FileDialog(this,
			"File Database Specification");
	public File inputFile;
	String key = new String("PISystemFile");
	protected Scanner fwScanner;
	protected PrintWriter pwOutStream;
	boolean isFileLoaded;

	private JPanel contentPane;
	protected Object lineNumber;

	private String[] popupType = ItemStock.itemChoiceType;
	private String[] popupColor = ItemStock.itemChoiceColor;
	private String[] popupSize = ItemStock.itemChoiceSize;
	private String[] columnHeader = { "Type", "Color", "Size", "Quantity" };

	private JTabbedPane tabbedPane;
	private Panel tabPaneInquire;
	private Panel tabPaneAdd;
	private Panel tabPaneDelete;
	private Panel tabPaneDisplay;

	protected boolean isTableDisplayed = false;
	private JTable tblDisplayStock;
	private JScrollPane scrollPaneTableDisplay;

	private String[][] readFileValues;
	private int[][] intTableValues;
	private String[][] writeTableValues = new String[100][4];
	private int[][][] quantity = new int[5][5][4]; // yes. a three dimensional

	protected JScrollPane scrollPaneTableInquiry;

	protected String filepath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JOptionPane
							.showMessageDialog(
									null,
									"Welcome to the Professional Inventory System!\n"
											+ "Please click File on the menu, then choose to Open, or make a New File to continue.",
									"Welcome!", JOptionPane.INFORMATION_MESSAGE);
					InventorySystemGUI frame = new InventorySystemGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InventorySystemGUI() {
		setResizable(false);
		setTitle("Professional Inventory System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 581, 539);
		// XXX
		/**
		 * start of menuBar ====================================================
		 **/
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setMode(FileDialog.SAVE);
				dialog.setVisible(true);

				// start try-catch on inputFile creation
				try {
					if (!dialog.getFile().equals("")) {

						String outfile = dialog.getFile();

						inputFile = new File(dialog.getDirectory() + outfile);
						filepath = new String(dialog.getDirectory() + outfile);
						pwOutStream = new PrintWriter(inputFile);
						pwOutStream.print(key);
						pwOutStream.print("1 1 1 1");
						pwOutStream.flush();
						isFileLoaded = true;
					}
				} catch (Exception f) {
					System.out.println(f.getMessage());
				}// end try-catch

				try {
					lineNumber = getTotalLineNumbers();
					getStrTableValues();
				} catch (FileNotFoundException x) {
					x.printStackTrace();
				}

				tblDisplayStock = new JTable(writeTableValues, columnHeader);
				tblDisplayStock.setShowGrid(false);
				tblDisplayStock.setRowSelectionAllowed(false);
				tblDisplayStock.setFillsViewportHeight(true);
				scrollPaneTableDisplay.setViewportView(tblDisplayStock);

				tabbedPane.setVisible(true);
				isFileLoaded = true;

			}
		});

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				do {
					dialog.setMode(FileDialog.LOAD);
					dialog.setVisible(true);

					// start try-catch on inputFile opening
					try {
						if (!dialog.getFile().equals("")) {
							inputFile = new File(dialog.getDirectory()
									+ dialog.getFile());
							filepath = new String(dialog.getDirectory()
									+ dialog.getFile());
							if (isPISFile())
								isFileLoaded = true;
							else
								isFileLoaded = false;
						}// end if
					}

					catch (Exception x) {
						JOptionPane.showMessageDialog(null,
								"Invalid File! Open a valid inventory file.",
								"Invalid File", JOptionPane.ERROR_MESSAGE);
						isFileLoaded = false;
						System.out.println(x.getMessage());
					}// end try-catch
				} while (!isFileLoaded);

				try {
					lineNumber = getTotalLineNumbers();
					getStrTableValues();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				tblDisplayStock = new JTable(writeTableValues, columnHeader);
				tblDisplayStock.setShowGrid(false);
				tblDisplayStock.setRowSelectionAllowed(false);
				tblDisplayStock.setFillsViewportHeight(true);
				scrollPaneTableDisplay.setViewportView(tblDisplayStock);
				tabbedPane.setVisible(true);
				isFileLoaded = true;
			}
		});
		mnFile.add(mntmOpen);

		mnFile.add(mntmNew);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveFile(filepath);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnFile.add(mntmSave);
		// ----------------------------------------------------------
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		// ---------------------------------------------------------- XD
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JOptionPane
						.showMessageDialog(null,
								"Thank you for using the Professional Inventory System");

				try {
					saveFile(filepath);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setVisible(false);

		tabPaneInquire = new Panel();
		tabbedPane.addTab("Inquire Items", null, tabPaneInquire, null);
		tabPaneInquire.setLayout(null);

		JPanel pnlToolsInquire = new JPanel();
		pnlToolsInquire.setLayout(null);
		pnlToolsInquire.setBounds(10, 11, 529, 50);
		tabPaneInquire.add(pnlToolsInquire);

		Label lblColorInquire = new Label("Color");
		lblColorInquire.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblColorInquire.setAlignment(Label.RIGHT);
		lblColorInquire.setBounds(0, 28, 49, 22);
		pnlToolsInquire.add(lblColorInquire);

		Label lblTypeInquire = new Label("Type");
		lblTypeInquire.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblTypeInquire.setAlignment(Label.RIGHT);
		lblTypeInquire.setBounds(0, 0, 49, 22);
		pnlToolsInquire.add(lblTypeInquire);

		final Choice choiceTypeInquire = new Choice();
		choiceTypeInquire.setBounds(55, 2, 150, 20);
		pnlToolsInquire.add(choiceTypeInquire);

		final Choice choiceColorInquire = new Choice();
		choiceColorInquire.setBounds(55, 30, 150, 20);
		pnlToolsInquire.add(choiceColorInquire);

		final Choice choiceSizeInquire = new Choice();
		choiceSizeInquire.setBounds(289, 0, 150, 20);
		pnlToolsInquire.add(choiceSizeInquire);

		final TextField tFieldQuantityInquire = new TextField();
		tFieldQuantityInquire.setBounds(289, 28, 150, 22);
		pnlToolsInquire.add(tFieldQuantityInquire);

		Label lblQuantityInquire = new Label("Quantity");
		lblQuantityInquire.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblQuantityInquire.setAlignment(Label.RIGHT);
		lblQuantityInquire.setBounds(211, 28, 72, 22);
		pnlToolsInquire.add(lblQuantityInquire);

		Label lblSizeInquire = new Label("Size");
		lblSizeInquire.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblSizeInquire.setAlignment(Label.RIGHT);
		lblSizeInquire.setBounds(211, 0, 72, 22);
		pnlToolsInquire.add(lblSizeInquire);

		Button btnSearch = new Button("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int iType, iColor, iSize, iQuantity;

				iType = choiceTypeInquire.getSelectedIndex();
				iColor = choiceColorInquire.getSelectedIndex();
				iSize = choiceSizeInquire.getSelectedIndex();

				iQuantity = Integer.parseInt(tFieldQuantityInquire.getText());

				if (searchQuantity(iType, iColor, iSize, iQuantity))
					JOptionPane.showMessageDialog(null, "Type: "
							+ popupType[iType] + " | Color: "
							+ popupColor[iColor] + " | Size: "
							+ popupSize[iSize] + " | Quantity: " + iQuantity
							+ " FOUND!", "Item Found",
							JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "Type: "
							+ popupType[iType] + " | Color: "
							+ popupColor[iColor] + " | Size: "
							+ popupSize[iSize] + " | Quantity: " + iQuantity
							+ " NOT FOUND!", "Item NOT Found",
							JOptionPane.ERROR_MESSAGE);

			}
		});
		btnSearch.setBounds(459, 10, 70, 22);
		pnlToolsInquire.add(btnSearch);

		for (int k = 0; k < popupType.length; k++)
			choiceTypeInquire.add(popupType[k]);
		for (int k = 0; k < popupColor.length; k++)
			choiceColorInquire.add(popupColor[k]);
		for (int k = 0; k < popupSize.length; k++)
			choiceSizeInquire.add(popupSize[k]);

		tabPaneAdd = new Panel();
		tabbedPane.addTab("Add Items", null, tabPaneAdd, null);
		tabPaneAdd.setLayout(null);

		JPanel pnlToolsAdd = new JPanel();
		pnlToolsAdd.setBounds(10, 11, 539, 50);
		tabPaneAdd.add(pnlToolsAdd);
		pnlToolsAdd.setLayout(null);

		Label lblTypeAdd = new Label("Type");
		lblTypeAdd.setBounds(0, 0, 49, 22);
		pnlToolsAdd.add(lblTypeAdd);
		lblTypeAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblTypeAdd.setAlignment(Label.RIGHT);

		Label lblColorAdd = new Label("Color");
		lblColorAdd.setBounds(0, 28, 49, 22);
		pnlToolsAdd.add(lblColorAdd);
		lblColorAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblColorAdd.setAlignment(Label.RIGHT);

		Label lblSizeAdd = new Label("Size");
		lblSizeAdd.setBounds(211, 0, 72, 22);
		pnlToolsAdd.add(lblSizeAdd);
		lblSizeAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblSizeAdd.setAlignment(Label.RIGHT);

		Label lblNoAdd = new Label("Quantity");
		lblNoAdd.setBounds(211, 28, 72, 22);
		pnlToolsAdd.add(lblNoAdd);
		lblNoAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblNoAdd.setAlignment(Label.RIGHT);

		final Choice choiceTypeAdd = new Choice();
		for (int k = 0; k < popupType.length; k++)
			choiceTypeAdd.add(popupType[k]);
		choiceTypeAdd.setBounds(55, 2, 150, 20);
		pnlToolsAdd.add(choiceTypeAdd);

		final Choice choiceColorAdd = new Choice();
		for (int k = 0; k < popupColor.length; k++)
			choiceColorAdd.add(popupColor[k]);
		choiceColorAdd.setBounds(55, 30, 150, 20);
		pnlToolsAdd.add(choiceColorAdd);

		final Choice choiceSizeAdd = new Choice();
		for (int k = 0; k < popupSize.length; k++)
			choiceSizeAdd.add(popupSize[k]);
		choiceSizeAdd.setBounds(289, 0, 150, 20);
		pnlToolsAdd.add(choiceSizeAdd);

		final TextField tFieldNoAdd = new TextField();
		tFieldNoAdd.setBounds(289, 28, 150, 22);
		pnlToolsAdd.add(tFieldNoAdd);

		Button btnAdd = new Button("Add Item");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int iType, iColor, iSize, iQuantity;

				iType = choiceTypeAdd.getSelectedIndex();
				iColor = choiceColorAdd.getSelectedIndex();
				iSize = choiceSizeAdd.getSelectedIndex();
				iQuantity = Integer.parseInt(tFieldNoAdd.getText());

				setQuantity(iType, iColor, iSize, iQuantity);
				JOptionPane.showMessageDialog(null, "Type: " + popupType[iType]
						+ " | Color: " + popupColor[iColor] + " | Size: "
						+ popupSize[iSize] + " | Quantity: " + iQuantity
						+ " ADDED!", "Item Added",
						JOptionPane.INFORMATION_MESSAGE);

				readQuantity();
				tblDisplayStock.repaint();

			}
		});
		btnAdd.setBounds(459, 10, 70, 22);
		pnlToolsAdd.add(btnAdd);
		tabbedPane.setEnabledAt(1, true);
		tabPaneDelete = new Panel();
		tabbedPane.addTab("Delete Items", null, tabPaneDelete, null);
		tabPaneDelete.setLayout(null);

		JPanel pnlToolsDelete = new JPanel();
		pnlToolsDelete.setLayout(null);
		pnlToolsDelete.setBounds(10, 11, 539, 50);
		tabPaneDelete.add(pnlToolsDelete);

		Label lblTypeDelete = new Label("Type");
		lblTypeDelete.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblTypeDelete.setAlignment(Label.RIGHT);
		lblTypeDelete.setBounds(0, 0, 49, 22);
		pnlToolsDelete.add(lblTypeDelete);

		Label lblColorDelete = new Label("Color");
		lblColorDelete.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblColorDelete.setAlignment(Label.RIGHT);
		lblColorDelete.setBounds(0, 28, 49, 22);
		pnlToolsDelete.add(lblColorDelete);

		Label lblSizeDelete = new Label("Size");
		lblSizeDelete.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblSizeDelete.setAlignment(Label.RIGHT);
		lblSizeDelete.setBounds(211, 0, 72, 22);
		pnlToolsDelete.add(lblSizeDelete);

		Label lblNoDelete = new Label("Quantity");
		lblNoDelete.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblNoDelete.setAlignment(Label.RIGHT);
		lblNoDelete.setBounds(211, 28, 72, 22);
		pnlToolsDelete.add(lblNoDelete);

		final Choice choiceTypeDelete = new Choice();
		for (int k = 0; k < popupType.length; k++)
			choiceTypeDelete.add(popupType[k]);
		choiceTypeDelete.setBounds(55, 2, 150, 20);
		pnlToolsDelete.add(choiceTypeDelete);

		final Choice choiceColorDelete = new Choice();
		for (int k = 0; k < popupColor.length; k++)
			choiceColorDelete.add(popupColor[k]);
		choiceColorDelete.setBounds(55, 30, 150, 20);
		pnlToolsDelete.add(choiceColorDelete);

		final Choice choiceSizeDelete = new Choice();
		for (int k = 0; k < popupSize.length; k++)
			choiceSizeDelete.add(popupSize[k]);
		choiceSizeDelete.setBounds(289, 0, 150, 20);
		pnlToolsDelete.add(choiceSizeDelete);

		final TextField tFieldNoDelete = new TextField();
		tFieldNoDelete.setBounds(289, 28, 150, 22);
		pnlToolsDelete.add(tFieldNoDelete);

		Button btnDelete = new Button("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int iType, iColor, iSize, iQuantity;

				iType = choiceTypeDelete.getSelectedIndex();
				iColor = choiceColorDelete.getSelectedIndex();
				iSize = choiceSizeDelete.getSelectedIndex();
				iQuantity = Integer.parseInt(tFieldNoDelete.getText());

				int temp = iQuantity;

				if (searchQuantity(iType, iColor, iSize, iQuantity)) {
					deleteQuantity(iType, iColor, iSize, iQuantity);
					JOptionPane.showMessageDialog(null, "Type: "
							+ popupType[iType] + " | Color: "
							+ popupColor[iColor] + " | Size: "
							+ popupSize[iSize] + " | Quantity: " + temp
							+ " Deleted!", "Item Deleted",
							JOptionPane.INFORMATION_MESSAGE);
				} else
					JOptionPane.showMessageDialog(null, "Type: "
							+ popupType[iType] + " | Color: "
							+ popupColor[iColor] + " | Size: "
							+ popupSize[iSize] + " | Quantity: " + temp
							+ " NOT FOUND!", "Item NOT Found",
							JOptionPane.ERROR_MESSAGE);

				readQuantity();
				tblDisplayStock.repaint();
			}
		});
		btnDelete.setBounds(459, 10, 70, 22);
		pnlToolsDelete.add(btnDelete);

		tabPaneDisplay = new Panel();
		tabPaneDisplay.setLayout(new BorderLayout(0, 0));

		tabbedPane.addTab("Display Items", null, tabPaneDisplay, null);

		JPanel pnlToolDisplay = new JPanel();
		tabPaneDisplay.add(pnlToolDisplay, BorderLayout.CENTER);
		pnlToolDisplay.setLayout(new BorderLayout(0, 0));

		scrollPaneTableDisplay = new JScrollPane();
		pnlToolDisplay.add(scrollPaneTableDisplay);

	}

	/*
	 * protected void cleanArray() { // TODO Auto-generated method stub for (int
	 * i = 0; i < 5; i++) for (int j = 0; j < 5; j++) for (int k = 0; k < 4;
	 * k++) writeTableValues[][];
	 * 
	 * }
	 */

	private void saveFile(String loc) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File saveMe = new File(loc);
		PrintWriter out2 = new PrintWriter(saveMe);

		out2.println(key);
		for (int k = 0; k < 100; k++) {
			for (int j = 0; j < 4; j++) {
				if (writeTableValues[k][j] != null) {

					out2.print(stringToIntIndexer(writeTableValues[k][j], j));
					out2.print(" ");
				} else {
					out2.print("0");
					out2.print(" ");
				}
			}
			out2.println();
		}
		out2.flush();
	}

	private int stringToIntIndexer(String realIndex, int j) {
		// TODO Auto-generated method stub
		switch (j) {
		case 0:
			for (int k = 0; k < ItemStock.itemChoiceType.length; k++)
				if (realIndex.equals(ItemStock.itemChoiceType[k]))
					return k;
		case 1:
			for (int k = 0; k < ItemStock.itemChoiceColor.length; k++)
				if (realIndex.equals(ItemStock.itemChoiceColor[k]))
					return k;
		case 2:
			for (int k = 0; k < ItemStock.itemChoiceSize.length; k++)
				if (realIndex.equals(ItemStock.itemChoiceSize[k]))
					return k;
		case 3:
		case 4:
			return Integer.parseInt(realIndex);
		}
		return -1;

	}

	/* end of pnlAdd */
	// XXX tools, of the trade!
	/**
	 * getTotalLineNumbers() ===================================================
	 * 
	 * returns the total number of lines in the file opened
	 * 
	 **/

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

	/**
	 * isPISFile() =============================================================
	 * 
	 * checks if the file opened is a PISFile. The Scanner searches at the start
	 * of the file for the String key, which contains the value "PISystemFile"
	 * returns true if a PISFile, false if not.
	 * 
	 **/
	protected boolean isPISFile() throws FileNotFoundException {
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

	/**
	 * getStrTableValues(Scanner fwScanner2) ===================================
	 * 
	 * gets the values stored in file, stores it in readFileValues for usage in
	 * the table. Actually sets the readFileValues for usage in the whole
	 * program.
	 * 
	 **/
	public void getStrTableValues() throws FileNotFoundException {

		Scanner fwScanner2 = new Scanner(new FileReader(inputFile));

		readFileValues = new String[getTotalLineNumbers()][4];

		fwScanner2.nextLine();
		for (int row = 0; row < getTotalLineNumbers() - 1; row++)
			for (int col = 0; col < 4; col++) {
				readFileValues[row][col] = fwScanner2.next();
			}
		sortTableValues();
		fwScanner2.close();
	}

	/**
	 * sortTableValues() =======================================================
	 * 
	 * It does everything. YES. It is actually a very long method subdivided
	 * into small method sequences.
	 * 
	 * first part involves reading the values from the file (which is previously
	 * stored on an array called readFileValues) and parses it into the real
	 * integer equivalents.
	 * 
	 * the next method, combineValues(), traverses the whole length of the file
	 * and calls the method setQuantity() for every cell in the array
	 * intTableValues.
	 * 
	 * setQuantity() sets the value of the quantity by type, by color, and by
	 * size. this is the hardest to think about. i swear. but it took longer for
	 * me to do the GUI.
	 * 
	 * readQuantity() calls makeArray(), to euclidate what does the three dime
	 * array is all about.
	 * 
	 * actually, quantity[][][] is quantity[type][color][size].
	 * 
	 * you got it already. I know.
	 * 
	 **/
	public void sortTableValues() throws NumberFormatException,
			FileNotFoundException {
		int row;

		intTableValues = new int[getTotalLineNumbers()][4];
		for (row = 0; row < getTotalLineNumbers() - 1; row++) {
			for (int col = 0; col < 4; col++) {
				intTableValues[row][col] = Integer
						.parseInt(readFileValues[row][col]);
			}
		}

		combineValues();

	}

	private void combineValues() throws FileNotFoundException {

		for (int row = 0; row < getTotalLineNumbers() - 1; row++) {
			setQuantity(intTableValues[row][0], intTableValues[row][1],
					intTableValues[row][2], intTableValues[row][3]);
		}
		readQuantity();
	}

	private void setQuantity(int i, int j, int k, int l) {

		quantity[i][j][k] += l; // kuso. KUSO!

	}

	private void readQuantity() {

		int lineAt = 0;

		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				for (int k = 0; k < 4; k++) {
					if (quantity[i][j][k] != 0) {
						makeNewArray(i, j, k, quantity[i][j][k], lineAt);
						lineAt++;
					}
				}
	}

	private void makeNewArray(int i, int j, int k, int quantity_, int row) {

		writeTableValues[row][0] = ItemStock.setIndexEquivalentType(i);
		writeTableValues[row][1] = ItemStock.setIndexEquivalentColor(j);
		writeTableValues[row][2] = ItemStock.setIndexEquivalentSize(k);
		writeTableValues[row][3] = String.valueOf(quantity_);

	}

	private boolean searchQuantity(int i, int j, int k, int l) {

		if (quantity[i][j][k] != 0) {
			if (l <= quantity[i][j][k])
				return true;
		}

		return false;

	}

	private void deleteQuantity(int iType, int iColor, int iSize, int iQuantity) {
		if (iQuantity <= quantity[iType][iColor][iSize]) {
			quantity[iType][iColor][iSize] = quantity[iType][iColor][iSize]
					- iQuantity;
			if (quantity[iType][iColor][iSize] < 0)
				quantity[iType][iColor][iSize] = 0;
		}

		else
			quantity[iType][iColor][iSize] = 0;
	}
}
