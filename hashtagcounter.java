
import java.io.*;
import java.util.*;



//Main Class FibonacciHeap implementing Fibonacci Heap
public class hashtagcounter {

	public  Map map = new HashMap();  //HashMap for mapping all nodes with distinct hashtags 
	
	//Class node to define Node of heap
	public class node{
		String hashtag;
		int count;
		int degree;
		boolean childcut;
				
		node left,right,parent,child;		
	}
	
	node maxroot;     //To points the node with maximum frequency hashtag in circular list of roots 
	
	
	
	
	//--------------------------------- Insert Function -------------------------------------------
	//Creating node and inserting it next to maxroot into circular list of roots in circular doubly linked list of roots
	//This function is called when it has to create a node with new distinct hashtag
	public node insert(int n,String hash,node max,Map m)
	{
		//Creating new node and 
		node newnode = new node();
		newnode.count = n;
		newnode.hashtag = hash;
        newnode.left = null;
        newnode.right = null;
        newnode.degree = 0;
        newnode.parent = null;
        newnode.child = null;
        newnode.childcut = false;
        
        //Putting this new node into map with its mapping with it's hashtag
        m.put(hash, newnode);
        
        //For first insert when heap is empty (i.e. max is null) , making this new node Max
        if(max == null)
        {
          	max = newnode;
          	max.right = max;
          	max.left = max;
        }
        
        //If heap is not empty, Inserting new node in next position of Maxroot of heap
        else
        {        	     
        	     max.right.left = newnode;
        	     newnode.right = max.right;
        	     max.right = newnode;    
        	     newnode.left = max;
        	     
        	    //If this newly inserted node has higher frequency of hashtag than Maxroot frequency, making this new node the maxroot of heap 
        	    if(newnode.count > max.count)
        	    	max = newnode;
        	     
        }
        
        //Returning back the maxroot of Heap
        return max;
	}
	
	
	
	
	
	//------------------------------ insertnode Function----------------------------------------
	//Function to insert a node in circular doubly linked list of roots in heap
	public node insertNode(node max, node n)
	{	
            //Insert the node n at right side of the Maxroot in circular list of roots.
		     n.left = max;        
    	     n.right = max.right;
    	     max.right.left = n;
    	     max.right = n;
            
    	    //If new inserted node has higher hashtag frequency than the Maxroot node, make it the Maxroot of heap  
    	    if(n.count > max.count)
    	    	max = n;
    	//Return Maxroot    
		return max;
	}
	
	
	
	//----------------------------------- PairwiseCombine Function-------------------------
	//Function to Meld (Pairwise Combine) tress with same degrees after removal of Maxroot from heap
	public node pairwiseCombine(node max)
	{
		node presentMapNode=null, currentNode=null,nextNode=null,child=null,temp5=null;
		
		//Hashmap to keep track of tress with their degree
		Map m = new HashMap();
		
		if((!(max==null)))
		{
			
		  //Put first node with it's degree into Hashmap which is empty initially	
		  m.put(max.degree, max);
		  currentNode=max.right;
          
		  while(currentNode!=max)   //For each root node in list
		  {
			  nextNode = currentNode.right;
			 
			     while(true)      
			     {    
			    	 //To check if there is node already present in hashmap with same degree as of currentNode
			    	 if(!m.containsKey(currentNode.degree))
			    		 break;
			    	 //If not present break the loop
			    	 
			    	//If same degree node present in hashmap, get that node and remove it's entry from hashmap
			    	 presentMapNode = (node)m.get(currentNode.degree);
					  m.remove(presentMapNode.degree);
					  
					  
					  //If the node with same degree got from map has equal or higher frequency than currentNode, make currentNode it's child
					   if(presentMapNode.count>=currentNode.count)
					  {
						  
						  currentNode.left.right = currentNode.right;
						  currentNode.right.left = currentNode.left;
						   
						  
						  if(presentMapNode.child == null)
						  {
							  presentMapNode.child = currentNode;
							  presentMapNode.degree++;
							  currentNode.right=currentNode;
							  currentNode.left=currentNode;
							  currentNode.parent=presentMapNode; 						  
						  }
						  else
						  {
							  child= presentMapNode.child;							  
							  currentNode.parent = presentMapNode;
							  presentMapNode.degree++; 
							  child.right.left = currentNode;
							  currentNode.right = child.right;
							  currentNode.left = child;
							  child.right = currentNode;
							  
						  }
						  
						 
						  if(currentNode == max)
							  presentMapNode = max;
					      
						  currentNode = presentMapNode;
						  
						 
						  continue;
					  }
					 //If the node with same degree got from map has lesser frequency than currentNode, make currentNode it's parent
					  else
					  {
						  presentMapNode.right.left = presentMapNode.left;
						  presentMapNode.left.right = presentMapNode.right;
						  
						   if(currentNode.child == null)
						   {
							   currentNode.child =presentMapNode;
							   currentNode.degree++;
							   presentMapNode.left=presentMapNode;
							   presentMapNode.right=presentMapNode;
							   presentMapNode.parent=currentNode;
						   
						   }
						   else
						   {
							      child= currentNode.child;
								  presentMapNode.parent = currentNode;
								  currentNode.degree++;
							    
								  child.right.left = presentMapNode;
								  presentMapNode.right = child.right;
								  presentMapNode.left = child;
								  child.right = presentMapNode;
						   }
					  
					  }			    	 
			    	 
			     }
			  
			  //Put this node after meld into hashmap with it's degree
			  m.put(currentNode.degree, currentNode);
			  
			  //Take next node for traversal in rootnode list 
			  currentNode = nextNode;
		  	  
		  }
		  		  
		}
		else
			return null;
		

		return max;
	}
	
	
	
	
	//--------------------------------- RemoveMax Function -----------------------------
	//Function to remove the Maxroot from Heap
	public node removeMax(node max)
	{
		node nextmax=null,temp=null,temp1=null,child1=null,childlast=null;
		if(max==null)
		{
		    return null;
		}	
		else 
		{
			
            //If degree of maxroot is not zero, remove it's children and insert them into list of root nodes
			if(max.degree>0)
			{	
			
			child1 = max.child;
			childlast  = child1.left;
			max.degree = 0;
			max.child = null;
			max.right.left = childlast;
			childlast.right = max.right;
			max.right = child1;
			child1.left = max;
			
			do
			{
				child1.parent = null;
				child1.childcut = false;
				child1 = child1.right;
			}while(child1!=childlast.right);
			

			
			}
			
			
			int maxvalue=0;
			
			//If only node in root node list is maxroot
			if(max.right == max)
			  return null;
			
			temp=max.right;
			
			
			//Find the node with next maximum frequency
		    while(temp!=max)
		    {
		    	node n = temp.child;
		    	if(n!=null && n.right==max){
		    		System.out.println("stop");
		    		n=n.right;
		    	}

		    	if(temp.count>maxvalue)
		    	{
		    		nextmax=temp;
		    		maxvalue=temp.count;
		    	}
		    	
		    	temp=temp.right;
		    }
			
		    
		    //Delete Maxroot
		    max.right.left = max.left;
			max.left.right = max.right;
			this.map.remove(max.hashtag);
		
			max=nextmax;

		}
				
		return max;
	}
	
	
	


	//-------------------------  Displayroots Function -------------------------
	//Function to displaty all the nodes in the root nodes list
	public void displayRoots(node max)
	{
		
	 node temp;
	 
	 if(max != null)
	 {	 
	  for(temp = max; temp.right!=max; temp = temp.right )
	  {  
	     System.out.println("\n" + temp.hashtag + "\t" + temp.count);
	  }   
	  System.out.println("\n" + temp.hashtag + "\t" + temp.count);
	 }
	
           	   
	}
	
	

	
	//------------------------- Cascadcut Function ------------------------
	//After cutting the node, checking the node in the level above it for cascadcut
	public node cascadcut(node max, node parent)
	{
		node gparent = parent.parent;
		
		if(gparent!=null && parent.childcut==true) //If the parent's chilcut is true and it's not on root level, cut this node too
		{
		 max = 	this.cut(max,parent,gparent);
		
		}
		else if(gparent!=null && parent.childcut == false)
			parent.childcut = true;
		
		return max;
	}
	
	
	
	
	//---------------------------- Cut Function -----------------------------
	//Function to cut a node from it's parent and sibling list
	public node cut(node max, node n, node parent)
	{
	       n.parent = null;  //cut node from parent
				
		  if(n.left == n)    //If it's only child
			  parent.child = null;
		  else                  //else remove from sibling list
		  {	  
		    n.left.right = n.right;    
		    n.right.left = n.left;
		  
		   if(parent.child == n)
			   parent.child = n.right;
		  }
		  
		  
		n.right=null;
		n.left=null;
		
		parent.degree--;
		n.childcut = false;
		
		//insert the node which is cut into rootnode list
		max = this.insertNode(max, n);
		//call cascad function for further check of cut in levels above
		max = this.cascadcut(max,parent);
		
		return max;
	}
	
	
	
	
	//-------------------------- Main Function------------------------------------
	public static void main(String[] args){
		
	    //Creating a heap object
		hashtagcounter h = new hashtagcounter();
		h.maxroot = null; //setting it's maxroot to null initially
		String input="",hash="";
		int cnt=0,numberOfHighTags=0;
		node presentNode = null, parent=null,ins=null;
		node del = null,n=null,n1=null,n2=null;
		   
		try{
		//Setting Input and Output files	
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("output.txt"))); 
		//Reading from the input file till it reads 'stop'
		while((input = br.readLine() ) != null && !(input.equalsIgnoreCase("stop")))
		{	
	      String input_array[] = input.trim().split(" "); 
		
		  char c = input_array[0].charAt(0);
		  
		  //Checking if first word read is a hashtag
		  if(c=='#')
		  {
			  String hasht[] = input_array[0].trim().split("#");
			  hash=hasht[1];
			  cnt = Integer.parseInt(input_array[1]); //get frequency of that hashtag
		  
		  
		  //Checking any node already have that hashtag
		  if(!h.map.containsKey(hash))
		  {  
		  	//If no node with same hashtag already, call insert to create a node for this hashtag and insert it into rootnode list  
		    h.maxroot =  h.insert(cnt,hash,h.maxroot,h.map);
		  }
		  else //If there is a node already with same hashtag
			 {
			  presentNode = (node)h.map.get(hash); //Get that node using hashmap
			  
			  
			  //Increase frequency of that hashtag which was alreay present in the heap
			  presentNode.count = presentNode.count + cnt;
			  parent = presentNode.parent;
			  
			 //after increasing frequency, checking if it's exceeding frequency of it's parent
			  if(parent!=null)
			 {	 
			  if((presentNode.count > parent.count))
			    {
				 h.maxroot =  h.cut(h.maxroot, presentNode, parent);  		       
			    }
			 }  
			  
				 //If frequency of PresentNode is exceeding frequency of Maxroot, make it maxroot		 
				 if(presentNode.count>h.maxroot.count)
					 h.maxroot=presentNode;
			  
			 }    		
		
		  }		
		  
		  else{       //If the input is just a number without any hashtag, start procedure of removing those many (let numberOfHighTags) maxnodes to get (numberOfHighTags) high frequency hash-tags
			  
			  numberOfHighTags = Integer.parseInt(input_array[0]);
			  
			  del=null;
			  
			  //Store those high tags nodes in list 'del' and remove using removeMax function
			    for(int i=0;i<numberOfHighTags-1;i++)
			    {
			    
			        if(del==null)
			        {			        	
			        	del = h.new node();
			        	del.hashtag = h.maxroot.hashtag;
			        	del.count = h.maxroot.count;
			        	del.right = del;
			        	del.left = del;
			        	del.degree = 0;
			        	del.child = null;
			        	del.parent= null;
			        	del.childcut=false;
			        	
			        				        	
			        }
			        else
			        {
			        	n = h.new node();
			        	n.hashtag = h.maxroot.hashtag;
			        	n.count = h.maxroot.count;    				        	
			        	n.degree = 0;
			        	n.child = null;
			        	n.parent= null;
			        	n.childcut=false;
                        
			        	n.left  = del;
			        	n.right = del.right;
			        	del.right.left = n;
			        	del.right = n;
			        	
			        	if(del.left == del)
			        		del.left = n;
			        	
			        	 	
			        }
			        
			        bw.write(h.maxroot.hashtag + ","+ " ");
			        h.maxroot= h.removeMax(h.maxroot);  	
			    }
			    
			    if(del==null)
		        {			        	
		        	del = h.new node();
		        	del.hashtag = h.maxroot.hashtag;
		        	del.count = h.maxroot.count;
		        	del.right = del;
		        	del.left = del;
		        	del.degree = 0;
		        	del.child = null;
		        	del.parent= null;
		        	del.childcut=false;
		        	
		        				        	
		        }
		        else 
		        {
		        	n = h.new node();
		        	n.hashtag = h.maxroot.hashtag;
		        	n.count = h.maxroot.count;    				        	
		        	n.degree = 0;
		        	n.child = null;
		        	n.parent= null;
		        	n.childcut=false;
                 
		        	n.left  = del;
		        	n.right = del.right;
		        	del.right.left = n;
		        	del.right = n;
		        	
		        	if(del.left == del)
		        		del.left = n;
		        	
		        	 	
		        }
		        
		        bw.write(h.maxroot.hashtag);
		        h.maxroot= h.removeMax(h.maxroot);  	
			    
			     bw.write("\r\n");
			     n1 = del.left;
			     n2 = n1.left;
			     
			     
			   //After getting required number of High frequency hashtags, put them back from 'del' into Heap
			    for(int i=0;i<numberOfHighTags;i++)
			    {
			    	
			    	//n2 = n1.left;
			    	
			    	n1.left.right = n1.right;
			    	n1.right.left = n1.left;
			    	
			    	n1.right = null;
			    	n1.left = null;
			    	h.map.put(n1.hashtag, n1);
			
			    	h.maxroot = h.insertNode(h.maxroot, n1);
			                	
			    	//insert back all del linked nodes back into heap.    	
			    	n1 = n2;
			    	n2 = n1.left;
			    }
		 
		      }
		  
		}
			    
	     br.close();              //Close files
	     bw.close();
		}catch(Exception e){
			System.out.println("IO Exception");
			e.printStackTrace();
		}
	
	}
		
}
