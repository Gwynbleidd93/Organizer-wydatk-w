package organizerWdatkow;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

public class ExpenditureTableModel extends AbstractTableModel {

	String [] categoryNames = {" ","Paliwo","Jedzenie","Czynsz","Alkohol","Ubrania","Prezent","Inne"};
	
	String [] columnNames = {"Wydatek","z³"};
	
	ArrayList <String> expendituresNames = new ArrayList<>();
//	ArrayList <JComboBox> expendituresNames = new ArrayList<>();
	ArrayList <Double> expendituresValues = new ArrayList<>();
	
	
	public ExpenditureTableModel(){
		
	for (int i=0;i<getRowCount();i++){
		expendituresNames.add("");
		expendituresValues.add(0.0);
	}
		
		
	}
	
	  public String getColumnName(int col) {
		    return columnNames[col];
		  }
	
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	
	// ilosc wierszy w kazdnym dniu  - jak na razie na sztywno
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public Object getValueAt(int i, int j) {
		Object o = null;
		switch (j){
		case 0: {o=expendituresNames.get(i); break;}
		case 1: {o=expendituresValues.get(i); break;}
		default:{ break; }		
		}		
		return o;
	}
	
	
	public void setValueAt(Object value, int i, int j){
		switch (j){
		case 0: {expendituresNames.set(i, (String)value); break;}
		case 1: {expendituresValues.set(i,(Double)value); break;}
		default:{ break; }		
		}
		fireTableCellUpdated(i, j);
	}
	
	  public Class getColumnClass(int c) {
		    return getValueAt(0, c).getClass();
		  }
	
	
	
	  public boolean isCellEditable(int row, int col) {
		    if (col == 1 || col == 0 ) {  
		      return true;
		    } else {
		        return false;
		    }
		  }
	

	
	
	
	
	
}
