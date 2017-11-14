package organizerWdatkow;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class Main extends JFrame {

	
	public String [] namesOfMonths = {"Styczeń","Luty","Marzec","Kwiecień","Maj","Czerwiec","Lipiec","Sierpień","Wrzesień","Październik","Listopad","Grudzień"};
	public Integer [] yearsOfCalendar = new Integer [16]; 
	
	//================================konstruktor - build GUI
	public Main(){        
		
		setPreferredSize(new Dimension(1200,600));
		setTitle("Organizer wydatków");
		
		Container cp = getContentPane();     // cp - ContentPane
		
		// wypełnienie tablicy lat objętych opracowaniem - moedlowo lata 2015 - 2030
			for (int i=0;i<yearsOfCalendar.length;i++){
				int yearStart = 2015;
				yearsOfCalendar[i]=yearStart+i;
			}
		
		
		//panele grupujace obszar kalendarza
		JPanel calendarPanelMain,calendarPanelHeader,calendarPanelDaysOfWeekHeader,calendarPanelDays;
		
		//panele poza kalendarzemw głównym oknie
		JPanel buttonsPanel;
		
//====================== okno główne		
		
		JButton bAdd;
		
		bAdd = new JButton("Dodaj");
		
		
		calendarPanelMain = new JPanel();
		calendarPanelMain.setBorder(BorderFactory.createEtchedBorder());
		buttonsPanel = new JPanel();
		buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
		
		
		
		//1.
		cp.setLayout(new MigLayout("","[grow]","[][]50[]")); 
		//1.1
		cp.add(calendarPanelMain,"wrap,growx");
		
		JScrollPane scrollPane = new JScrollPane(calendarPanelMain);
		scrollPane.setPreferredSize(new Dimension(400,400));
		cp.add(scrollPane,"wrap, growx");
		//1.2
		cp.add(buttonsPanel,"growx");
		
		buttonsPanel.add(bAdd);
//======================================================		
		//obszar kalendarza
	// 1.1
		calendarPanelMain.setLayout(new MigLayout("flowx","[1100px]","[min][min][pref]"));
		
		//1.1.1
		calendarPanelHeader = new JPanel();		
		calendarPanelHeader.setBorder(BorderFactory.createEtchedBorder());
		
		
		//1.1.2
		calendarPanelDaysOfWeekHeader= new JPanel();
		calendarPanelDaysOfWeekHeader.setBorder(BorderFactory.createEtchedBorder());
		
		//1.1.3
		calendarPanelDays=new JPanel();
		calendarPanelDays.setBorder(BorderFactory.createEtchedBorder());
		
		
		
		
		calendarPanelMain.add(calendarPanelHeader,"grow, wrap ");
		calendarPanelMain.add(calendarPanelDaysOfWeekHeader,"grow, wrap");
		calendarPanelMain.add(calendarPanelDays,"grow");
		
		
	//===============================================
		//obszar calendarPanelHeader  1.1.1
		
		calendarPanelHeader.setLayout(new MigLayout("center","[center][center]","[]"));
		
		
		JComboBox monthsCombo = new JComboBox(namesOfMonths);
		JComboBox yearsCombo = new JComboBox(yearsOfCalendar);
		monthsCombo.setPreferredSize(new Dimension(100, 30));
		yearsCombo.setPreferredSize(new Dimension(100,20));
		
		yearsCombo.setSelectedIndex(2);   //ustawiam 2017 rok
		monthsCombo.setSelectedIndex(10); // ustawiam Listopad
		
		
	//	JLabel lHeader = new JLabel("Wrzesien 2017");
	//	calendarPanelHeader.add(lHeader);
		calendarPanelHeader.add(monthsCombo);
		calendarPanelHeader.add(yearsCombo);
		
		
	
		
		
	//=================================================
		//obszar calendarDaysOfWeekHeader 1.1.2
		
		calendarPanelDaysOfWeekHeader.setLayout(new MigLayout("fill","[center][center][center][center][center][center][center]","[]"));
		
		final String [] daysOfWeekHeaders = {"Poniedziałek","Wtorek","Środa","Czwartek","Piątek","Sobota","Niedziela"};
		JLabel [] lDaysOfWeekHeaders = new JLabel[daysOfWeekHeaders.length];
		
		for (int i=0;i<lDaysOfWeekHeaders.length;i++){
			lDaysOfWeekHeaders[i]= new JLabel(daysOfWeekHeaders[i]);	
		
		}
	
	
		for (int i=0;i<lDaysOfWeekHeaders.length;i++){
			calendarPanelDaysOfWeekHeader.add(lDaysOfWeekHeaders[i]);
		}
	
	//====================================
		//obszar calendarPanelDays 1.1.3
		
		calendarPanelDays.setLayout(new MigLayout("wrap","[center, sg][center,sg][center,sg][center,sg][center,sg][center,sg][center,sg]","[sg][sg][sg][sg][sg][sg]"));

		
		//			1.1.3 calendarPanelDays			
		//		1.1.3.1 calendarPanelSingleDay
		//	1.1.3.1.1 calendarPanelDayHeader
		//	1.1.3.1.2 calendarPanelExpenditureList
		//	1.1.3.1.3 calendarPanelExpenditureSum
		
		final int sizeOfTabOfPanelsOfDaysInMonth = 42;  //size 7*6 [7 columns * 6 rows ]
		JPanel [] calendarPanelSingleDayTab = new JPanel [sizeOfTabOfPanelsOfDaysInMonth];  
		JPanel [] calendarPanelDayHeaderTab = new JPanel [sizeOfTabOfPanelsOfDaysInMonth];
		JPanel [] calendarPanelExpenditureListTab = new JPanel [sizeOfTabOfPanelsOfDaysInMonth];
		JPanel [] calendarPanelExpenditureSum = new JPanel [sizeOfTabOfPanelsOfDaysInMonth];
		
//		1.1.3.1.1 calendarPanelDayHeader
		String [] dayHeaderStringTab = new String [sizeOfTabOfPanelsOfDaysInMonth];
		JLabel [] dayHeaderLabelTab = new JLabel [sizeOfTabOfPanelsOfDaysInMonth];
		
//	1.1.3.1.2 calendarPanelExpenditureList
		JTextArea [] expenditureListTab = new JTextArea [sizeOfTabOfPanelsOfDaysInMonth];	
		
//		1.1.3.1.3 calendarPanelExpenditureSum		
		JLabel [] expenditureSumLabelTab = new JLabel[sizeOfTabOfPanelsOfDaysInMonth];
		JTextField [] expenditureSumTextFieldTab = new JTextField[sizeOfTabOfPanelsOfDaysInMonth];
		
		
		
		
		for (int i=0;i<calendarPanelSingleDayTab.length;i++){	// tworzenie paneli w tablicach		
			JPanel singleDayPanel = new JPanel();
			singleDayPanel.setBorder(BorderFactory.createEtchedBorder());
			JPanel dayHeaderPanel = new JPanel();
			dayHeaderPanel.setBorder(BorderFactory.createEtchedBorder());
			JPanel expenditureListPanel = new JPanel();
		    expenditureListPanel.setBorder(BorderFactory.createEtchedBorder());
			JPanel expenditureSumPanel = new JPanel();
			expenditureSumPanel.setBorder(BorderFactory.createEtchedBorder());
			
			calendarPanelSingleDayTab[i]=singleDayPanel;
			calendarPanelDayHeaderTab[i]=dayHeaderPanel;
			calendarPanelExpenditureListTab[i]=expenditureListPanel;
			calendarPanelExpenditureSum[i]=expenditureSumPanel;
			
			//================
			calendarPanelDays.add(calendarPanelSingleDayTab[i]);
			//=================
			
			calendarPanelSingleDayTab[i].setLayout(new MigLayout("wrap, fill","[center,fill]","[c,min][c,100px][c,min]"));
			
			calendarPanelSingleDayTab[i].add(calendarPanelDayHeaderTab[i]);
			calendarPanelSingleDayTab[i].add(calendarPanelExpenditureListTab[i]);
			calendarPanelSingleDayTab[i].add(calendarPanelExpenditureSum[i]);
			
			//========================
			
			dayHeaderStringTab[i] = Integer.toString(i+1);
			dayHeaderLabelTab[i] = new JLabel(dayHeaderStringTab[i]);
			//=======================
			
			expenditureListTab[i] = new JTextArea(20, 20);
			
			//===============================
			
			expenditureSumLabelTab[i]= new JLabel("Suma: ");
			expenditureSumTextFieldTab[i] = new JTextField(20);
			
			//===========================
		//dodawanie komponenetow do paneli
			//=====
			calendarPanelDayHeaderTab[i].setLayout(new MigLayout("fill","[center]","[]"));
			calendarPanelDayHeaderTab[i].add(dayHeaderLabelTab[i]);
			//======
			calendarPanelExpenditureListTab[i].setLayout(new MigLayout("fill","[center]","[]"));
			calendarPanelExpenditureListTab[i].add(expenditureListTab[i]);
			//=====
			calendarPanelExpenditureSum[i].setLayout(new MigLayout("fill,wrap","[center , 30%][center, 70%]","[]"));
			calendarPanelExpenditureSum[i].add(expenditureSumLabelTab[i]);
			calendarPanelExpenditureSum[i].add(expenditureSumTextFieldTab[i]);
		//===============================================

		}
		

		
		
		
		
		
		
	//=====================================
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}
	

	public static void main(String[] args) {

		Main okno =  new Main();
		
	}

}
