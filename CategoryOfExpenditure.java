package organizerWdatkow;

import java.awt.Color;

public class CategoryOfExpenditure {

	String name;
	Color colorOfCategory;
	
	//=========konstruktory rekursyjne	
	
	public CategoryOfExpenditure(String name, Color color){
		this.name = name;
		this.colorOfCategory=color;
	}
	
	public CategoryOfExpenditure(String name){
		this(name,Color.LIGHT_GRAY);
	}
	public CategoryOfExpenditure(){
		this("Brak nazwy");
	}
	
//=============================================	
	
}
