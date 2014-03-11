/**
 * CS349 Winter 2014
 * Assignment 3 Demo Code
 * Jeff Avery & Michael Terry
 */
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.*;

/*
 * View to display the Title, and Score
 * Score currently just increments every time we get an update
 * from the model (i.e. a new fruit is added).
 */
public class TitleView extends JPanel implements ModelListener {
  private Model model;
  private JLabel title, score, time, lives;
  private int count = 0;
  private int gameTime = 0;
  public Timer timer;

  // Constructor requires model reference
  TitleView (Model model) {
    // register with model so that we get updates
    this.model = model;
    this.model.addObserver(this);

    // draw something
    setBorder(BorderFactory.createLineBorder(Color.black));
    setBackground(Color.YELLOW);
    // You may want a better name for this game!
    title = new JLabel(" CS349 Fruit Slicer Game");
    score = new JLabel("Score");
    time = new JLabel("Time");
    lives = new JLabel("Lives");

    // use border layout so that we can position labels on the left and right
    this.setLayout(new BorderLayout());
    this.add(title, BorderLayout.CENTER);
    this.add(score, BorderLayout.WEST);
    this.add(lives, BorderLayout.SOUTH);
    this.add(time, BorderLayout.EAST);

    timer = new Timer(1000, gameTimeUpdate);
    timer.setRepeats(true);
    timer.start();
  }

  ActionListener gameTimeUpdate = new ActionListener() {
	  public void actionPerformed(ActionEvent e){
		  updateGameTime();
	  }
  };
  
  public void updateGameTime(){
	  this.gameTime = this.gameTime+1;
  }
  
  // Panel size
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(500,35);
  }

  // Update from model
  // This is ONLY really useful for testing that the view notifications work
  // You likely want something more meaningful here.
  @Override
  public void update() {
	  if (model.getFails() >= 6){
		timer.stop();
	  }
	  else
		  if (!timer.isRunning()){
			  timer.restart();
			  gameTime = 0;
		  }
		  paint(getGraphics());
  }

  // Paint method
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    score.setText("Score: " + model.getScore() + "                  ");
    int min = this.gameTime/60;
    int sec = gameTime%60;
    if (sec <10)
    	time.setText("Time: " + min + ":0" + sec + "   ");
    else
    	time.setText("Time: " + min + ":" + sec + "   ");
    int rem = Math.max(5-model.getFails(), 0);
    lives.setText("Lives Remaining: " + rem);
  }
  
  
}


