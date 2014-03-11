	/**
	 * CS349 Winter 2014
	 * Assignment 3 Demo Code
	 * Jeff Avery & Michael Terry
	 */
	import java.util.ArrayList;
	import java.util.Vector;
	import javax.swing.Timer;
	import java.awt.event.*;
	
	/*
	 * Class the contains a list of fruit to display.
	 * Follows MVC pattern, with methods to add observers,
	 * and notify them when the fruit list changes.
	 */
	public class Model{// implements ActionListener{
	  // Observer list
	  private Vector<ModelListener> views = new Vector();
	
	  // Fruit that we want to display
	  private ArrayList<Fruit> shapes = new ArrayList();
	  
	  private	int score = 0;
	  private	int fails = 0;
	  

	  // Constructor
	  Model() {
	    shapes.clear();	    
	  }
	  
	  public void restart(){
		  shapes.clear();
		  this.score = 0;
		  this.fails = 0;
		  for (ModelListener v : views) {
		      v.update();
		  }
	  }
	
	  // MVC methods
	  // These likely don't need to change, they're just an implementation of the
	  // basic MVC methods to bind view and model together.
	  public void addObserver(ModelListener view) {
	    views.add(view);
	  }
	
	  // get score
	  public int getScore(){
		  return this.score;
	  }
	  
	  //set score
	  public void setScore(int s){
		  this.score = s;
	  }
	  
	  // get fails
	  public int getFails(){
		  return this.fails;
	  }

	  // set fails
	  public void setFails(int f){
		  this.fails = f;
	  }
	  
	  public void notifyObservers() {
	    for (ModelListener v : views) {
	      v.update();
	    }
	    if (this.fails >= 6){
	    	for (ModelListener v : views) {
	  	      v.update();
	  	    }
	    	/*for (Fruit f : shapes){
	    		remove(f);
	    	}*/
	    }
	  }
	
	  // Model methods
	  // You may need to add more methods here, depending on required functionality.
	  // For instance, this sample makes to effort to discard fruit from the list.
	  public void add(Fruit s) {
	    shapes.add(s);
	  }
	  
	  public void remove(Fruit s){
		  int i = shapes.indexOf(s);
		  shapes.remove(s);
		  s = null;
	  }
	
	  public ArrayList<Fruit> getShapes() {
	      return (ArrayList<Fruit>)shapes.clone();
	  }
	  
	  public int numFruits(){
		  return shapes.size();
	  }
	  
	  //@Override
	  ActionListener timeUpdate = new ActionListener() {
		  public void actionPerformed(ActionEvent e){
			  notifyObservers();
		  }
	  };
	}
