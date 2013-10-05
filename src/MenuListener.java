import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class MenuListener extends MouseAdapter {
	private Board b;
	private GemPuzzle puzzle;
	public MenuListener(Board b, GemPuzzle p){
		this.b=b;
		puzzle=p;
	}
	
	public void mouseClicked(MouseEvent me){
		BoardMenuItem item=(BoardMenuItem) me.getSource();
		if(item.getType()==BoardMenuItem.NEW_GAME){
			puzzle.getSolvablePuzzle();
		}else if(item.getType()==BoardMenuItem.NEW_UNSOLV){
			puzzle.getUnsolvablePuzzle();
		}
		b.getNumPanel().removeAll();
		b.fillGrid();
		System.out.println(puzzle.isSolvable());
		b.getTextArea().setText("Initial inversions: " + puzzle.calculateInversions());
		b.validate();
	}
}