import java.util.Random;
/**
 * @author Antonio Nikolov 10378197 nikolova@tcd.ie
 * 	A class which represents the logic needed to construct and modify a board used to show
 *	the classic 15 Chapman Puzzle.
 */
public class GemPuzzle {

	/**
	 * number of elements in a row/column
	 */
	private int width;  
	/**
	 * simplifies getInversions() and populateRandomly();
	 */
	private int [] single;
	/**
	 * stores the int values of the tiles
	 */
	private int [][] board;
	/**
	 * the tiles that "moves"
	 */
	public final int BLANK;
	/**
	 * the row of the blank in the initial configuration
	 */
	public int row_blank; 
	/**
	 *the column of the blank in the initial configuration 
	 */
	public int col_blank; 
	/**
	 * index of the blank in the single array
	 */
	private int index_blank;
	/**
	 * used for easy index conversion from double to single array index 
	 */
	public int [][] reference_array;
									
	/**
	 * initialises double array of size d x d 
	 * @param d - the length of the side
	 */
	public GemPuzzle(int d){
		width=d;
		single= new int[width*width];
		board=new int[width][width];
		BLANK=single.length-1;
		reference_array=new int[d][d];
	}

	/**
	 * Fills the board with a random sequence length^2 long.
	 * Initialises a temporary array with every integer from 0 to length^2-1
	 * For every elements in the single array: a random number is generated from 
	 * the remaining length of the temporary array;the element from that index is 
	 * put in the single array; the element in the last index from the temporary array is 
	 * copied into the random index; the number of the remaining available indices is decremented
	 * and on every iteration the upper bound of the possible random number is 1 less than on the 
	 * previous iteration.
	 */
	public void populateRandomly(){
		Random gen= new Random();
		int [] temp=new int[width*width];
		int random_index;
		int remaining=temp.length;
		for(int i=0;i<remaining;i++){
			temp[i]=i;
		}
		int length=single.length;
		for(int i=0;i<length;i++){
			random_index=gen.nextInt(remaining);
			single[i]=temp[random_index];
			if(single[i]==BLANK){
				index_blank=i;//save the index of the blank for later reference
			}
			remaining--;
			temp[random_index]=temp[remaining];
		}
		//fill the board 
		singleToDouble(single);
	}
	
	/**
	 * convert the single array to the board(double)
	 * also fills a double reference array with 0 to length^2-1
	 * containing the indices of a single array with is used 
	 * when swapping tiles to get the corresponding
	 *  index from a double array to the one in a single array
	 */
	private void singleToDouble(int [] single){
		int index=0;
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.width;j++){
				board[i][j]=single[index];
				if(board[i][j]==BLANK){
					row_blank=i+1;
					col_blank=j;
				}
				reference_array[i][j]=index;
				index++;

			}
		}
	}
	/**
	 * @param i, j position of element in the double to which you want the  index in the single array 
	 */
	public int getIndexInSingle(int i, int j){
		return reference_array[i][j];
	}
	
	/**Discounts  when i is on BLANK index. as BLANK is represented by the highest int number on
	 * the board there is not need to check if current index is bigger than the blank as
	 * it always is going to return false.
	 * @return the number of inversions in the double array. 
	 * 
	 */
	public int calculateInversions(){
		int inversions=0;
		int len= single.length;
		for(int i=0;i<len-1;i++){
			for(int j=i+1;j<len&&single[i]!=BLANK;j++){
				if(single[i]>single[j]){
					inversions++;
				}
			}
		}return inversions;
		
	}
	/** Uses the formula from http://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html 
	 * The validity of this algorithm is further explained in the link.
	 * 
	 * ( (grid width odd) && (#inversions even) )  ||  ( (grid width even) && ((blank on odd row from bottom) == (#inversions even)))
	 * 1. For a grid of odd width the polarity of the inversions does not change when a tile is moved.
	 * 2. For a grid of even width the polarity of inversions must equal the polarity of the row the blank is on from the bottom. 
	 * @return true if solvable; false if not solvable.
	 * 
	 */
	public boolean isSolvable(){
		int inv=calculateInversions();
		if(!isEven(width) && isEven(inv))return true;
		if(isEven(width) &&((blankOnOddRowFromBottom())==(isEven(inv))))return true;
		return false;
	}
	/**
	 * DOES NOT WORK. NOT USED. Only works if the blank is on odd row from bottom
	 */
	public boolean isSolvable2(){
		int inv=calculateInversions();
		int man= getManhattanDistance(row_blank,col_blank);
		if(isEven(inv +man))return true;
		return false;
	}

	/**
	 * @return true if 1.the number of rows is even and the row of the blank tile is odd
	 * 				   2.the number of rows is odd and row of the blank is even.
	 * 			otherwise return false.
	 */
	private boolean blankOnOddRowFromBottom(){
		if(isEven(row_blank)^isEven(width))return false;
		return true;
	}
	/**
	 * @param n - signed integer
	 * @return true if even
	 * 			false if odd
	 */
	private boolean isEven(int n){
		if(Math.abs(n)==1)return false;
		if(Math.abs(n)%2==0)return true;
		return false;
	}
	/**
	 * @param i j current position 
	 * @param i2 j2 new position
	 * @return true iff only i or only j move position and it must be by one.
	 */
	public boolean isValidMove(int i, int j, int i2, int j2){
		if(Math.abs(i-i2)==1 && j==j2)return true;
		else if(Math.abs(j-j2)==1 && i==i2)return true;
		return false;
	}
	/**
	 * @return true if the number of inversions is 0 and the blank is on the bottom right corner
	 */
	public boolean isSolved(){
		if(calculateInversions()==0 && single[BLANK]==BLANK)return true;
		return false;
	}
	/**
	 * @param i - row of blank
	 * @param j - column of blank
	 * @return the number of times the blank tile has to move
	 * to get to the bottom right corner
	 */
	public int getManhattanDistance(int i,int j){
		int vertical=board.length - 1 - i;
		int horizontal=board.length - 1 - j;
		return vertical+horizontal;
		
	}

	/**swaps tiles [i,j]<->[i2,j2] in the double aray
	 * and updates the single array
	 * @param i - row of first
	 * @param j - column of first
	 * @param i2 - row of second
	 * @param j2 - column of second
	 */
	public void swapTiles(int i,int j,int i2, int j2){
		int temp=board[i][j];
		board[i][j]=board[i2][j2];
		board[i2][j2]=temp;
		temp=single[getIndexInSingle(i,j)];
		System.out.println("Swapping; " + temp);
		System.out.println("with: "+ single[getIndexInSingle(i2,j2)]);
		single[getIndexInSingle(i,j)]=single[getIndexInSingle(i2,j2)];
		single[getIndexInSingle(i2,j2)]=temp;
	}
	
	/**
	 * Keep populating the object this method is invoked on until a solvable solution is generated.
	 */
	public void getSolvablePuzzle(){
		do{
			populateRandomly();
			System.out.println(printBoard());
			System.out.println("Inversions: " + calculateInversions());
		}while(!isSolvable2());
	}
	
	/**
	 * Keep populating the object this method is invoked on until an unsolvable solution is generated.
	 */
	public void getUnsolvablePuzzle(){
		do{
			populateRandomly();
			System.out.println(printBoard());
			System.out.println("Inversions: " + calculateInversions());
		}while(isSolvable());
	}
	//getter methods
	public int getDimensions(){
		return width;
	}
	/**
	 * @return the number on this tile.
	 */
	public int getTile(int i, int j){
		return board[i][j];
	}
	public int getLength(){
		return single.length; 
	}
	public int getBlankIndex(){
		return index_blank;
	}
	/**
	 * @return string representation of the double array
	 */
	public String printBoard(){
		String s="";
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.width;j++){
				if(board[i][j]==BLANK)s+=" -";
				else s+=" " + board[i][j];
			}
			s+="\n";
		}return s;
	}
	//for debugging. To run application use Board class
	public static void main(String [] args){
		GemPuzzle p= new GemPuzzle(4);
		p.getSolvablePuzzle();
		System.out.println(p.printBoard());
		System.out.println("Inversions: \n" + p.calculateInversions());
		System.out.println("Solvability: " + p.isSolvable());
	}

}