import edu.princeton.cs.algs4.In;

public class RwayTST {
	private int maxLevel;
	private Node root;      // root of trie
	
	public class Node{
		private int level;
		private Node [] subtries;
		private Node left, mid, right;  // left, middle, and right subtries
		private Object val;
		private char c;   
	}
	
	public RwayTST(int maxLevel){
		this.maxLevel = maxLevel;
	}
	
    public void put(String key, int val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        else root = put(root, key, val, 0);
    }

	private Node put(Node x, String key, int val, int d) {
		char c;
    	if(x == null) {
    		x = new Node();
    		x.val = null;
    		x.level = d;
        	if (d < maxLevel) { 
        		x.subtries = new Node[26];
        	}
        	else if(d < key.length()){
        		c = key.charAt(d);
        		x.c = c;
        	}
    	}
    	//System.out.println(d);
    	
    	if (key.length()  == d ) {
    		//System.out.println(c);
    		x.val = val;
    		return x;
    	}
    	
    	//System.out.println(key + " " + d);
    	c = key.charAt(d);
    	if (d < maxLevel) {
    		x.subtries[c-'A'] = put(x.subtries[c-'A'],key,val,d+1);
    	}
    	else {
            if      (c < x.c)               x.left  = put(x.left,  key, val, d);
            else if (c > x.c)               x.right = put(x.right, key, val, d);
            else if (d < key.length() )  	x.mid   = put(x.mid,   key, val, d+1);
            else                            x.val   = val;
    	}
    	
    	return x;
    }
	
    public boolean hasPrefix(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return true;
    }
    
    public Object get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(root.subtries[key.charAt(0)-'A'], key, 1);
        if (x == null) return null;
        return x.val;
    }
    
    private Node get(Node x, String key, int d) {
    	//System.out.println("not node" + d );
        if (x == null) { 
        	//System.out.println("null node");
        	return null;
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");


    	if (key.length()  == d ) {
    		//System.out.println("TEST" + d);
    		return x;
    	}
    	char c = key.charAt(d);
        
        if (x.level < maxLevel) {
            return get(x.subtries[c-'A'], key, d+1);
        }
        else {
	        if      (c < x.c)              return get(x.left,  key, d);
	        else if (c > x.c)              return get(x.right, key, d);
	        else if (d < key.length() )	   return get(x.mid,   key, d+1);
	        else                           return x;
        }
    }
    
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }
    
    
    public static void main(String[] args) {
    	RwayTST rTST = new RwayTST(3);
		In in = new In("D:\\java\\projects\\boggle\\boggle\\dictionary-test.txt");
		String [] dict = in.readAllLines();
		int i = 0;
    	for(String key: dict) {
    		rTST.put(key, i++);
    	}
    	
    	//rTST.put("BB",0);
    	//rTST.put("BBBB",1);
    	System.out.println(rTST.get("ITEM"));
    	System.out.println(rTST.get("AA"));
    	//System.out.println(rTST.get("BB"));
    	
    	
	}
}
