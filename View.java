/**
 * CS349 Winter 2014
 * Assignment 3 Demo Code
 * Jeff Avery & Michael Terry
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.Random;

/*
 * View of the main play area.
 * Displays pieces of fruit, and allows players to slice them.
 */
public class View extends JPanel implements ModelListener {
    private Model model;
    private final MouseDrag drag;
    public Timer timer;
    private JLabel gameOver;
    private JButton restart;

    // Constructor
    View (Model m) {
        model = m;
        model.addObserver(this);

        setBackground(Color.WHITE);
        
        timer = new Timer(10, model.timeUpdate);
  	  	timer.setRepeats(true);
  	  	timer.start();

        createFruits();
        
        // drag represents the last drag performed, which we will need to calculate the angle of the slice
        drag = new MouseDrag();
        // add mouse listener
        addMouseListener(mouseListener);
    }
    
    public void createFruits(){
    	int max = getPreferredSize().width / 2;
    	double x = Math.random() * max -75;
    	if (Math.random() >= 0.5){
        	x = x + max;
        }
    	if (x <= getPreferredSize().width/2 + 75 
    			&& x >= getPreferredSize().width/2 -75)
    		x = 0;
    	
    	double y = getPreferredSize().height;
    	int ix = (int) x;
    	int iy = (int) y;
    	
    	Fruit f = new Fruit(new Area(new Rectangle(ix, iy, 75, 75)));
    	f.setX(ix);
    	f.setY(iy);
    	if (x >=max){
    		f.setStart(1);
    	}
    	
    	
    	Random generator = new Random();
    	int i = generator.nextInt(7);
    	if (i == 0)
    		f.setType(Fruit.Type.ORANGE);
    	else if (i == 1)
    		f.setType(Fruit.Type.RED);
    	else if (i == 2)
    		f.setType(Fruit.Type.BLACK);
    	else if (i == 3)
    		f.setType(Fruit.Type.GREEN);
    	else if (i == 4)
    		f.setType(Fruit.Type.BLUE);
    	else if (i == 5)
    		f.setType(Fruit.Type.YELLOW);
    	else
    		f.setType(Fruit.Type.MAGENTA);
    	/*TODO randomize fruit types */
    	
    	model.add(f);
    }

    // Update fired from model
    @Override
    public void update() {
    	if (!timer.isRunning() && model.getFails() < 6){
			  timer.restart();
			  gameOver.setVisible(false);
			  restart.setVisible(false);
			  gameOver = null;
			  restart = null;
    	}
        this.repaint();
    }

    // Panel size
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500,400);
    }

    // Paint this panel
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        if(model.getFails() < 6){
        
	        // draw all pieces of fruit
	        // note that fruit is responsible for figuring out where and how to draw itself
	        for (Fruit s : model.getShapes()) {
	        	if (s.sliced == 1){
	        		model.remove(s);
	        	}
	        	if (s.piece == 1){
	        		s.setY(s.getY() +3);
	        	}
	        	else if (s.getDirection() == 1){
	        		s.setY(s.getY() - 3);
	        		// starting from the right and arcing up
	        		if (s.getStart() == 1){
	        			s.setX(s.getX() - 1);
	        			if (s.getX() == getPreferredSize().width/2 ||
	        					s.getY() <= 30){
	        				s.setX(s.getX() -1);
	        				s.setDirection(0);
	        			}
	        		}
	        		//starting from left and arcing up
	        		else{
	        			s.setX(s.getX() + 1);
	        			if (s.getX() == getPreferredSize().width/2 ||
	            				s.getY() <= 30){
	        				s.setX(s.getX() +1);
	        				s.setDirection(0);
	        			}
	        		}
	        	}
	        	//going down
	        	else{
	        		s.setY(s.getY() + 3);
	        		// started from the right
	        		if (s.getStart() == 1)
	        			s.setX(s.getX() - 1);
	        		//started from the left
	        		else
	        			s.setX(s.getX() + 1);
	        	}
	        	if (s.getY() > getPreferredSize().height+10 && s.piece == 0){
	        		if (s.piece == 0){
	        			model.setFails(model.getFails() + 1);
	        		}
	        		model.remove(s);
	        	}
	        	else
	        		s.draw(g2);
	        }
	        if (Math.random() * 1000 > 985){
	        	this.createFruits();
	        }
        }
        else{
        	int x = getPreferredSize().width/2 - 50;
        	gameOver = new JLabel("   Game Over");
    		gameOver.setVisible(true);
    		gameOver.setBounds(new Rectangle(x, 50, 100, 25));
          	this.add(gameOver); 
          	restart = new JButton("Restart?");
          	restart.setVisible(true);
          	restart.setBounds(new Rectangle(x, 75, 100, 25));
          	this.add(restart);
          	timer.stop();
          	restart.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   model.restart();
                }          
            });

          	
        }
    }

    // Mouse handler
    // This does most of the work: capturing mouse movement, and determining if we intersect a shape
    // Fruit is responsible for determining if it's been sliced and drawing itself, but we still
    // need to figure out what fruit we've intersected.
    private MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            drag.start(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            drag.stop(e.getPoint());

            // you could do something like this to draw a line for testing
            // not a perfect implementation, but works for 99% of the angles drawn
            
             /*int[] x = { (int) drag.getStart().getX(), (int) drag.getEnd().getX(), (int) drag.getEnd().getX(), (int) drag.getStart().getX()};
             int[] y = { (int) drag.getStart().getY()-1, (int) drag.getEnd().getY()-1, (int) drag.getEnd().getY()+1, (int) drag.getStart().getY()+1};
             model.add(new Fruit(new Area(new Polygon(x, y, x.length))));*/

            // find intersected shapes
            int offset = 0; // Used to offset new fruits
            for (Fruit s : model.getShapes()) {
                if (s.intersects(drag.getStart(), drag.getEnd())) {
                    model.setScore(model.getScore()+1);
                    try {
                        Fruit[] newFruits = s.split(drag.getStart(), drag.getEnd());
                        for (Fruit f : newFruits){
                        	model.add(f);
                        }

                        // add offset so we can see them split - this is used for demo purposes only!
                        // you should change so that new pieces appear close to the same position as the original piece
                       /* for (Fruit f : newFruits) {
                            f.translate(offset, offset);
                            model.add(f);
                            offset += 20;
                        }*/
                    } catch (Exception ex) {
                        System.err.println("Caught error: " + ex.getMessage());
                    }
                } else {}
            }
        }
    };

    /*
     * Track starting and ending positions for the drag operation
     * Needed to calculate angle of the slice
     */
    private class MouseDrag {
        private Point2D start;
        private Point2D end;

        MouseDrag() { }

        protected void start(Point2D start) { this.start = start; }
        protected void stop(Point2D end) { this.end = end; }

        protected Point2D getStart() { return start; }
        protected Point2D getEnd() { return end; }

    }
}
