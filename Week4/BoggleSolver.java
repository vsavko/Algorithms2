import edu.princeton.cs.algs4.In;

public class BoggleSolver {
	//private TST<Integer> trie;
	private RwayTST trie;
	
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	int i = 0;
    	//trie = new RwayTST();
    	trie = new RwayTST (100);
    	for(String key: dictionary) {
    		trie.put(key, i++);
    	}
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
    	//Use depth Search First
    	TST<Integer> foundWords = new TST<>();
    	boolean [][] checkedCells;
    	
    	for(int i = 0; i < board.cols(); i++) {
    		for(int j = 0; j < board.rows(); j++) {
    			checkedCells = new boolean[board.rows()][board.cols()];
    			StringBuilder currentWord = new StringBuilder();
    			DFSBoggle(board,foundWords, checkedCells, j,i, currentWord);
    		}
    	}
    	
    	return foundWords.keys();
    }
    
    private void DFSBoggle (BoggleBoard board, TST<Integer> foundWords,
    							boolean [][] checkedCells, int row, int col, StringBuilder currentWord ) {
    	boolean [][] checkedCellsCopy = new boolean[board.rows()][board.cols()];
    	for(int i =0; i < board.rows(); i++) {
    		for( int j = 0; j < board.cols(); j++) {
    			checkedCellsCopy[i][j] = checkedCells[i][j];
    		}
    	}
    	
    	StringBuilder currentWordCopy = new StringBuilder(currentWord);
    	char letter = board.getLetter(row, col);
    	currentWordCopy.append(letter);
    	if (letter == 'Q')
    		currentWordCopy.append('U');
    	
    	if (!trie.hasPrefix(currentWordCopy.toString())) return;
    	
    	checkedCellsCopy[row][col] = true;
    	
    	if (currentWordCopy.length() >=3 && trie.contains(currentWordCopy.toString())) {
    		foundWords.put(currentWordCopy.toString(), 0);
    	}
    	
    	for(int i = -1 ; i < 2; i++) {
    		int checkedCol = col + i;
    		if(checkedCol < 0 || checkedCol >= board.cols() ) continue;
    		for(int j = -1 ; j < 2; j++) {
    			int checkedRow = row + j;
    			if(checkedRow < 0 || checkedRow >= board.rows() || (checkedCellsCopy[checkedRow][checkedCol] == true)) continue;
    			DFSBoggle(board, foundWords, checkedCellsCopy, checkedRow, checkedCol, currentWordCopy);
    		}
    	}
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
    	if (!trie.contains(word)) return 0;
    	int len = word.length();
    	if (len < 3) return 0;
    	else if(len < 5) return 1;
    	else if(len < 6) return 2;
    	else if(len < 7) return 3;		
    	else if(len < 8) return 5;	
    	else return 11;	
    }
    
	public static void main(String[] args) {
		BoggleBoard board = new BoggleBoard("D:\\java\\projects\\boggle\\boggle\\board-q.txt");
		System.out.println(board);
		In in = new In("D:\\java\\projects\\boggle\\boggle\\dictionary-algs4.txt");
		String [] dict = in.readAllLines();
		BoggleSolver bSolver = new BoggleSolver(dict);
		boolean key2 = bSolver.trie.hasPrefix("C");
		System.out.println(key2);
		
		int score = 0;
		for(String key3: bSolver.getAllValidWords(board)) {
			System.out.println(key3 + " " + bSolver.scoreOf(key3));
			score += bSolver.scoreOf(key3);
		}
		System.out.println("SCORE " + score);
		
		System.out.println("CITIZEN " + bSolver.scoreOf("CITIZEN"));
	}
	
}
