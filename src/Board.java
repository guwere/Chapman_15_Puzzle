import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 * @author Antonio Nikolov 10378197 nikolova@tcd.ie
 * If playing on board large than 20 please change the font size to a smaller one.
 * A class which creates a GUI combining the GemPuzzle logic with with a UI.
 */
public class Board extends JFrame implements MouseListener {
	public static final int TILE_DIMENSION=100;
	private static JButton BLANK;
	public static Font NUMBERS_FONT;
	private JPanel num_panel;
	private JTextArea txt_area;
	private JMenuBar bar;
	private GemPuzzle puzzle;
	private int length;
	private JTextField entry=new JTextField(8);
    private JLabel jLabel1=new JLabel();
    
	public Board(){
		startPane();
	}
	private void startPane(){
		 JPanel start_panel= new JPanel();
	     JButton start_button=new JButton("Go");
	     start_button.addMouseListener(this);
	     start_button.setSize(30,30);
	     getContentPane().add(start_panel);
	     jLabel1.setText("Enter dimension as integer >1");
	     start_panel.add(jLabel1);
	     start_panel.add(entry);
	     start_panel.add(start_button);
	     start_panel.setBackground(Color.lightGray);
	     setSize(250,250);
	     NUMBERS_FONT=new Font("LucidaTypewriterBold.ttf",Font.BOLD,23);
	     setLocationRelativeTo(null);
	     setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	     setVisible(true);
	}
	private void newBoard(){
		setSize(length*TILE_DIMENSION,length*TILE_DIMENSION);
        createPanels();
        fillGrid();
        createMenu();
        setResizable(false);
        setTitle("Sliding Puzzle");
        if(length>10){
        	setExtendedState(MAXIMIZED_BOTH);
        }
        setVisible(true);
	}
	public void createMenu(){
		bar=new JMenuBar();
		setJMenuBar(bar);
		BoardMenuItem ctrl_item=new BoardMenuItem("new solvable");
		ctrl_item.setFunctionality(BoardMenuItem.NEW_GAME);
		ctrl_item.setBackground(Color.yellow);
		ctrl_item.addMouseListener(new MenuListener(this,puzzle));
		bar.add(ctrl_item);
		
		ctrl_item=new BoardMenuItem("new unsolvable");
		ctrl_item.setFunctionality(BoardMenuItem.NEW_UNSOLV);
		ctrl_item.setBackground(Color.green);
		ctrl_item.addMouseListener(new MenuListener(this,puzzle));
		bar.add(ctrl_item);
	}
	public void createPanels(){	
		num_panel=new JPanel();	
		num_panel.setLayout(new GridLayout(length, length, 0, 0));
        num_panel.setMaximumSize(new Dimension(length*TILE_DIMENSION,length*TILE_DIMENSION));
        txt_area=new JTextArea(3,3);
        txt_area.setEditable(false);
        txt_area.setText("Initial inversions: " + puzzle.calculateInversions());
        add(num_panel, BorderLayout.CENTER);
        add(txt_area,BorderLayout.SOUTH);
 
    }
	public void fillGrid(){
		JButton button;
		Integer current;
		TileListener tile_listener =new TileListener(this,puzzle);
		for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
            	button= new JButton();
            	button.addMouseListener(tile_listener);
            	current=puzzle.getTile(i,j);
            	if(current==puzzle.BLANK){
            		BLANK=button;
            		button.setBackground(Color.black);
            	}else{
            		button.setText(current.toString());
                	button.setFont(NUMBERS_FONT);
            	}
            	num_panel.add(button);
            }
        }

	}
	public JButton getBlank(){
		return BLANK;
	}
	public JTextArea getTextArea(){
		return txt_area;
	}
	public JPanel getNumPanel(){
		return num_panel;
	}
	
	public void mouseClicked(MouseEvent me) {
		if(isNumber(entry.getText())){
			int num=Integer.parseInt(entry.getText());
			if(num>1){
				getContentPane().removeAll();
				length= num;
				puzzle =new GemPuzzle(num);
				puzzle.getSolvablePuzzle();
				System.out.println(puzzle.isSolvable());
				newBoard();
			}
		}
	}
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent arg0) {
	}
	public void mouseReleased(MouseEvent arg0) {
	}
	
	public static boolean isNumber(String s){
		try{
			int n=Integer.parseInt(s);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
		
	}
	public static void main(String [] args){
		Board b= new Board();
	}

}








