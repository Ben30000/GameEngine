
public class MoveTo {

	
	private String type;  // Types: "Slide", "Black", and "Fast"
	private String futureSequence;
	private Menu targetMenu; // Used for sliding menu system, to specifically identify the upcoming menu and obtain its' position
	private MoveTo moveTo;
	

	public MoveTo(String type, String futureSequence, MoveTo moveTo) {
		
		this.type = type;
		this.futureSequence = futureSequence;
		this.moveTo = moveTo;
	}
	
	
	

	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getFutureSequence() {
		return futureSequence;
	}


	public void setFutureSequence(String futureSequence) {
		this.futureSequence = futureSequence;
	}


	public MoveTo getMoveTo() {
		return moveTo;
	}


	public void setMoveTo(MoveTo moveTo) {
		this.moveTo = moveTo;
	}





	public Menu getTargetMenu() {
		return targetMenu;
	}





	public void setTargetMenu(Menu targetMenu) {
		this.targetMenu = targetMenu;
	}

}
