package organizerWdatkow;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class Main extends JFrame implements ActionListener {

/*	Hierarchia paneli
	1. ContentPane
	
	1.1 calendarPanelMain
		1.1.1 calendarPanelHeader
		1.1.2 calendarPanelDaysOfWeekHeader
		1.1.3 calendarPanelDays
			1.1.3.1 calendarPanelSingleDay
				1.1.3.1.1 calendarPanelDayHeader
				1.1.3.1.2 calendarPanelExpenditureList
				1.1.3.1.3 calendarPanelExpenditureSum
	*/
	
	
	public String[] namesOfMonths = { "Styczeń", "Luty", "Marzec", "Kwiecień",
			"Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik",
			"Listopad", "Grudzień" };
	public Integer[] yearsOfCalendar = new Integer[16];

	// panele grupujace obszar kalendarza
	JPanel calendarPanelMain, calendarPanelHeader,
			calendarPanelDaysOfWeekHeader, calendarPanelDays;

	// panele poza kalendarzemw głównym oknie
	JPanel buttonsPanel;

	// buttony
	JButton bAdd, bAddCategory, bRefresh;

	// comboBoxy
	JComboBox monthsCombo, yearsCombo;

	final String[] daysOfWeekHeaders = { "Poniedziałek", "Wtorek", "Środa",
			"Czwartek", "Piątek", "Sobota", "Niedziela" };
	JLabel[] lDaysOfWeekHeaders = new JLabel[daysOfWeekHeaders.length];

	// 1.1.3 calendarPanelDays
	// 1.1.3.1 calendarPanelSingleDay
	// 1.1.3.1.1 calendarPanelDayHeader
	// 1.1.3.1.2 calendarPanelExpenditureList
	// 1.1.3.1.3 calendarPanelExpenditureSum

	final int sizeOfTabOfPanelsOfDaysInMonth = 42; // size 7*6 [7 columns * 6
													// rows ]
	JPanel[] calendarPanelSingleDayTab = new JPanel[sizeOfTabOfPanelsOfDaysInMonth];
	JPanel[] calendarPanelDayHeaderTab = new JPanel[sizeOfTabOfPanelsOfDaysInMonth];
	JPanel[] calendarPanelExpenditureListTab = new JPanel[sizeOfTabOfPanelsOfDaysInMonth];
	JPanel[] calendarPanelExpenditureSum = new JPanel[sizeOfTabOfPanelsOfDaysInMonth];

	// 1.1.3.1.1 calendarPanelDayHeader
	String[] dayHeaderStringTab = new String[sizeOfTabOfPanelsOfDaysInMonth];
	JLabel[] dayHeaderLabelTab = new JLabel[sizeOfTabOfPanelsOfDaysInMonth];

	// 1.1.3.1.2 calendarPanelExpenditureList
	JTextArea[] expenditureListTab = new JTextArea[sizeOfTabOfPanelsOfDaysInMonth];
	ExpenditureTableModel[] expenditureTableModelTab = new ExpenditureTableModel[sizeOfTabOfPanelsOfDaysInMonth];
	JTable[] expenditureTableTab = new JTable[sizeOfTabOfPanelsOfDaysInMonth];
	JScrollPane[] expenditureTableScrollPaneTab = new JScrollPane[sizeOfTabOfPanelsOfDaysInMonth];

	// 1.1.3.1.3 calendarPanelExpenditureSum
	JLabel[] expenditureSumLabelTab = new JLabel[sizeOfTabOfPanelsOfDaysInMonth];
	JTextField[] expenditureSumTextFieldTab = new JTextField[sizeOfTabOfPanelsOfDaysInMonth];

	//======== JPopupMenu on RightClick on JTable
	
		JPopupMenu tablePopupMenu;
		JMenuItem miAdd;
		JMenuItem miDelete;
		JTable sourceTable;
			
	// ================================konstruktor - build GUI
	public Main() {

		setPreferredSize(new Dimension(1200, 600));
		setTitle("Organizer wydatków");

		Container cp = getContentPane(); // cp - ContentPane

		// wypełnienie tablicy lat objętych opracowaniem - modelowo lata 2015 -
		// 2030
		for (int i = 0; i < yearsOfCalendar.length; i++) {
			int yearStart = 2015;
			yearsOfCalendar[i] = yearStart + i;
		}
		

		// ====================== okno główne

		bAdd = new JButton("Dodaj");
		bAddCategory = new JButton("Dodaj kategorię");
		bRefresh = new JButton("Odśwież");
		bRefresh.addActionListener(this);

		calendarPanelMain = new JPanel();
		calendarPanelMain.setBorder(BorderFactory.createEtchedBorder());
		buttonsPanel = new JPanel();
		buttonsPanel.setBorder(BorderFactory.createEtchedBorder());

		//======== JPopupMenu on RightClick on JTable
		
			tablePopupMenu = new JPopupMenu();
			miAdd = new JMenuItem("Dodaj");
			miAdd.addActionListener(this);
			miDelete = new JMenuItem("Usuń");
			miDelete.addActionListener(this);
			
			tablePopupMenu.add(miAdd);
			tablePopupMenu.add(miDelete);
			
		
		// 1.
		cp.setLayout(new MigLayout("", "[grow]", "[][]50[]"));
		// 1.1
		cp.add(calendarPanelMain, "wrap,growx");

		JScrollPane scrollPane = new JScrollPane(calendarPanelMain);
		scrollPane.setPreferredSize(new Dimension(400, 400));
		cp.add(scrollPane, "wrap, growx");
		// 1.2
		cp.add(buttonsPanel, "growx");

		// buttonsPanel.add(bAdd);
		// buttonsPanel.add(bAddCategory);
		buttonsPanel.add(bRefresh);
		// ======================================================
		// obszar kalendarza
		// 1.1
		calendarPanelMain.setLayout(new MigLayout("flowx", "[1100px]",
				"[min][min][pref]"));

		// 1.1.1
		calendarPanelHeader = new JPanel();
		calendarPanelHeader.setBorder(BorderFactory.createEtchedBorder());

		// 1.1.2
		calendarPanelDaysOfWeekHeader = new JPanel();
		calendarPanelDaysOfWeekHeader.setBorder(BorderFactory
				.createEtchedBorder());

		// 1.1.3
		calendarPanelDays = new JPanel();
		calendarPanelDays.setBorder(BorderFactory.createEtchedBorder());

		calendarPanelMain.add(calendarPanelHeader, "grow, wrap ");
		calendarPanelMain.add(calendarPanelDaysOfWeekHeader, "grow, wrap");
		calendarPanelMain.add(calendarPanelDays, "grow");

		// ===============================================
		// obszar calendarPanelHeader 1.1.1

		calendarPanelHeader.setLayout(new MigLayout("center",
				"[center][center]", "[]"));

		monthsCombo = new JComboBox(namesOfMonths);
		yearsCombo = new JComboBox(yearsOfCalendar);
		monthsCombo.setPreferredSize(new Dimension(100, 30));
		yearsCombo.setPreferredSize(new Dimension(100, 20));

		yearsCombo.setSelectedIndex(2); // ustawiam 2017 rok
		monthsCombo.setSelectedIndex(10); // ustawiam Listopad

		calendarPanelHeader.add(monthsCombo);
		calendarPanelHeader.add(yearsCombo);

		// =================================================
		// obszar calendarDaysOfWeekHeader 1.1.2

		calendarPanelDaysOfWeekHeader.setLayout(new MigLayout("fill",
				"[center][center][center][center][center][center][center]",
				"[]"));

		for (int i = 0; i < lDaysOfWeekHeaders.length; i++) {
			lDaysOfWeekHeaders[i] = new JLabel(daysOfWeekHeaders[i]);

		}

		for (int i = 0; i < lDaysOfWeekHeaders.length; i++) {
			calendarPanelDaysOfWeekHeader.add(lDaysOfWeekHeaders[i]);
		}

		// ====================================
		// obszar calendarPanelDays 1.1.3

		calendarPanelDays
				.setLayout(new MigLayout(
						"wrap",
						"[center, sg][center,sg][center,sg][center,sg][center,sg][center,sg][center,sg]",
						"[sg][sg][sg][sg][sg][sg]"));

		// ============================

		for (int i = 0; i < calendarPanelSingleDayTab.length; i++) { // tworzenie
																		// paneli
																		// w
																		// tablicach
			JPanel singleDayPanel = new JPanel();
			singleDayPanel.setBorder(BorderFactory.createEtchedBorder());
			JPanel dayHeaderPanel = new JPanel();
			dayHeaderPanel.setBorder(BorderFactory.createEtchedBorder());
			JPanel expenditureListPanel = new JPanel();
			expenditureListPanel.setBorder(BorderFactory.createEtchedBorder());
			JPanel expenditureSumPanel = new JPanel();
			expenditureSumPanel.setBorder(BorderFactory.createEtchedBorder());

			calendarPanelSingleDayTab[i] = singleDayPanel;
			calendarPanelDayHeaderTab[i] = dayHeaderPanel;
			calendarPanelExpenditureListTab[i] = expenditureListPanel;
			calendarPanelExpenditureSum[i] = expenditureSumPanel;

			// ================
			calendarPanelDays.add(calendarPanelSingleDayTab[i]);
			// =================

			calendarPanelSingleDayTab[i].setLayout(new MigLayout("wrap, fill",
					"[center,fill]", "[c,min][c,100px][c,min]"));

			calendarPanelSingleDayTab[i].add(calendarPanelDayHeaderTab[i]);
			calendarPanelSingleDayTab[i]
					.add(calendarPanelExpenditureListTab[i]);
			calendarPanelSingleDayTab[i].add(calendarPanelExpenditureSum[i]);

			// ========================

			dayHeaderStringTab[i] = Integer.toString(i + 1);
			dayHeaderLabelTab[i] = new JLabel(dayHeaderStringTab[i]);
			// =======================!!!!!!

			//expenditureListTab[i] = new JTextArea(20, 20);

			expenditureTableModelTab[i] = new ExpenditureTableModel();
			expenditureTableTab[i] = new JTable(expenditureTableModelTab[i]);
			expenditureTableTab[i].setToolTipText(Integer.toString(i));
			//dodaje PopupMenu do tabel
			expenditureTableTab[i].addMouseListener(new MouseAdapter(){
				
	            public void mouseReleased(MouseEvent e)
	            {
	                if (e.isPopupTrigger())
	                {
	                    sourceTable = (JTable)e.getSource();
	                    int row = sourceTable.rowAtPoint( e.getPoint() );
	                    int column = sourceTable.columnAtPoint( e.getPoint() );

	                    if (! sourceTable.isRowSelected(row))
	                        sourceTable.changeSelection(row, column, false, false);
	                    
	                    tablePopupMenu.show(e.getComponent(), e.getX(), e.getY());
	                
	                }
	            }
				
			});
			//end MouseAdapter
			
			expenditureTableScrollPaneTab[i] = new JScrollPane(
					expenditureTableTab[i]);
			expenditureTableTab[i].setFillsViewportHeight(true);
			// ===============================

			expenditureSumLabelTab[i] = new JLabel("Suma: ");
			expenditureSumTextFieldTab[i] = new JTextField(20);

			// ===========================
			// dodawanie komponenetow do paneli
			// =====
			calendarPanelDayHeaderTab[i].setLayout(new MigLayout("fill",
					"[center]", "[]"));
			calendarPanelDayHeaderTab[i].add(dayHeaderLabelTab[i]);
			// ======
			calendarPanelExpenditureListTab[i].setLayout(new MigLayout("fill",
					"[center]", "[]"));
			calendarPanelExpenditureListTab[i]
					.add(expenditureTableScrollPaneTab[i]);

			// =====
			calendarPanelExpenditureSum[i].setLayout(new MigLayout("fill,wrap",
					"[center , 30%][center, 70%]", "[]"));
			calendarPanelExpenditureSum[i].add(expenditureSumLabelTab[i]);
			calendarPanelExpenditureSum[i].add(expenditureSumTextFieldTab[i]);
			// ===============================================

		}

		// =====================================
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}

	public static void main(String[] args) {

		Main okno = new Main();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();

		// =======Odśwież
		if (source == bRefresh) {
			for (int i = 0; i < sizeOfTabOfPanelsOfDaysInMonth; i++) {

				double sum = 0;

				// sumowanie wartosci wydatkow w kazdym modelu - zoptymalizowac
				// - zeby odswiezalo tylko w modelu w ktorym byla zmiana ->
				// ActionListener dla JTable - do ogarniecia
				for (int j = 0; j < expenditureTableModelTab[i].expendituresValues
						.size(); j++) {
					sum = sum
							+ expenditureTableModelTab[i].expendituresValues
									.get(j);
				}

				expenditureSumTextFieldTab[i].setText(Double.toString(sum));

			}
		}
		// =========
// miAdd
		if (source == miAdd){
			
			// tymczasowa wersja z ToolTipText  - popracuj nad getLocation albo cos takiego
			int index = Integer.parseInt(sourceTable.getToolTipText());
					
			
			expenditureTableModelTab[index].expendituresNames.add("");
			expenditureTableModelTab[index].expendituresValues.add(0.0);
			expenditureTableModelTab[index].fireTableDataChanged();
			repaint();
					
		}
//=====miDelete		
		
		if(source==miDelete){
			
			int index = Integer.parseInt(sourceTable.getToolTipText());
			int rowId = sourceTable.getSelectedRow();
			
			expenditureTableModelTab[index].expendituresNames.remove(rowId);
			expenditureTableModelTab[index].expendituresValues.remove(rowId);
			expenditureTableModelTab[index].fireTableDataChanged();
			repaint();
		}
		
		
		
	}

}
