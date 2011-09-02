package caseStudyII;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class KaNa extends Frame implements ActionListener {
	File rec = new File("records.txt");
	PrintWriter pr;
	BufferedReader rd;

	String type;
	String size;
	String color;
	String quantity;
	int pk = 0;

	Choice[] fields = new Choice[3];
	TextField tf = new TextField();
	Button bSearch = new Button("Search");
	Button bAdd = new Button("Add");
	Button bDelete = new Button("Delete");
	Button bExit = new Button("Exit");

	Panel entries = new Panel(new GridLayout(4, 2));
	Panel buttons = new Panel(new GridLayout(2, 2));

	public KaNa() {
		initialize();
	}

	private void initialize() {
		setSize(300, 200);
		setLayout(new GridLayout(2, 1));

		for (int i = 0; i < fields.length; i++)
			fields[i] = new Choice();
		entries.add(new Label("Type"));
		entries.add(fields[0]);
		entries.add(new Label("Size"));
		entries.add(fields[1]);
		entries.add(new Label("Color"));
		entries.add(fields[2]);
		entries.add(new Label("Quantity"));
		entries.add(tf);

		fields[0].add("Blouse");
		fields[0].add("Shorts");
		fields[0].add("Polo");
		fields[0].add("Pants");

		fields[1].add("Small");
		fields[1].add("Medium");
		fields[1].add("Large");
		fields[1].add("Extra Large");

		fields[2].add("Blue");
		fields[2].add("Green");
		fields[2].add("Red");
		fields[2].add("Yellow");
		fields[2].add("White");

		buttons.add(bAdd);
		buttons.add(bDelete);
		buttons.add(bSearch);
		buttons.add(bExit);

		add(entries);
		add(buttons);

		bAdd.addActionListener(this);
		bDelete.addActionListener(this);
		bSearch.addActionListener(this);
		bExit.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == bExit) {
				dispose();
			} else if (e.getSource() == bDelete) {

			} else if (e.getSource() == bSearch) {

			} else if (e.getSource() == bAdd) {
				pr = new PrintWriter(rec);
				String toPrint = pk + ":";
				toPrint += fields[0].getSelectedItem() + ":";
				toPrint += fields[1].getSelectedItem() + ":";
				toPrint += fields[2].getSelectedItem() + ":";
				toPrint += tf.getText();
				pr.println(toPrint);
				pr.close();
				JOptionPane
						.showMessageDialog(null, "Entry successfully added.");
			}
		} catch (Exception ex) {
		}
		tf.setText("");
		fields[0].select(0);
		fields[1].select(0);
		fields[2].select(0);
	}

	public static void main(String[] args) {
		KaNa k = new KaNa();
		k.setVisible(true);
	}

}
