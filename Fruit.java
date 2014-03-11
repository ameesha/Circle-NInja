/**
 * CS349 Winter 2014
 * Assignment 3 Demo Code
 * Jeff Avery & Michael Terry
 */
import java.awt.*;
import java.awt.geom.*;
import java.math.*;
import java.util.Collection;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D.Double;	

/**
 * Class that represents a Fruit. Can be split into two separate fruits.
 */
public class Fruit implements FruitInterface {
	public enum Type {
    	ORANGE, RED, BLACK, GREEN, BLUE, YELLOW, MAGENTA
    }
	private Type			fruitType 	 = null;
    private Area            fruitShape   = null;
    private Color           fillColor    = Color.RED;
    private Color           outlineColor = Color.BLACK;
    private AffineTransform transform    = new AffineTransform();
    private double          outlineWidth = 5;
    private double 				x = 0;
    private double 				y = 0;
    private int					direction = 1; //1 = up, 0 is down
    private int					start = 0; // 0 = left, 1 = right
    public int				sliced = 0; // 0 if not, 1 if sliced
    public int				piece = 0; // 0 if not a piece, 1 if a left piece, 2 if a right piece, 3 if top, 4 if bottom
    
    /** get start **/
    public int getStart(){
    	return this.start;
    }
    
    public void setStart(int i){
    	this.start = i;
    }
    
    /** get X **/
    public double getX(){
    	return this.x;
    }
    /** set X **/
    public void setX(double val){
    	this.x = val;
    }
    /** get Y **/
    public double getY(){
    	return this.y;
    }
    /** set Y **/
    public void setY(double val){
    	this.y = val;
    }
    /** get type **/
    public Type getType(){
    	return this.fruitType;
    }
    
    /** set type **/
    public void setType(Type type){
    	this.fruitType = type;
    }
    
    /** get direction **/
    public int getDirection(){
    	return this.direction;
    }
    
    /** set direction **/
    public void setDirection(int i){
    	this.direction = i;
    }

    /**
     * A fruit is represented using any arbitrary geometric shape.
     */
    Fruit (Area fruitShape) {
        this.fruitShape = (Area)fruitShape.clone();
    }

    /**
     * The color used to paint the interior of the Fruit.
     */
    public Color getFillColor() {
        return fillColor;
    }
    /**
     * The color used to paint the interior of the Fruit.
     */
    public void setFillColor(Color color) {
        fillColor = color;
    }
    /**
     * The color used to paint the outline of the Fruit.
     */
    public Color getOutlineColor() {
        return outlineColor;
    }
    /**
     * The color used to paint the outline of the Fruit.
     */
    public void setOutlineColor(Color color) {
        outlineColor = color;
    }
    
    /**
     * Gets the width of the outline stroke used when painting.
     */
    public double getOutlineWidth() {
        return outlineWidth;
    }

    /**
     * Sets the width of the outline stroke used when painting.
     */
    public void setOutlineWidth(double newWidth) {
        outlineWidth = newWidth;
    }

    /**
     * Concatenates a rotation transform to the Fruit's affine transform
     */
    public void rotate(double theta) {
        transform.rotate(theta);
    }

    /**
     * Concatenates a scale transform to the Fruit's affine transform
     */
    public void scale(double x, double y) {
        transform.scale(x, y);
    }

    /**
     * Concatenates a translation transform to the Fruit's affine transform
     */
    public void translate(double tx, double ty) {
        transform.translate(tx, ty);
    }

    /**
     * Returns the Fruit's affine transform that is used when painting
     */
    public AffineTransform getTransform() {
        return (AffineTransform)transform.clone();
    }

    /**
     * Creates a transformed version of the fruit. Used for painting
     * and intersection testing.
     */
    public Area getTransformedShape() {
        return fruitShape.createTransformedArea(transform);
    }

    /**
     * Paints the Fruit to the screen using its current affine
     * transform and paint settings (fill, outline)
     */
    public void draw(Graphics2D g2) {
    	if (this.fruitType == Type.ORANGE){
    		 g2.setPaint(Color.orange);
    	}
    	else if (this.fruitType == Type.RED){
    		g2.setPaint(Color.red);
    	}
    	else if (this.fruitType == Type.BLACK){
    		g2.setPaint(Color.black);  		
    	}
    	else if (this.fruitType == Type.GREEN){
    		g2.setPaint(Color.green);
    	}
    	else if (this.fruitType == Type.YELLOW){
    		g2.setPaint(Color.yellow);
    	}
    	else if (this.fruitType == Type.MAGENTA){
    		g2.setPaint(Color.magenta);
    	}
    	else{
    		g2.setPaint(Color.blue);
    	}
    	
    	if (this.piece > 0){
    		if (this.fruitType == Type.ORANGE){
    			g2.setPaint(Color.orange);
    		}
    		else if (this.fruitType == Type.BLACK){
    			g2.setPaint(Color.black);
    		}
    		else if (this.fruitType == Type.RED){
        		g2.setPaint(Color.red);
    		}
    		else if (this.fruitType == Type.BLUE)
    			g2.setPaint(Color.blue);
    		else if (this.fruitType == Type.MAGENTA)
    			g2.setPaint(Color.magenta);
    		else if (this.fruitType == Type.GREEN)
    			g2.setPaint(Color.green);
    		else if (this.fruitType == Type.YELLOW)
    			g2.setPaint(Color.yellow);
    		else
    			g2.setPaint(Color.orange);
    		Arc2D arc = new Arc2D.Double();
    		if (this.piece == 1)
    			arc.setArc(this.getX(), this.getY(), (double) 75, 75,90, 180, Arc2D.CHORD);
    		else if (this.piece ==2)
    			arc.setArc(this.getX(), this.getY(), (double) 75, 75,270, 180, Arc2D.CHORD);
    		else if (this.piece == 3)
    			arc.setArc(this.getX(), this.getY(), (double) 75, 75, 0, 180, Arc2D.CHORD);
    		else
    			arc.setArc(this.getX(), this.getY(), (double) 75, 75,180, 180, Arc2D.CHORD);
    		g2.fill(arc);
    		this.setDirection(0);
    	}
    	else{
    		g2.fill(new Ellipse2D.Double(this.x,this.y,75,75));
    	}
    }

    /**
     * Tests whether the line represented by the two points intersects
     * this Fruit.
     */
    public boolean intersects(Point2D p1, Point2D p2) {
    	double xDiff = p2.getX() - p1.getX();
    	double yDiff = p2.getY() - p1.getY();
    	if (this.piece >0  || (xDiff == 0 && yDiff == 0)){
    		return false;
    	}
    	if (xDiff < 75 && yDiff < 75)
    		return false;
    	
    	Rectangle2D.Double rect = new Rectangle2D.Double();
    	rect.setFrame(this.getX(), this.getY(), (double) 75,(double) 75);
    	boolean intersect = rect.getBounds2D().intersectsLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    	if (intersect){
    		return true;
    	}
    	else return false;
    }

    /**
     * Returns whether the given point is within the Fruit's shape.
     */
    public boolean contains(Point2D p1) {
        return this.getTransformedShape().contains(p1);
    }

    /**
     * This method assumes that the line represented by the two points
     * intersects the fruit. If not, unpredictable results will occur.
     * Returns two new Fruits, split by the line represented by the
     * two points given.
     */
    public Fruit[] split(Point2D p1, Point2D p2) throws NoninvertibleTransformException {
    	this.sliced = 1;
        Area topArea = null;
        Area bottomArea = null;

        if (topArea == null && bottomArea == null){
        	double rx = p2.getX()-p1.getX();
        	double ry = p2.getY()-p1.getY();
        	double rangle = Math.atan2(ry,  rx);
        	
        	AffineTransform aTransform = new AffineTransform();
        	aTransform.rotate(-rangle);
        	aTransform.translate(-p1.getX(), -p1.getX());
        	Area area = new Area();
        	area = area.createTransformedArea(aTransform);
        	
        	Rectangle2D bounds = area.getBounds2D();
        	
        	double halfMinY = Math.min(0,  bounds.getMinY());
        	double halfMaxY = Math.min(0,  bounds.getMaxY());
        	Rectangle2D half = new Rectangle2D.Double(bounds.getX(), halfMinY,
        			bounds.getWidth(), halfMaxY-halfMinY);
        	
        	double halfMinY1 = Math.max(0, bounds.getMinY());
        	double halfMaxY1 = Math.max(0, bounds.getMaxY());
        	Rectangle2D half1 = new Rectangle2D.Double(bounds.getX(), halfMinY1,
        			bounds.getWidth(), halfMaxY1-halfMinY1);
        	
        	Area a0 = new Area(area);
        	a0.subtract(new Area(half1));
        	Area a1 = new Area(area);
        	a1.subtract(new Area(half));
        	
        	try{
        		aTransform.invert();
        	}
        	catch (NoninvertibleTransformException e){}
        	
        	a0 = a0.createTransformedArea(aTransform);
        	a1 = a1.createTransformedArea(aTransform);
        	
        	double xDiff = Math.abs(p2.getX() - p1.getX());
        	double yDiff = Math.abs(p2.getY() - p1.getY());
        	
        	Fruit f1 = new Fruit(a0);
        	f1.setX(this.getX());
        	f1.setY(this.getY());
        	f1.fruitType = this.fruitType;
        	Fruit f2 = new Fruit(a1);

        	f2.fruitType = this.fruitType;
        	
        	if (xDiff <= yDiff){
        		f2.setX(this.getX() + 30);
        		f2.setY(this.getY());
        		f1.piece = 1;
        		f2.piece = 2;
        	}else{
        		f2.setX(this.getX());
        		f2.setY(this.getY() + 10);
        		f1.piece = 3;
        		f2.piece = 4;
        	}
        	
        	return new Fruit[] { f1, f2 };   
        }  	
        return new Fruit[] { new Fruit(topArea), new Fruit(bottomArea) };
     }
}
