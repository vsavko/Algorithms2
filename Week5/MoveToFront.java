import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
	private static char numOfChar = 256;
	
	private static int getPoistion(char searchedElement, Node firstElement) {
		Node previousElement = null;
		int returnElement = -1;
		int counter = 0;
		
		while(firstElement != null) {
			if (firstElement.value == searchedElement) {
				returnElement = counter;
				if (previousElement !=  null) previousElement.next = firstElement.next;
				break;
			}
			counter++;
			previousElement = firstElement;
			firstElement = firstElement.next;
		}
		return returnElement;	
	}
	
	private static char getPoistionByIndex(char indexNr, Node firstElement) {
		Node previousElement = null;
		int counter = 0;
		
		while(counter < indexNr) {
			previousElement = firstElement;
			firstElement = firstElement.next;
			counter++;
		}
		
		if(previousElement != null)
			previousElement.next = firstElement.next;
		
		return firstElement.value;	
	}
	
	
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
 	
    	Node initialRoot = new Node((char)0);
    	Node root = initialRoot;

    	for(char i = 1; i < numOfChar; i++) {
    		root.next = new Node(i);	
    		root = root.next;
    	}
    	
    	while(!BinaryStdIn.isEmpty()) {
	    	char s = BinaryStdIn.readChar();
	    	int index = getPoistion(s,initialRoot);
	    	BinaryStdOut.write((byte)index);
	    	if (index != 0) {
		    	root = new Node((char)s);
		    	root.next = initialRoot;
		    	initialRoot = root;
	    	}
    	}
    	BinaryStdIn.close();
    	BinaryStdOut.flush();    	
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
    	Node initialRoot = new Node((char)0);
    	Node root = initialRoot;

    	for(char i = 1; i < numOfChar; i++) {
    		root.next = new Node(i);	
    		root = root.next;
    	}
    	
    	while(!BinaryStdIn.isEmpty()) {
	    	char s = BinaryStdIn.readChar();
	    	char index = getPoistionByIndex((char)s,initialRoot);
	    	BinaryStdOut.write((byte)index);
	    	if (s != 0) {
		    	root = new Node(index);
		    	root.next = initialRoot;
		    	initialRoot = root;
	    	}
    	}
    	
    	BinaryStdIn.close();
    	BinaryStdOut.flush();   
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
    	
    	if (args[0].equals("-")) {
    		MoveToFront.encode();
    	}
    	if (args[0].equals("+")) {
    		MoveToFront.decode();
    	}

    }
}
