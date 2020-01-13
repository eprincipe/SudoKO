package gui;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.Color;

import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.SoftBevelBorder;

import logic.SudokuBacktracking;

public class MainWindow {

	private JFrame frmSudoko;
	private static final int n = 9;
	private JTextField sudokuTextField[][] = new JTextField[n][n];
	private int xstf, ystf;
	private final Color LSALMON = new Color(255,160,122);
	private final Color BISQUE = new Color(255,228,196);
	private final Color CRIMSON = new Color(220,20,60);
	private final Color IVORY = new Color(255,255,240);	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmSudoko.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSudoko = new JFrame();
		frmSudoko.setTitle("SudoKO");
		frmSudoko.setBounds(100, 100, 450, 300);
		frmSudoko.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{10, 10, 0};
		gridBagLayout.rowHeights = new int[]{10, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frmSudoko.getContentPane().setLayout(gridBagLayout);
		
		JSplitPane splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridwidth = 2;
		gbc_splitPane.insets = new Insets(0, 0, 0, 5);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		frmSudoko.getContentPane().add(splitPane, gbc_splitPane);
		
		JPanel controlPanel = new JPanel();
		splitPane.setLeftComponent(controlPanel);
		GridBagLayout gbl_controlPanel = new GridBagLayout();
		gbl_controlPanel.columnWidths = new int[] {0, 2};
		gbl_controlPanel.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 3};
		gbl_controlPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_controlPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		controlPanel.setLayout(gbl_controlPanel);
		
		JLabel lblDifficulty = new JLabel("Difficulty");
		GridBagConstraints gbc_lblDifficulty = new GridBagConstraints();
		gbc_lblDifficulty.insets = new Insets(0, 0, 5, 0);
		gbc_lblDifficulty.gridx = 0;
		gbc_lblDifficulty.gridy = 1;
		controlPanel.add(lblDifficulty, gbc_lblDifficulty);
		
		JComboBox<String> difficultyComboBox = new JComboBox<String>();
		difficultyComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Noob", "Casul", "Git Gud"}));
		difficultyComboBox.setSelectedIndex(0);
		GridBagConstraints gbc_difficultyComboBox = new GridBagConstraints();
		gbc_difficultyComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_difficultyComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_difficultyComboBox.gridx = 0;
		gbc_difficultyComboBox.gridy = 2;
		controlPanel.add(difficultyComboBox, gbc_difficultyComboBox);
		
		JButton btnStart = new JButton("Start!");
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.insets = new Insets(0, 0, 5, 0);
		gbc_btnStart.gridx = 0;
		gbc_btnStart.gridy = 3;
		controlPanel.add(btnStart, gbc_btnStart);
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int grid[][] = SudokuBacktracking.generatePuzzle(difficultyComboBox.getSelectedIndex(), n);
				startPuzzle(grid);
			}
		});
		
		JButton btnHint = new JButton("Hint");
		GridBagConstraints gbc_btnHint = new GridBagConstraints();
		gbc_btnHint.insets = new Insets(0, 0, 5, 0);
		gbc_btnHint.gridx = 0;
		gbc_btnHint.gridy = 6;
		controlPanel.add(btnHint, gbc_btnHint);
		btnHint.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int grid[][] = generateGrid();
				int solution[][] = generateGrid();
				if(SudokuBacktracking.solve(solution)) {
					int i = 0, j = 0;
					boolean foundEmpty = false;
					while (i < n && !foundEmpty) {
						j = 0;
						while (j < n && !foundEmpty) {
							foundEmpty = grid[i][j] == 0;
							j++;
						}
						i++;
					}
					grid[i-1][j-1] = solution[i-1][j-1];
					updateGrid(grid);
				} else
					JOptionPane.showMessageDialog(null, "Too bad, no possible solution :(", "RIP in peace", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		JButton btnEndItAll = new JButton("End it all!");
		GridBagConstraints gbc_btnEndItAll = new GridBagConstraints();
		gbc_btnEndItAll.gridx = 0;
		gbc_btnEndItAll.gridy = 7;
		controlPanel.add(btnEndItAll, gbc_btnEndItAll);
		btnEndItAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int grid[][] = generateGrid();
				if(SudokuBacktracking.solve(grid))
					updateGrid(grid);
				else
					JOptionPane.showMessageDialog(null, "Too bad, no possible solution :(", "RIP in peace", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		JPanel sudokuPanel = new JPanel();
		splitPane.setRightComponent(sudokuPanel);
		GridBagLayout gbl_sudokuPanel = new GridBagLayout();
		gbl_sudokuPanel.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
		gbl_sudokuPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		sudokuPanel.setLayout(gbl_sudokuPanel);
		
		
		for (xstf = 0; xstf < n; xstf++) {
			for (ystf = 0; ystf < n; ystf++) {
				JTextField stf = new JTextField();
				
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = xstf;
				gbc_textField.gridy = ystf;
				sudokuPanel.add(stf, gbc_textField);
				stf.setColumns(10);
				stf.setHorizontalAlignment(JTextField.CENTER);
				stf.setBackground(getBackgroundColor(xstf, ystf));
				
				stf.addFocusListener(new FocusListener() {
					
					@Override
					public void focusLost(FocusEvent e) {
						stf.setBackground(getBackgroundColor(xstf, ystf)); 
					}
					
					@Override
					public void focusGained(FocusEvent e) {
						stf.setBackground(LSALMON);
						int i = 0, j = 0;
						boolean found = false;
						while(i < n && !found) {
							j = 0;
							while(j < n && !found) {
								found = sudokuTextField[i][j] == stf;
								j++;
							}
							i++;
						}
						xstf = i-1;
						ystf = j-1;
					}
				});
				
				stf.addKeyListener(new KeyAdapter() {
					
					@Override
					public void keyTyped(KeyEvent e) {
						if(stf.isEditable()) {
							stf.setText("");
							stf.transferFocus();
						}
					}
				});
				
				Action arrowUp = new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						sudokuTextField[xstf][ystf<=0?8:ystf-1].requestFocusInWindow();
					}
				};
				Action arrowDown = new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						sudokuTextField[xstf][ystf>=8?0:ystf+1].requestFocusInWindow();
					}
				};
				Action arrowLeft = new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						sudokuTextField[xstf<=0?8:xstf-1][ystf].requestFocusInWindow();
					}
				};
				Action arrowRight = new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						sudokuTextField[xstf>=8?0:xstf+1][ystf].requestFocusInWindow();
					}
				};
				InputMap im = stf.getInputMap();
				im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "arrow.up");
				im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "arrow.down");
				im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "arrow.left");
				im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "arrow.right");
				stf.getActionMap().put("arrow.up", arrowUp);
				stf.getActionMap().put("arrow.down", arrowDown);
				stf.getActionMap().put("arrow.left", arrowLeft);
				stf.getActionMap().put("arrow.right", arrowRight);
				stf.setBorder(new SoftBevelBorder(0));
				stf.setEditable(false);
				
				sudokuTextField[xstf][ystf] = stf;
			}
		}
		xstf=0;
		ystf=0;
	}
	
	public Color getBackgroundColor (int x, int y) {
		int qx = x/((int)Math.sqrt(n)), qy = y/((int)Math.sqrt(n));
		if(((qx % 2) == 0) ^ (qy % 2 == 0))
			return BISQUE;
		return IVORY;
	}
	
	public void startPuzzle(int grid[][]) {
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				if(grid[i][j] != 0) {
					sudokuTextField[i][j].setText(grid[i][j]+"");
					sudokuTextField[i][j].setEditable(false);
					sudokuTextField[i][j].setFont(sudokuTextField[i][j].getFont().deriveFont(Font.BOLD));
					sudokuTextField[i][j].setForeground(CRIMSON);
				} else {
					sudokuTextField[i][j].setText("");
					sudokuTextField[i][j].setEditable(true);
					sudokuTextField[i][j].setFont(sudokuTextField[i][j].getFont().deriveFont(Font.BOLD));
					sudokuTextField[i][j].setForeground(Color.BLACK);
				}
	}
	
	public void updateGrid(int grid[][]) {
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				sudokuTextField[i][j].setText(grid[i][j]==0?"": grid[i][j] + "");
			}
		}
	}
	
	public int[][] generateGrid() {
		int grid[][] = new int[n][n];
		try {
			for(int i = 0; i < n; i++)
				for(int j = 0; j < n; j++) {
					String t = sudokuTextField[i][j].getText();
					if( t.compareTo("") == 0 || t.compareTo(" ") == 0)
						grid[i][j] = 0;
					else
						grid[i][j] = Integer.parseInt(t);
					
				}
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(null, "Only numbers in the boxes!", "No hacks, snowflake", JOptionPane.ERROR_MESSAGE);
		}
		return grid;
	}
}
