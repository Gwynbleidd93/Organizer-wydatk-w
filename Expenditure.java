package organizerWdatkow;

public class Expenditure {

	CategoryOfExpenditure category;			// kategoria np jedzienie, paliwo
	double valueOfExpenditure;				// wartosc wydatku w z³ 
	String comments;						// uwagi do wydatku
	
	
	//========konstruktory
	
	public Expenditure( CategoryOfExpenditure category, double value, String comments){
		this.category=category;
		this.valueOfExpenditure=value;
		this.comments=comments;
	}
	
	public Expenditure(CategoryOfExpenditure category, double value){
		this(category,value," ");		
	}
	
	public Expenditure(double value){
		
		this (new CategoryOfExpenditure(),value, " ");
		
	}
	
	public Expenditure(){
		this(new CategoryOfExpenditure(),0," ");
	}
	
//=======================================================	
	
	
	
	
}
