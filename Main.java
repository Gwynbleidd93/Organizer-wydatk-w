package organizerWdatkow;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

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
	
	
	 public final String[] namesOfMonths = { "Styczeń", "Luty", "Marzec", "Kwiecień",
			"Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik",
			"Listopad", "Grudzień" };
	 public final Integer[] yearsOfCalendar = new Integer[15];

	ArrayList <LocalDate> dateArrayList = new ArrayList<>();  // lista przechowujaca daty od 2015-01-01 do 2029-12-31
	ArrayList <ExpenditureTableModel> tableModelArrayList = new ArrayList<>(); // lista modeli tabeli dla każdej daty
	ArrayList <Integer> dayOfWeekOfDataArrayList = new ArrayList<Integer>(); // przechowuje indeksy dnia tygodnia np, czwartek - 4 poniedziałek  - 1 dla każdej daty 
	HashMap <LocalDate, ExpenditureTableModel> date_ModelMap = new HashMap<LocalDate,ExpenditureTableModel>();
	HashMap <LocalDate,Integer> date_DayOfWeek_Map = new HashMap <LocalDate,Integer> ();
	
	
	// panele grupujace obszar kalendarza
	JPanel calendarPanelMain, calendarPanelHeader,
			calendarPanelDaysOfWeekHeader, calendarPanelDays;

	// panele poza kalendarzemw głównym oknie
	JPanel buttonsPanel;

	// buttony
	JButton bAdd, bAddCategory, bRefresh,bTmp,bTmp2;

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
		// wypełnienie ArrayListy datami z przedziału 2015-01-01 do 2029-12-31
		
		for (int i=0;i<15;i++){			
			for (int j=1;j<13;j++){
				LocalDate tmpDate = LocalDate.of(2015+i,j,1);
				for (int k=1;k<tmpDate.lengthOfMonth()+1;k++){
					LocalDate myDate = LocalDate.of(2015+i, j, k);
					dateArrayList.add(myDate);
				}					
			}
		}
		
		// tworzenie modeli tablic wydatków dla każdej daty
		
		for (int i=0;i<dateArrayList.size();i++){
			tableModelArrayList.add(new ExpenditureTableModel());
			dayOfWeekOfDataArrayList.add(dateArrayList.get(i).getDayOfWeek().getValue());
		}
		// tworzenie mapy haszującej Key:Data - Value: model tablicy wydatków
		
		for (int i=0;i<dateArrayList.size();i++){			
			date_ModelMap.put(dateArrayList.get(i), tableModelArrayList.get(i));	
			date_DayOfWeek_Map.put(dateArrayList.get(i),dayOfWeekOfDataArrayList.get(i));
		}
		

		// ====================== okno główne

		bAdd = new JButton("Dodaj");
		bAddCategory = new JButton("Dodaj kategorię");
		bRefresh = new JButton("Odśwież");
		bRefresh.addActionListener(this);

		bTmp = new JButton("Tmp");
		bTmp.addActionListener(this);
		
		bTmp2 = new JButton("Tmp2");
		bTmp2.addActionListener(this);
		
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
		buttonsPanel.add(bTmp);
		buttonsPanel.add(bTmp2);
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
		monthsCombo.setSelectedIndex(11); // ustawiam Grudzień

		monthsCombo.addActionListener(this);
		yearsCombo.addActionListener(this);
		
	
		
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
			expenditureTableTab[i] = new JTable();
		//	expenditureTableTab[i] = new JTable(expenditureTableModelTab[i]);
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
		fillCalendar(12, 2017); // aby po uruchomieniu pobralo modele poczatkowe
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

		// lepszy switch case wydajnosciowo
		
		// =======Odśwież
		if (source == bRefresh) {
			//!!!!!!!!!!!!!!
		
			int month = monthsCombo.getSelectedIndex()+1;   //numer miesiaca
			int year = Integer.parseInt(yearsCombo.getSelectedItem().toString());//numer roku
			int startDayOfMonthIndex = date_DayOfWeek_Map.get(LocalDate.of(year, month, 1));	
			//dlatego gdyż miedzy indeksami ToolTipIndex a wartoscia indeksu dnia poczatkowego jest zależność (TTindex+2) - startDayIndex = szukany numer dnia miesiaca
			
			for (int i=startDayOfMonthIndex-1;i<LocalDate.of(year, month, 1).lengthOfMonth()+startDayOfMonthIndex;i++){
				
				double sum=0;
				
				for (int j=0; j<date_ModelMap.get(LocalDate.of(year, month,j+1)).expendituresValues.size();j++){
					
					sum = sum + date_ModelMap.get(LocalDate.of(year, month, j+1)).expendituresValues.get(j);
					
				}
				
				expenditureSumTextFieldTab[i].setText(Double.toString(sum));
				
			}
			
			
		/*	
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
}  */
			
			
		}
		// =========
// miAdd
		if (source == miAdd){
			
			// tymczasowa wersja z ToolTipText  - popracuj nad getLocation albo cos takiego
			int index = Integer.parseInt(sourceTable.getToolTipText());
			int month = monthsCombo.getSelectedIndex()+1;   //numer miesiaca
			int year = Integer.parseInt(yearsCombo.getSelectedItem().toString());//numer roku
			int startDayOfMonthIndex = date_DayOfWeek_Map.get(LocalDate.of(year, month, 1));
			
			//dlatego gdyż miedzy indeksami ToolTipIndex a wartoscia indeksu dnia poczatkowego jest zależność (TTindex+2) - startDayIndex = szukany numer dnia miesiaca
			
			date_ModelMap.get(LocalDate.of(year, month, (index+2)-startDayOfMonthIndex)).expendituresNames.add("");
			date_ModelMap.get(LocalDate.of(year, month, (index+2)-startDayOfMonthIndex)).expendituresValues.add(0.0);
			date_ModelMap.get(LocalDate.of(year, month, (index+2)-startDayOfMonthIndex)).fireTableDataChanged();
			
			repaint();
					
		}
//=====miDelete		
		
		if(source==miDelete){
						
			int index = Integer.parseInt(sourceTable.getToolTipText());
			int rowId = sourceTable.getSelectedRow();
			int month = monthsCombo.getSelectedIndex()+1;   //numer miesiaca
			int year = Integer.parseInt(yearsCombo.getSelectedItem().toString());//numer roku
			int startDayOfMonthIndex = date_DayOfWeek_Map.get(LocalDate.of(year, month, 1));
			//=============
			//dlatego gdyż miedzy indeksami ToolTipIndex a wartoscia indeksu dnia poczatkowego jest zależność (TTindex+2) - startDayIndex = szukany numer dnia miesiaca
			date_ModelMap.get(LocalDate.of(year, month, (index+2)-startDayOfMonthIndex)).expendituresNames.remove(rowId);
			date_ModelMap.get(LocalDate.of(year, month, (index+2)-startDayOfMonthIndex)).expendituresValues.remove(rowId);
			date_ModelMap.get(LocalDate.of(year, month, (index+2)-startDayOfMonthIndex)).fireTableDataChanged();
			
			repaint();
		}
		
		if (source==bTmp){
					
		}

		
		if (source==bTmp2){
			
		
		}
		
		//============================comboBoxy months i years
		String month = monthsCombo.getSelectedItem().toString();
		String year = yearsCombo.getSelectedItem().toString();
			
		switch (month){
		//============
		case "Styczeń":{
			
			switch (year){
			case "2015":{				
				fillCalendar(1, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(1, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(1, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(1, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(1, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(1, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(1, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(1, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(1, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(1, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(1, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(1, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(1, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(1, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(1, 2029);
			break;	
			}
			//================
					
			}//end switch(year) styczen
				
			
		break;	
		}
		//===================
		case "Luty":{
			
			
			switch (year){
			case "2015":{				
				fillCalendar(2, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(2, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(2, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(2, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(2, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(2, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(2, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(2, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(2, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(2, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(2, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(2, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(2, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(2, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(2, 2029);
			break;	
			}
			//================
			
			}//end switch(year) luty
						
		break;	
		}
		//====================
		case "Marzec":{
			
			switch (year){
			case "2015":{				
				fillCalendar(3, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(3, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(3, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(3, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(3, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(3, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(3, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(3, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(3, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(3, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(3, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(3, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(3, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(3, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(3, 2029);
			break;	
			}
			//================
			}//end switch(year) marzec
						
		break;	
		}
		//======================
		case "Kwiecień":{
			
			switch (year){
			case "2015":{				
				fillCalendar(4, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(4, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(4, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(4, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(4, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(4, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(4, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(4, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(4, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(4, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(4, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(4, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(4, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(4, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(4, 2029);
			break;	
			}
			//================
			}//end switch(year) kwiecien	
			
		break;	
		}
		//======================
		case "Maj":{
			
			switch (year){
			case "2015":{				
				fillCalendar(5, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(5, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(5, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(5, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(5, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(5, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(5, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(5, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(5, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(5, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(5, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(5, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(5, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(5, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(5, 2029);
			break;	
			}
			//================		
			}//end switch(year) maj
			
			
		break;	
		}
		//======================
		case "Czerwiec":{
			
			switch (year){
			case "2015":{				
				fillCalendar(6, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(6, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(6, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(6, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(6, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(6, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(6, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(6, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(6, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(6, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(6, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(6, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(6, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(6, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(6, 2029);
			break;	
			}
			//================			
			}//end switch(year) czerwiec
	
		break;	
		}
		//======================
		case "Lipiec":{
			
			switch (year){
			case "2015":{				
				fillCalendar(7, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(7, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(7, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(7, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(7, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(7, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(7, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(7, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(7, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(7, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(7, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(7, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(7, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(7, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(7, 2029);
			break;	
			}
			//================
			}//end switch(year) lipiec
		
		break;	
		}
		//======================
		case "Sierpień":{
			
			switch (year){
			case "2015":{				
				fillCalendar(8, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(8, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(8, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(8, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(8, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(8, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(8, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(8, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(8, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(8, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(8, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(8, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(8, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(8, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(8, 2029);
			break;	
			}
			//================
			}//end switch(year) sierpień
			
			
		break;	
		}
		//======================
		case "Wrzesień":{
			
			switch (year){
			case "2015":{				
				fillCalendar(9, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(9, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(9, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(9, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(9, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(9, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(9, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(9, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(9, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(9, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(9, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(9, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(9, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(9, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(9, 2029);
			break;	
			}
			//================
			}//end switch(year) wrzesień
			
			
		break;	
		}
		//======================
		case "Październik":{
			
			switch (year){
			case "2015":{				
				fillCalendar(10, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(10, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(10, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(10, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(10, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(10, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(10, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(10, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(10, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(10, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(10, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(10, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(10, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(10, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(10, 2029);
			break;	
			}
			//================
			}//end switch(year) październik
			
			
		break;	
		}
		//======================
		case "Listopad":{
			
			switch (year){
			case "2015":{				
				fillCalendar(11, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(11, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(11, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(11, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(11, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(11, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(11, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(11, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(11, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(11, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(11, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(11, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(11, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(11, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(11, 2029);
			break;	
			}
			//================
			}//end switch(year) listopad	
		break;	
		}
		//======================
		case "Grudzień":{
			
			switch (year){
			case "2015":{				
				fillCalendar(12, 2015);			
			break;	
			}
			//================
			case "2016":{
				fillCalendar(12, 2016);
			break;	
			}
			//================
			case "2017":{
				fillCalendar(12, 2017);
			break;	
			}
			case "2018":{
				fillCalendar(12, 2018);
			break;	
			}
			case "2019":{
				fillCalendar(12, 2019);
			break;	
			}
			case "2020":{
				fillCalendar(12, 2020);
			break;	
			}
			case "2021":{
				fillCalendar(12, 2021);
			break;	
			}
			case "2022":{
				fillCalendar(12, 2022);
			break;	
			}
			case "2023":{
				fillCalendar(12, 2023);
			break;	
			}
			case "2024":{
				fillCalendar(12, 2024);
			break;	
			}
			case "2025":{
				fillCalendar(12, 2025);
			break;	
			}
			case "2026":{
				fillCalendar(12, 2026);
			break;	
			}
			case "2027":{
				fillCalendar(12, 2027);
			break;	
			}
			case "2028":{
				fillCalendar(12, 2028);
			break;	
			}
			case "2029":{
				fillCalendar(12, 2029);
			break;	
			}
			//================
			}//end switch(year) grudzien
			
		break;	
		}
		//======================
		}//end switch(month)
		
		
		//===================================
		
	}

//metoda fillCalendar - ustawienie widoku i odpowiedniego zbioru modeli dla wybranego miesiąca i roku	
	
	private void fillCalendar(int month,int year){
		
		for (int i=0;i<sizeOfTabOfPanelsOfDaysInMonth;i++){
			calendarPanelSingleDayTab[i].setVisible(true);
		}
		
		int startDay = date_DayOfWeek_Map.get(LocalDate.of(year, month, 1))-1; //indeks tabeli w gui od ktorego zacząć dodawać modele
		int monthLength = LocalDate.of(year, month, 1).lengthOfMonth();
		int j=1;
		
		for (int i=0;i<sizeOfTabOfPanelsOfDaysInMonth;i++){
			//wypełnienie kalendarza odpowiednimi modelami
			if ((i>=startDay)&&(i<startDay+monthLength)){
				expenditureTableTab[i].setModel(date_ModelMap.get(LocalDate.of(year, month, j)));
				dayHeaderLabelTab[i].setText(dayHeaderStringTab[j-1]);
				j++;		
			}
			//ukrycie pozostałych dni
			else	if ((i<startDay)||(i>=startDay+monthLength)){
				calendarPanelSingleDayTab[i].setVisible(false);
			}					
			
		}
	}
	//===end metoda fillCalendar
	
	
	
}
