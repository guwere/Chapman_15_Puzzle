import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class TileListener extends MouseAdapter {
    private Board b;
    private GemPuzzle puzzle;
    public TileListener(Board b,GemPuzzle p){
        this.b=b;
        puzzle=p;
    }
    public void mouseClicked(MouseEvent me){
        //divide the positions in the jpanel by the tile width to get positions
        JButton current= (JButton)me.getSource();
        int row_curr=current.getY()/current.getHeight();
        int column_curr=current.getX()/current.getWidth();
        int row_blank=b.getBlank().getY()/b.getBlank().getHeight();
        int column_blank=b.getBlank().getX()/b.getBlank().getWidth();

        System.out.printf("current index: %s , %s \n", row_curr,column_curr);
        System.out.printf("x: %s , y: %s \n", current.getY(),current.getX());

        if(puzzle.isValidMove(row_curr, column_curr, row_blank, column_blank)){

            b.getTextArea().setText("Changing blank with "+ puzzle.getTile(row_curr, column_curr));
            puzzle.swapTiles(row_curr, column_curr, row_blank, column_blank);
            b.getNumPanel().add(b.getBlank(),puzzle.getIndexInSingle(row_curr,column_curr));
            b.getNumPanel().add(current,puzzle.getIndexInSingle(row_blank,column_blank));

            if(puzzle.isSolved()){
                b.getTextArea().setText("Puzzle Solved");
                JOptionPane.showMessageDialog(null, "Puzzle Solved!", "CONGRATULATIONS!!!", JOptionPane.INFORMATION_MESSAGE);
            }
            b.getTextArea().append("\n Inversions: " + puzzle.calculateInversions());
            b.getNumPanel().validate();
        }
    }
}