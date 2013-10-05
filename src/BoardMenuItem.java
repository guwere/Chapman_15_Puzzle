import javax.swing.*;
public class BoardMenuItem extends JMenuItem {
	public static final int NEW_GAME=1;
	public static final int NEW_UNSOLV=2;
	int type;
	public BoardMenuItem(String name){
		super(name);
	}
	public void setFunctionality(int n){
		type=n;
	}
	public int getType(){
		return type;
	}
}