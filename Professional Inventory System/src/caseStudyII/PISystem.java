package caseStudyII;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class PISystem extends JFrame {

	/**
	 * PISystem - Professional Inventory System
	 * 
	 * A simple inventory system consisting of basic Add, Delete, and Search
	 * functionalities.
	 * 
	 */
	private static final long serialVersionUID = -4721063361921293336L;
	private JPanel contentPane; // main panel
	JScrollPane scrollPane = new JScrollPane(); // for fitting table into a pane
	private JTable tblMain;
	private SpinnerNumberModel spinnerSet = new SpinnerNumberModel(0, 0, 65535,
			1); // Spinner limits and configuration
	protected String filepath; // filepath opened/saved
	protected String key = new String("PISystemFile"); // a key that verifies if
														// a file is a PIS File
	protected boolean isFileLoaded = false;

	@SuppressWarnings("rawtypes")
	private Vector<Vector> rowData = new Vector<Vector>(); // table data handler
															// for display
	@SuppressWarnings("rawtypes")
	private Vector<Vector> rowIntData = new Vector<Vector>(); // table data
																// handler for
																// file - out
	private Vector<String> colHeader = new Vector<String>(Arrays.asList("Type",
			"Color", "Size", "Quantity")); // table headers

	private String[] popupType = ItemStock.itemChoiceType; // from class
															// ItemStock
	private String[] popupColor = ItemStock.itemChoiceColor;
	private String[] popupSize = ItemStock.itemChoiceSize;

	private String[][] readFileValues; // (String) temporary array container for
										// file - read
	private int[][] intTableValues; // (int)
	private int[][][] quantity; // classifier. [type][color][size] = quantity

	private JMenuItem mntmSave;

	private JToggleButton tglbtnSearch; // toggle buttons. buttons that act as
										// checkboxes
	private JToggleButton tglbtnDelete;
	private JToggleButton tglbtnAdd;
	private Choice choiceSize;
	private Choice choiceColor;
	private Choice choiceType;
	private JSpinner spinnerNo; // spinner. integer selection. with knobs for
								// increment and derement
	protected boolean isSaved = true;
	private JLabel lblState; // for pnlState

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PISystem frame = new PISystem();
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
	public PISystem() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (isSaved || isFileLoaded == false)
					System.exit(0);
				else if (isFileLoaded
						&& JOptionPane.OK_OPTION == JOptionPane
								.showConfirmDialog(
										null,
										"Your file has not been saved. Save the file before exiting?",
										"Warning!", JOptionPane.YES_NO_OPTION,
										JOptionPane.WARNING_MESSAGE)) {
					try {
						lblState.setText("Saving File!");
						saveFile();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					lblState.setText("File Not Saved");
				}
			}
		});
		setTitle("Professional Inventory System");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 334); // uses Absolute Layouting

		JMenuBar menuBar = new JMenuBar(); // a menu instantiation
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// XXX New
				lblState.setText("New File");
				newFile();
				if (isFileLoaded)
					tblMain.repaint();
				setTitle(filepath + " - PISystem 3.0");
			}
		});
		mnFile.add(mntmNew);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// XXX Open
				lblState.setText("Open File");
				openFile();
				if (isFileLoaded)
					tblMain.repaint();
				setTitle(filepath + " - PISystem 3.0");
			}
		});
		mnFile.add(mntmOpen);

		mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveFile();
					lblState.setText("File Saved!");
					isSaved = true;
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		mntmSave.setEnabled(false);
		mnFile.add(mntmSave);

		JSeparator separator = new JSeparator(); // a menu item separator
		mnFile.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblState.setText("Exiting!");
				if (isSaved || isFileLoaded == false)
					System.exit(0);
				else if (JOptionPane.OK_OPTION == JOptionPane
						.showConfirmDialog(
								null,
								"Your file has not been saved. Are you sure you want to quit?",
								"Warning!", JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.WARNING_MESSAGE))
					System.exit(0);

			}
		});
		mnFile.add(mntmExit);

		JMenu mnAbout = new JMenu("Info");
		menuBar.add(mnAbout);
		/** About frame */
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblState.setText("About the Program...");
				About dialog = new About();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		mnAbout.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setVisible(false);

		JPanel pnlTools = new JPanel(); // the panel for the tools, choices,
										// spinner, button, and toggle buttons
		pnlTools.setBounds(10, 11, 180, 245);
		contentPane.add(pnlTools);
		pnlTools.setLayout(null);

		choiceType = new Choice();
		choiceType.setBounds(63, 59, 102, 20);
		pnlTools.add(choiceType);

		choiceColor = new Choice();
		choiceColor.setBounds(63, 85, 102, 20);
		pnlTools.add(choiceColor);

		choiceSize = new Choice();
		choiceSize.setBounds(63, 111, 102, 20);
		pnlTools.add(choiceSize);

		for (int k = 0; k < popupType.length; k++)
			choiceType.add(popupType[k]);
		for (int k = 0; k < popupColor.length; k++)
			choiceColor.add(popupColor[k]);
		for (int k = 0; k < popupSize.length; k++)
			choiceSize.add(popupSize[k]);

		JLabel lblType = new JLabel("Type");
		lblType.setLabelFor(choiceType);
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblType.setBounds(29, 60, 28, 19);
		pnlTools.add(lblType);

		JLabel lblColor = new JLabel("Color");
		lblColor.setLabelFor(choiceColor);
		lblColor.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblColor.setBounds(29, 86, 28, 19);
		pnlTools.add(lblColor);

		JLabel lblSize = new JLabel("Size");
		lblSize.setLabelFor(lblSize);
		lblSize.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSize.setBounds(36, 112, 21, 19);
		pnlTools.add(lblSize);

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblQuantity.setBounds(10, 140, 47, 19);
		pnlTools.add(lblQuantity);

		tglbtnAdd = new JToggleButton("Add");
		tglbtnAdd.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) { // sets other choices
															// as not selected
				lblState.setText("Add Item to Inventory");
				tglbtnDelete.setSelected(false);
				tglbtnSearch.setSelected(false);
			}
		});
		tglbtnAdd.setFont(new Font("Tahoma", Font.PLAIN, 10));
		tglbtnAdd.setToolTipText("Add item(s) to inventory");
		tglbtnAdd.setBounds(0, 11, 57, 23);
		pnlTools.add(tglbtnAdd);

		tglbtnDelete = new JToggleButton("Erase");
		tglbtnDelete.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				lblState.setText("Remove Item(s) from Inventory");
				tglbtnAdd.setSelected(false);
				tglbtnSearch.setSelected(false);
			}
		});
		tglbtnDelete.setFont(new Font("Tahoma", Font.PLAIN, 10));
		tglbtnDelete.setToolTipText("Remove item(s) from inventory");
		tglbtnDelete.setBounds(55, 11, 63, 23);
		pnlTools.add(tglbtnDelete);

		tglbtnSearch = new JToggleButton("Find");
		tglbtnSearch.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				lblState.setText("Look for item(s) from inventory");
				tglbtnDelete.setSelected(false);
				tglbtnAdd.setSelected(false);
			}
		});
		tglbtnSearch.setFont(new Font("Tahoma", Font.PLAIN, 10));
		tglbtnSearch.setToolTipText("Look for item(s) from inventory");
		tglbtnSearch.setBounds(116, 11, 63, 23);
		pnlTools.add(tglbtnSearch);

		spinnerNo = new JSpinner(spinnerSet);
		lblQuantity.setLabelFor(spinnerNo);
		spinnerNo.setBounds(63, 137, 102, 20);
		pnlTools.add(spinnerNo);

		JButton btnProcess = new JButton("PROCESS");
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// XXX PROCESS!

				if (tglbtnSearch.isSelected())
					searchInventory();
				if (tglbtnAdd.isSelected())
					addItem();

				if (tglbtnDelete.isSelected())
					deleteItem();

				drawTable();
			}

		});
		btnProcess.setToolTipText("Confirm input for processing");
		btnProcess.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnProcess.setBounds(35, 183, 112, 35);
		pnlTools.add(btnProcess);

		scrollPane.setBounds(200, 11, 299, 245);
		contentPane.add(scrollPane);

		JPanel pnlState = new JPanel();
		pnlState.setBounds(10, 260, 489, 20);
		contentPane.add(pnlState);
		pnlState.setLayout(new BorderLayout(0, 0));

		lblState = new JLabel("Professional Inventory System: Ready");
		lblState.setFont(new Font("Tahoma", Font.PLAIN, 10));
		pnlState.add(lblState, BorderLayout.EAST);

	}

	/**
	 * ***********************************************************************
	 * */
	// searchInventory() - does a manual search @ quantity[][][] classifier for
	// matches
	public void searchInventory() {
		int iType, iColor, iSize, iQuantity;

		iType = choiceType.getSelectedIndex();
		iColor = choiceColor.getSelectedIndex();
		iSize = choiceSize.getSelectedIndex();

		iQuantity = (Integer) spinnerNo.getValue();

		if (searchQuantity(iType, iColor, iSize, iQuantity))
			JOptionPane.showMessageDialog(null, "Type: " + popupType[iType]
					+ " | Color: " + popupColor[iColor] + " | Size: "
					+ popupSize[iSize] + " | Quantity: " + iQuantity
					+ " FOUND!", "Item Found", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, "Type: " + popupType[iType]
					+ " | Color: " + popupColor[iColor] + " | Size: "
					+ popupSize[iSize] + " | Quantity: " + iQuantity
					+ " NOT FOUND!", "Item NOT Found",
					JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * deleteItem() - deletes item and refreshes the table
	 * 
	 */
	public void deleteItem() {
		int iType, iColor, iSize, iQuantity;

		iType = choiceType.getSelectedIndex();
		iColor = choiceColor.getSelectedIndex();
		iSize = choiceSize.getSelectedIndex();

		iQuantity = (Integer) spinnerNo.getValue();

		int temp = iQuantity;

		if (searchQuantity(iType, iColor, iSize, iQuantity)) {
			deleteQuantity(iType, iColor, iSize, iQuantity);
			lblState.setText("Item Deleted!");
		} else
			JOptionPane.showMessageDialog(null,
					"Type: " + popupType[iType] + " | Color: "
							+ popupColor[iColor] + " | Size: "
							+ popupSize[iSize] + " | Quantity: " + temp
							+ " NOT FOUND!", "Item NOT Found",
					JOptionPane.ERROR_MESSAGE);
		isSaved = false;
		readQuantity();
		drawTable();
	}

	/**
	 * addItem() - gets information from tools and adds item specified to
	 * quantity[][][] classifier
	 * 
	 */
	public void addItem() {
		int iType, iColor, iSize, iQuantity;

		iType = choiceType.getSelectedIndex();
		iColor = choiceColor.getSelectedIndex();
		iSize = choiceSize.getSelectedIndex();
		iQuantity = (Integer) spinnerNo.getValue();

		setQuantity(iType, iColor, iSize, iQuantity);
		lblState.setText("Item Added!");
		isSaved = false;
		readQuantity();
		drawTable();
	}

	/**
	 * saveFile() writes all changes to file.
	 */
	public void saveFile() throws FileNotFoundException {
		PrintWriter outPutFile = new PrintWriter(new File(filepath));
		outPutFile.println(key);

		for (int j = 0; j < rowIntData.size(); j++)
			outPutFile.printf("%d %d %d %d\n",
					(rowIntData.elementAt(j)).elementAt(0),
					(rowIntData.elementAt(j)).elementAt(1),
					(rowIntData.elementAt(j)).elementAt(2),
					(rowIntData.elementAt(j)).elementAt(3));

		outPutFile.flush();
		outPutFile.close();
		isSaved = true;
	}

	/**
	 * drawTable(String [][]) does many things! it draws the table! yeah! and
	 * places it on the scrollPane. which is located at the right. and is very
	 * obvious.
	 */
	public void drawTable() {

		tblMain = new JTable(rowData, colHeader);
		tblMain.setShowGrid(false);
		tblMain.setColumnSelectionAllowed(false);
		tblMain.setRowSelectionAllowed(false);
		tblMain.setFillsViewportHeight(true);
		scrollPane.setViewportView(tblMain);
	}

	/**
	 * getTotalLineNumbers() ===================================================
	 * 
	 * returns the total number of lines in the file opened
	 * 
	 **/

	private int getTotalLineNumbers(File inputFile)
			throws FileNotFoundException {
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
	protected boolean isPISFile(File inputFile) throws FileNotFoundException {
		Scanner fileTestRead = new Scanner(new FileReader(inputFile));

		if (fileTestRead.next().equals(key) == false) {
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
	public void getStrTableValues(File inputFile) throws FileNotFoundException {

		Scanner fwScanner2 = new Scanner(new FileReader(inputFile));

		readFileValues = new String[getTotalLineNumbers(inputFile)][4];

		fwScanner2.nextLine();
		for (int row = 0; row < getTotalLineNumbers(inputFile) - 1; row++)
			for (int col = 0; col < 4; col++) {
				readFileValues[row][col] = fwScanner2.next();
			}

		sortTableValues(inputFile);
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
	public void sortTableValues(File inputFile) throws NumberFormatException,
			FileNotFoundException {
		int row;

		intTableValues = new int[getTotalLineNumbers(inputFile)][4];
		for (row = 0; row < getTotalLineNumbers(inputFile) - 1; row++) {
			for (int col = 0; col < 4; col++) {
				intTableValues[row][col] = Integer
						.parseInt(readFileValues[row][col]);
			}
		}

		combineValues(inputFile);

	}

	private void combineValues(File inputFile) throws FileNotFoundException {

		for (int row = 0; row < getTotalLineNumbers(inputFile) - 1; row++) {
			setQuantity(intTableValues[row][0], intTableValues[row][1],
					intTableValues[row][2], intTableValues[row][3]);
		}
		readQuantity();
	}

	public void setQuantity(int i, int j, int k, int l) {

		quantity[i][j][k] += l; // kuso. KUSO!

	}

	public void readQuantity() {

		rowData.clear();
		rowIntData.clear();

		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				for (int k = 0; k < 4; k++) {
					if (quantity[i][j][k] != 0) {
						makeNewArray(i, j, k, quantity[i][j][k]);
					}
				}
	}

	private void makeNewArray(int i, int j, int k, int quantity_) {

		Vector<String> colData = new Vector<String>(Arrays.asList(
				ItemStock.setIndexEquivalentType(i),
				ItemStock.setIndexEquivalentColor(j),
				ItemStock.setIndexEquivalentSize(k), String.valueOf(quantity_)));

		Vector<Integer> colIntData = new Vector<Integer>(Arrays.asList(i, j, k,
				quantity_));

		rowIntData.add(colIntData);
		rowData.add(colData);
	}

	private boolean searchQuantity(int i, int j, int k, int l) {

		if (quantity[i][j][k] != 0) {
			if (l <= quantity[i][j][k])
				return true;
		}

		return false;

	}

	public void deleteQuantity(int iType, int iColor, int iSize, int iQuantity) {
		if (iQuantity <= quantity[iType][iColor][iSize]) {
			quantity[iType][iColor][iSize] = quantity[iType][iColor][iSize]
					- iQuantity;
			if (quantity[iType][iColor][iSize] < 0)
				quantity[iType][iColor][iSize] = 0;
		}

		else
			quantity[iType][iColor][iSize] = 0;
	}

	/**
	 * newFile()
	 */
	// TODO newFile()
	public void newFile() {
		FileDialog dialogNew = new FileDialog(new Frame(),
				"Create New Database");

		dialogNew.setMode(FileDialog.SAVE);
		dialogNew.setVisible(true);

		try {
			if (!dialogNew.getFile().equals("")) {

				String outfile = dialogNew.getFile();

				File inputFile = new File(dialogNew.getDirectory() + outfile);
				filepath = new String(dialogNew.getDirectory() + outfile);
				signFile(inputFile);

				isFileLoaded = true;
			}
		} catch (Exception f) {
			System.out.println(f.getMessage());
		}// end try-catch

		rowData.clear();
		rowIntData.clear();
		quantity = new int[5][5][4];

		mntmSave.setEnabled(true);
		contentPane.setVisible(true);
		// drawTable(null);
	}

	public void signFile(File inputFile) throws FileNotFoundException {
		PrintWriter pwOutStream = new PrintWriter(inputFile);

		pwOutStream.println(key);
		pwOutStream.println("1 1 0 1");
		pwOutStream.flush();
		pwOutStream.close();
	}

	/**
	 * openFile()
	 */
	// TODO openFile()
	public void openFile() {
		FileDialog dialog = new FileDialog(new Frame(),
				"Open an Existing Database");
		File inputFile = null;
		rowData.clear();
		rowIntData.clear();
		quantity = new int[5][5][4];
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
					if (isPISFile(inputFile))
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
		} while (isFileLoaded == false);

		try {
			getStrTableValues(inputFile);
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		}

		mntmSave.setEnabled(true);
		contentPane.setVisible(true);
		drawTable();
	}
}
