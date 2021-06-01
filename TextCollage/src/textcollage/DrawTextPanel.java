package textcollage;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * A panel that contains a large drawing area where strings
 * can be drawn.  The strings are represented by objects of
 * type DrawTextItem.  An input box under the panel allows
 * the user to specify what string will be drawn when the
 * user clicks on the drawing area.
 */
public class DrawTextPanel extends JPanel  {
	
	// As it now stands, this class can only show one string at at
	// a time!  The data for that string is in the DrawTextItem object
	// named theString.  (If it's null, nothing is shown.  This
	// variable should be replaced by a variable of type
	// ArrayList<DrawStringItem> that can store multiple items.
	
	/* **************************
	 * ****Part 1: ArrayLists****
	 * **************************
	 *  
	 * private DrawTextItem theString;  // changed with an ArrayList<DrawTextItem> !
	 * 
	 */
	private ArrayList<DrawTextItem> strings = new ArrayList<>();
	/* **************************
	 * ****Part 1: ArrayLists****
	 * **************************
	 *  
	 * END OF THE CHANGE
	 * 
	 */ 

	
	private Color currentTextColor = Color.BLACK;  // Color applied to new strings.
	/* ******************************************
	 * *******Part 3: Improve the program!*******
	 * ******************************************
	 *
	 * Added all commented functionalities
	 */
	private int currentTextSize;  // Size applied to new strings.
	private JComboBox<Integer> fontSizeList; // for choose the font size
	private double increase; // to increase the dimension of the string
	private JTextField magnification; // where the user inputs the value for the increase
	private boolean hasBorder; // border for text
	private JCheckBox border; // to select or not the border
	private double angleRotation; // value of angle of rotation
	private JTextField rotation; // where the user inputs the value for angle of rotation
	private Color currentBgTextColor = null; // the color of current background text color
	private double textTransparency; // the value of text color transparency
	private JTextField txtTransparency; // where the user inputs the value for the text transparency
	private double backgroundTransparency; // the value of background text color transparency
	private JTextField bgTransparency; // where the user inputs the value for the background color text transparency
	/* ******************************************
	 * *******Part 3: Improve the program!*******
	 * ******************************************
	 *
	 * END OF THE CHANGE
	 */

	private Canvas canvas;  // the drawing area.
	private JTextField input;  // where the user inputs the string that will be added to the canvas
	private SimpleFileChooser fileChooser;  // for letting the user select files
	private JMenuBar menuBar; // a menu bar with command that affect this panel
	private MenuHandler menuHandler; // a listener that responds whenever the user selects a menu command
	private JMenuItem undoMenuItem;  // the "Remove Item" command from the edit menu
	/* **************************
	 * ******Part 2: Files*******
	 * **************************
	 * 
	 */
	JMenuItem saveItem;
	/* **************************
	 * ******Part 2: Files*******
	 * **************************
	 *
	 * END OF THE CHANGE
	 * 
	 */
	
	
	/**
	 * An object of type Canvas is used for the drawing area.
	 * The canvas simply displays all the DrawTextItems that
	 * are stored in the ArrayList, strings.
	 */
	private class Canvas extends JPanel {
		Canvas() {
			setPreferredSize( new Dimension(800,600) );
			setBackground(Color.LIGHT_GRAY);
			setFont( new Font( "Serif", Font.BOLD, 24 ));
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			/* **************************
			 * ****Part 1: ArrayLists****
			 * **************************
			 *  
			 * if (theString != null)
			 * 	theString.draw(g);
			 */
			if(strings != null) {
				for(DrawTextItem item : strings) {
					if (item != null) {
						item.draw(g);
					}
				}
			}
			/* **************************
			 * ****Part 1: ArrayLists****
			 * **************************
			 *  
			 * END OF THE CHANGE
			 * 
			 */
		}
	}
	
	/**
	 * An object of type MenuHandler is registered as the ActionListener
	 * for all the commands in the menu bar.  The MenuHandler object
	 * simply calls doMenuCommand() when the user selects a command
	 * from the menu.
	 */
	private class MenuHandler implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			doMenuCommand( evt.getActionCommand());
		}
	}

	/**
	 * Creates a DrawTextPanel.  The panel has a large drawing area and
	 * a text input box where the user can specify a string.  When the
	 * user clicks the drawing area, the string is added to the drawing
	 * area at the point where the user clicked.
	 */
	public DrawTextPanel() {
		fileChooser = new SimpleFileChooser();
		/* **************************
		 * ******Part 2: Files*******
		 * **************************
		 * 
		 */
		saveItem = new JMenuItem("Save...");
		saveItem.setEnabled(false);
		/* **************************
		 * ******Part 2: Files*******
		 * **************************
		 * 
		 * END OF THE CHANGE
		 */
		undoMenuItem = new JMenuItem("Remove Item");
		undoMenuItem.setEnabled(false);
		menuHandler = new MenuHandler();
		setLayout(new BorderLayout(3,3));
		setBackground(Color.BLACK);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		canvas = new Canvas();
		add(canvas, BorderLayout.CENTER);
		JPanel bottom = new JPanel();
		bottom.add(new JLabel("Text:"));
		input = new JTextField("Hello World!", 12);
		bottom.add(input);
		/* ******************************************
		 * *******Part 3: Improve the program!*******
		 * ******************************************
		 *
		 * Added all commented functionalities
		 */
		Integer[] intSize = {6,12,18,24,30,36,42};
		fontSizeList = new JComboBox<Integer>(intSize);
		fontSizeList.setSelectedIndex(3);
		currentTextSize = (Integer) fontSizeList.getSelectedItem();
		bottom.add(new JLabel("Font size:"));
		bottom.add(fontSizeList);
		bottom.add(new JLabel("Magnification:"));
		magnification = new JTextField("1", 2);
		magnification.setToolTipText("Negative or positive numbers allowed, Zero not allowed!");
		bottom.add(magnification);
		border = new JCheckBox("Border", false);
		bottom.add(border);
		bottom.add(new JLabel("Rotation:"));
		rotation = new JTextField("0", 2);
		bottom.add(rotation);
		bottom.add(new JLabel("Txt Transparency:"));
		txtTransparency = new JTextField("0", 2);
		bottom.add(txtTransparency);
		bottom.add(new JLabel("Bg Transparency:"));
		bgTransparency = new JTextField("0", 2);
		bottom.add(bgTransparency);
		/* ******************************************
		 * *******Part 3: Improve the program!*******
		 * ******************************************
		 *
		 * END OF THE CHANGES
		 */
		add(bottom, BorderLayout.SOUTH);
		canvas.addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				doMousePress( e );
			}
		} );
	}
		
	/**
	 * This method is called when the user clicks the drawing area.
	 * A new string is added to the drawing area.  The center of
	 * the string is at the point where the user clicked.
	 * @param e the mouse event that was generated when the user clicked
	 */
	public void doMousePress( MouseEvent e ) {
		String text = input.getText().trim();
		if (text.length() == 0) {
			input.setText("Hello World!");
			text = "Hello World!";
		}
		/* ******************************************
		 * *******Part 3: Improve the program!*******
		 * ******************************************
		 *
		 * Added all commented functionalities
		 */
		fontSizeList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentTextSize = fontSizeList.getItemAt(fontSizeList.getSelectedIndex());
			}
		});
		try {
			increase = Double.parseDouble(magnification.getText().trim());
		} catch (NumberFormatException err) {
			JOptionPane.showMessageDialog(this, 
					"Sorry, an error occurred while trying to get magnification:\n" + err + "\n"
						+ "You have to enter positive or negative values. Zero is not allowed!" + "\n"
							+ "The default value will be apply!");
			magnification.setText("1");
			increase = 1;
		}
		hasBorder = border.isSelected();
		try {
			angleRotation = Double.parseDouble(rotation.getText().trim());
		} catch (NumberFormatException err) {
			JOptionPane.showMessageDialog(this, 
					"Sorry, an error occurred while trying to get rotation:\n" + err + "\n"
						+ "You have to enter positive or negative integer values!" + "\n"
							+ "The default value will be apply!");
			rotation.setText("0");
			angleRotation = 0;
		}
		try {
			textTransparency = Double.parseDouble(txtTransparency.getText().trim());
			if(textTransparency < 0 || textTransparency > 1)
				throw new NumberFormatException("Text Transparency number not in the range 0 to 1");
		} catch (NumberFormatException err) {
			JOptionPane.showMessageDialog(this, 
					"Sorry, an error occurred while trying to get text transparency:\n" + err + "\n"
						+ "You have to enter number in the range 0 to 1!" + "\n"
						+ "The default value will be apply!");
			txtTransparency.setText("0");
			textTransparency = 0;
		}
		try {
			backgroundTransparency = Double.parseDouble(bgTransparency.getText().trim());
			if(backgroundTransparency < 0 || backgroundTransparency > 1)
				throw new NumberFormatException("Background Transparency number not in the range 0 to 1");
		} catch (NumberFormatException err) {
			JOptionPane.showMessageDialog(this, 
					"Sorry, an error occurred while trying to get background transparency:\n" + err + "\n"
							+ "You have to enter number in the range 0 to 1!" + "\n"
							+ "The default value will be apply!");
			bgTransparency.setText("0");
			backgroundTransparency = 0;
		}
		/* ******************************************
		 * *******Part 3: Improve the program!*******
		 * ******************************************
		 *
		 * END OF THE CHANGES
		 */
		
		DrawTextItem s = new DrawTextItem( text, e.getX(), e.getY() );
		s.setTextColor(currentTextColor);  // Default is null, meaning default color of the canvas (black).
		
		
		/* ******************************************
		 * *******Part 3: Improve the program!*******
		 * ******************************************
		 *
		 * Added all commented functionalities
		 */
		
//   SOME OTHER OPTIONS THAT CAN BE APPLIED TO TEXT ITEMS:
		s.setFont( new Font( "Serif", Font.ITALIC + Font.BOLD, currentTextSize ));  // Default is null, meaning font of canvas.
		// fixed a bug on DrawTextItem line 75 for magnification
		s.setMagnification(increase);  // Default is 1, meaning no magnification.
		s.setBorder(hasBorder);  // Default is false, meaning don't draw a border.
		s.setRotationAngle(angleRotation);  // Default is 0, meaning no rotation.
		s.setTextTransparency(textTransparency); // Default is 0, meaning text is not at all transparent.
		s.setBackground(currentBgTextColor);  // Default is null, meaning don't draw a background area.
		s.setBackgroundTransparency(backgroundTransparency);  // Default is 0, meaning background is not transparent.
		/* ******************************************
		 * *******Part 3: Improve the program!*******
		 * ******************************************
		 *
		 * END OF THE CHANGES
		 */
		
		
		/* **************************
		 * ****Part 1: ArrayLists****
		 * **************************
		 *
		 * theString = s;  // Set this string as the ONLY string to be drawn on the canvas!
		 */
		strings.add(s); // add the DrawTextItem to the ArrayList
		/* **************************
		 * ****Part 1: ArrayLists****
		 * **************************
		 *
		 * END OF THE CHANGE
		 */
		undoMenuItem.setEnabled(true);
		saveItem.setEnabled(true);
		canvas.repaint();
	}
	
	/**
	 * Returns a menu bar containing commands that affect this panel.  The menu
	 * bar is meant to appear in the same window that contains this panel.
	 */
	public JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			
			String commandKey; // for making keyboard accelerators for menu commands
			if (System.getProperty("mrj.version") == null)
				commandKey = "control ";  // command key for non-Mac OS
			else
				commandKey = "meta ";  // command key for Mac OS
			
			JMenu fileMenu = new JMenu("File");
			menuBar.add(fileMenu);
			/* **************************
			 * ******Part 2: Files*******
			 * **************************
			 *
			 * JMenuItem saveItem = new JMenuItem("Save...");
			 */
			saveItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "N"));
			saveItem.addActionListener(menuHandler);
			fileMenu.add(saveItem);
			JMenuItem openItem = new JMenuItem("Open...");
			openItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "O"));
			openItem.addActionListener(menuHandler);
			fileMenu.add(openItem);
			fileMenu.addSeparator();
			JMenuItem saveImageItem = new JMenuItem("Save Image...");
			saveImageItem.addActionListener(menuHandler);
			fileMenu.add(saveImageItem);
			
			JMenu editMenu = new JMenu("Edit");
			menuBar.add(editMenu);
			undoMenuItem.addActionListener(menuHandler); // undoItem was created in the constructor
			undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "Z"));
			editMenu.add(undoMenuItem);
			editMenu.addSeparator();
			JMenuItem clearItem = new JMenuItem("Clear");
			clearItem.addActionListener(menuHandler);
			editMenu.add(clearItem);
			
			JMenu optionsMenu = new JMenu("Options");
			menuBar.add(optionsMenu);
			JMenuItem colorItem = new JMenuItem("Set Text Color...");
			colorItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "T"));
			colorItem.addActionListener(menuHandler);
			optionsMenu.add(colorItem);
			JMenuItem bgColorItem = new JMenuItem("Set Background Color...");
			bgColorItem.addActionListener(menuHandler);
			optionsMenu.add(bgColorItem);
			/* ******************************************
			 * *******Part 3: Improve the program!*******
			 * ******************************************
			 *
			 * Added all commented functionalities
			 */
			JMenuItem bgTextColor = new JMenuItem("Set Background Text Color...");
			bgTextColor.addActionListener(menuHandler);
			optionsMenu.add(bgTextColor);
			/* ******************************************
			 * *******Part 3: Improve the program!*******
			 * ******************************************
			 *
			 * END OF THE CHANGES
			 */
			
		}
		return menuBar;
	}
	
	/**
	 * Carry out one of the commands from the menu bar.
	 * @param command the text of the menu command.
	 */
	private void doMenuCommand(String command) {
		if (command.equals("Save...")) { // save all the string info to a file
			/* **************************
			 * ******Part 2: Files*******
			 * **************************
			 *
			 * JOptionPane.showMessageDialog(this, "Sorry, the Save command is not implemented.");
			 * 
			 */
			File textCollageFile = fileChooser.getOutputFile(this, "Select Text Collage File Name", "textCollage.txt");
			if(textCollageFile == null)
				return;
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(textCollageFile));
				Color backgroundColor = canvas.getBackground(); // get the object Color to get the single RGB colors
				pw.write(
						"BackgroundColor:" + backgroundColor.getAlpha() + "," + backgroundColor.getRed()
				 + "," + backgroundColor.getGreen() + "," + backgroundColor.getBlue() + "\n");
				for(DrawTextItem item : strings) {
					Color textColor = item.getTextColor();
					pw.write(
							"Text:" + item.getString() +
							" - TextFont:" + item.getFont().getName() +
							"," + item.getFont().getStyle() + "," + item.getFont().getSize() +
							" - TextColor:" + textColor.getAlpha() + "," + textColor.getRed() +
							"," + textColor.getGreen() + "," + textColor.getBlue() +
							" - Coordinates:" + item.getX() + "," + item.getY() + 
							" - Magnification:" + item.getMagnification() + 
							" - Border:" + item.getBorder() + 
							" - Rotation:" + item.getRotationAngle() + 
							" - TextTransparency:" + item.getTextTransparency() + 
							" - BackgroundTransparency:" + item.getBackgroundTransparency());
					if(item.getBackground() != null) {
						Color textBackgroundColor = item.getBackground();
						pw.write(
								" - TextBgolor:" + textBackgroundColor.getAlpha() + "," + textBackgroundColor.getRed() +
								"," + textBackgroundColor.getGreen() + "," + textBackgroundColor.getBlue());
					}
					pw.write("\n");
				}
				pw.close();
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Sorry, an error occurred while trying to save the text collage:\n" + e);
			}
			/* **************************
			 * ******Part 2: Files*******
			 * **************************
			 *
			 * END OF THE CHANGE
			 */
		}
		else if (command.equals("Open...")) { // read a previously saved file, and reconstruct the list of strings
			/* **************************
			 * ******Part 2: Files*******
			 * **************************
			 *
			 * JOptionPane.showMessageDialog(this, "Sorry, the Open command is not implemented.");
			 * 
			 */
			File textCollageFile = fileChooser.getInputFile(this, "textCollage.txt");
			ArrayList<DrawTextItem> stringsLoaded = new ArrayList<>();
			if(textCollageFile == null)
				return;
			try {
				Scanner scanner = new Scanner(textCollageFile);
				Color newBackground = canvas.getBackground(); // get the previous background
				// Each dataset is on one line. The background color is on the first,
				// each DrawTextItem on the others. Each set consists of the attribute
				// name ':' and values. If there are more values, they are separated by commas.
				while(scanner.hasNext()) {
					String line = scanner.nextLine();
					if(line.contains("BackgroundColor")) { // get first background color data
						String[] tokenize = line.split(":"); // the first element is the name
															 // the second is data
						String[] RGBcolors = tokenize[1].split(",");
						newBackground = new Color(Integer.parseInt(RGBcolors[1]), Integer.parseInt(RGBcolors[2]),
								Integer.parseInt(RGBcolors[3]), Integer.parseInt(RGBcolors[0]));
					} else {
						String[] tokenize = line.split(" - ");
						DrawTextItem item = buildDrawTextItem(tokenize); // a method to build the DrawTextItem
						if(item != null) { // add only all ist ok
							stringsLoaded.add(item);
						}
					}
				}
				scanner.close();
				if(stringsLoaded.size() > 0) { // it is 0 something went wrong or the file is empty
					strings.clear();
					strings.addAll(stringsLoaded);
					canvas.setBackground(newBackground);
					undoMenuItem.setEnabled(true);
					canvas.repaint(); // (you'll need this to make the new list of strings take effect)
				} else {
					JOptionPane.showMessageDialog(this, 
							"Sorry, the Text Collage File does not seem a valid file!");
				}
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Sorry, an error occurred while trying to open the Text Collage File:\n" + e);
			}
			/* **************************
			 * ******Part 2: Files*******
			 * **************************
			 *
			 * END OF THE CHANGE
			 */
		}
		else if (command.equals("Clear")) {  // remove all strings
			/* **************************
			 * ****Part 1: ArrayLists****
			 * **************************
			 *
			 * theString = null;   // Remove the ONLY string from the canvas.
			 */
			strings.clear(); // empties the ArrayList
			/* **************************
			 * ****Part 1: ArrayLists****
			 * **************************
			 *
			 * END OF THE CHANGE
			 */
			saveItem.setEnabled(false);
			undoMenuItem.setEnabled(false);
			canvas.repaint();
		}
		else if (command.equals("Remove Item")) { // remove the most recently added string
			/* **************************
			 * ****Part 1: ArrayLists****
			 * **************************
			 *
			 * theString = null;   // Remove the ONLY string from the canvas.
			 * undoMenuItem.setEnabled(false);
			 */
			strings.remove(strings.size() - 1); // remove only the last item added
			if(strings.isEmpty()) {
				// if ArrayList is empty disable these
				saveItem.setEnabled(false);
				undoMenuItem.setEnabled(false);
			} else {
				saveItem.setEnabled(true);
				undoMenuItem.setEnabled(true);
			}
			/* **************************
			 * ****Part 1: ArrayLists****
			 * **************************
			 *
			 * END OF THE CHANGE
			 */
			canvas.repaint();
		}
		else if (command.equals("Set Text Color...")) {
			Color c = JColorChooser.showDialog(this, "Select Text Color", currentTextColor);
			if (c != null)
				currentTextColor = c;
		}
		else if (command.equals("Set Background Color...")) {
			Color c = JColorChooser.showDialog(this, "Select Background Color", canvas.getBackground());
			if (c != null) {
				canvas.setBackground(c);
				canvas.repaint();
			}
		}
		/* ******************************************
		 * *******Part 3: Improve the program!*******
		 * ******************************************
		 *
		 * Added all commented functionalities
		 */
		else if (command.equals("Set Background Text Color...")) {
			Color c = JColorChooser.showDialog(this, "Select Background Text Color", canvas.getBackground());
			if (c != null) {
				currentBgTextColor = c;
				canvas.repaint();
			}
		}
		/* ******************************************
		 * *******Part 3: Improve the program!*******
		 * ******************************************
		 *
		 * END OF THE CHANGES
		 */
		else if (command.equals("Save Image...")) {  // save a PNG image of the drawing area
			File imageFile = fileChooser.getOutputFile(this, "Select Image File Name", "textimage.png");
			if (imageFile == null)
				return;
			try {
				// Because the image is not available, I will make a new BufferedImage and
				// draw the same data to the BufferedImage as is shown in the panel.
				// A BufferedImage is an image that is stored in memory, not on the screen.
				// There is a convenient method for writing a BufferedImage to a file.
				BufferedImage image = new BufferedImage(canvas.getWidth(),canvas.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				Graphics g = image.getGraphics();
				g.setFont(canvas.getFont());
				canvas.paintComponent(g);  // draws the canvas onto the BufferedImage, not the screen!
				boolean ok = ImageIO.write(image, "PNG", imageFile); // write to the file
				if (ok == false)
					throw new Exception("PNG format not supported (this shouldn't happen!).");
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Sorry, an error occurred while trying to save the image:\n" + e);
			}
		}
	}
	
	/* **************************
	 * ******Part 2: Files*******
	 * **************************
	 *
	 */
	/**
	 * The method parse the line and buil a DrawTextItem element
	 * @param dataLine
	 * @return a DrawTextItem
	 */
	private DrawTextItem buildDrawTextItem(String[] dataLine) {
		DrawTextItem theString = null;
		for(String item : dataLine) {
			String[] tokenize = item.split(":");
			switch(tokenize[0]) { // for each attribute get data and build DrawTextItem
				case "Text":
					theString = new DrawTextItem(tokenize[1]);
					break;
				case "TextFont":
					if(theString != null) {
						String[] fontElements = tokenize[1].split(",");
						theString.setFont(new Font(fontElements[0], Integer.parseInt(fontElements[1]), Integer.parseInt(fontElements[2])));
					}
					break;
				case "TextColor":
					if(theString != null) {
						String[] RGBcolors = tokenize[1].split(",");
						Color textColor = new Color(Integer.parseInt(RGBcolors[1]), Integer.parseInt(RGBcolors[2]),
								Integer.parseInt(RGBcolors[3]), Integer.parseInt(RGBcolors[0]));
						theString.setTextColor(textColor);
					}
					break;
				case "Coordinates":
					if(theString != null) {
						String[] coordinates = tokenize[1].split(",");
						theString.setX(Integer.parseInt(coordinates[0]));
						theString.setY(Integer.parseInt(coordinates[1]));
					}
					break;
				case "Magnification":
					if(theString != null) {
						theString.setMagnification(Double.parseDouble(tokenize[1]));
					}
					break;
				case "Border":
					if(theString != null) {
						theString.setBorder(Boolean.parseBoolean(tokenize[1]));
					}
					break;
				case "Rotation":
					if(theString != null) {
						theString.setRotationAngle(Double.parseDouble(tokenize[1]));
					}
					break;
				case "TextTransparency":
					if(theString != null) {
						theString.setTextTransparency(Double.parseDouble(tokenize[1]));
					}
					break;
				case "TextBgolor":
					if(theString != null) {
						String[] RGBcolors = tokenize[1].split(",");
						Color textBgColor = new Color(Integer.parseInt(RGBcolors[1]), Integer.parseInt(RGBcolors[2]),
								Integer.parseInt(RGBcolors[3]), Integer.parseInt(RGBcolors[0]));
						theString.setBackground(textBgColor);
					}
					break;
				case "BackgroundTransparency":
					if(theString != null) {
						theString.setBackgroundTransparency(Double.parseDouble(tokenize[1]));
					}
					break;
				default:
					// nothing
			}
		}
		return theString;
	}
	/* **************************
	 * ******Part 2: Files*******
	 * **************************
	 *
	 * END OF THE CHANGE
	 */

}
