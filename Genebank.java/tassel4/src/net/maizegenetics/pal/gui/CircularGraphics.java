// CircularGraphics.java
//
// (c) 1999-2001 PAL Development Core Team
//
// This package may be distributed under the
// terms of the Lesser GNU General Public License (LGPL)

package net.maizegenetics.pal.gui;

import java.awt.*;

/**
		@author Matthew Goode
*/

public class CircularGraphics {

	double worldRadius_, worldAngle_;
	public int screenCentreX;
	public int screenCentreY;
	public int screenRadius;

	final static double TWO_PI = 2*Math.PI;

	Graphics g_;
	FontMetrics fm_;
	Font font_;
	boolean invertY_;

	public CircularGraphics(Graphics g, double worldAngle, double worldRadius, int screenX, int screenY, int screenWidth, int screenHeight) {
		this(g,worldAngle,worldRadius,screenX, screenY, screenWidth, screenHeight,false);
	}
	public CircularGraphics(Graphics g, double worldAngle, double worldRadius, int screenX, int screenY, int screenWidth, int screenHeight, boolean invertY) {
		this.g_ = g;
		this.invertY_ = invertY;
		this.worldAngle_ = worldAngle;
		this.worldRadius_ = worldRadius;
		screenRadius = Math.min(screenWidth/2, screenHeight/2);
		screenCentreX = screenX+screenWidth/2;
		screenCentreY = screenY+screenHeight/2;

		font_ = g_.getFont();
		if(font_!=null) {
			fm_ = g_.getFontMetrics(font_);
		}
	}

	public void setFont(Font newFont) {
		g_.setFont(newFont);
		this.font_ = newFont;
		fm_ = g_.getFontMetrics(font_);
	}

	/** For drawing arbitary lines */
	public void drawLine( double angle1, double radius1, double angle2, double radius2) {
		g_.drawLine(getScreenX(angle1,radius1), getScreenY(angle1, radius1), getScreenX(angle2,radius2),getScreenY(angle2,radius2) );
	}

	/** For drawing arbitary lines */
	public void drawLine( double angle, double radiusStart, double radiusEnd) {
		g_.drawLine(getScreenX(angle,radiusStart), getScreenY(angle, radiusStart), getScreenX(angle,radiusEnd),getScreenY(angle,radiusEnd) );
	}

	/** For drawing arbitary lines */
	public void drawLineDegreeAlign( double angle, double radiusStart, double radiusEnd) {
		angle = ((int)(angle*360/worldAngle_))*worldAngle_/360;
		g_.drawLine(getScreenX(angle,radiusStart), getScreenY(angle, radiusStart), getScreenX(angle,radiusEnd),getScreenY(angle,radiusEnd) );
	}
	private final double convertRadius(double radius) {
		//return radius;
		return worldRadius_ - radius;
	}

	/** For drawing arbitary lines */
	public void drawArc( double angleStart, double angleEnd, double radius) {
		int actualRadius = (int)(screenRadius*convertRadius(radius)/worldRadius_);
		if(actualRadius<1) {
			return;
		}
		int startAngle, endAngle;
//		if(invertY_) {
			startAngle = (int)(angleStart*360/worldAngle_);
			endAngle = (int)(angleEnd*360/worldAngle_);
//		} else {
//			startAngle = (int)(angleStart*360/worldAngle_);
//			endAngle = (int)(angleEnd*360/worldAngle_);
//		}
//		if(invertY_) {
//			g_.drawArc(screenCentreX-actualRadius,  screenCentreY+actualRadius, actualRadius*2,-actualRadius*2, startAngle, endAngle-startAngle );
//		} else {
			g_.drawArc(screenCentreX-actualRadius,  screenCentreY-actualRadius, actualRadius*2,actualRadius*2, startAngle, endAngle-startAngle );
			/*System.out.println(""+(screenCentreX-actualRadius)+ "   "+
												( screenCentreY-actualRadius)+ "   " +
												( actualRadius*2) + "    " +
												( actualRadius*2 ) + "   " +
												( startAngle ) + "    " +
												( endAngle-startAngle ));
        */
//		}
	}

	public void setColor(Color c) {
		g_.setColor(c);
	}


	public void drawString(String s, double angle, double radius) {
		int sX =getScreenX(angle,radius);
		int sY = getScreenY(angle,radius);
		FontMetrics fm = g_.getFontMetrics(g_.getFont());
		int stringWidth = fm.stringWidth(s);
		int stringHeight = fm.getHeight();

		g_.drawString(s,sX-stringWidth/2,sY+stringHeight/2);


	}
	public void drawString(String s, double angle, double radius,int outdent) {
		int sX =getScreenX(angle,radius,outdent);
		int sY = getScreenY(angle,radius,outdent);
		FontMetrics fm = g_.getFontMetrics(g_.getFont());
		int stringWidth = fm.stringWidth(s);
		int stringHeight = fm.getHeight();

		g_.drawString(s,sX-stringWidth/2,sY+stringHeight/2);
	}

	public void circleString(String s, double angle, double radius,int outdent) {
		int sX =getScreenX(angle,radius,outdent);
		int sY = getScreenY(angle,radius,outdent);
		FontMetrics fm = g_.getFontMetrics(g_.getFont());
		int stringWidth = fm.stringWidth(s);
		int stringHeight = fm.getHeight();

		g_.drawString(s,sX-stringWidth/2,sY+stringHeight/2);
		g_.drawOval(sX-stringWidth/2-5,sY-stringHeight/2-2 ,stringWidth+10, stringHeight+10);
	}
	public int getScreenX(double angle, double radius) {
		return screenCentreX + (int)(convertRadius(radius)*screenRadius*Math.cos(angle*TWO_PI/worldAngle_)/worldRadius_);
	}
	public int getScreenX(double angle, double radius, int outdent) {
		double trigBit = Math.cos(angle*TWO_PI/worldAngle_);
		return screenCentreX + (int)(outdent*trigBit + convertRadius(radius)*screenRadius*trigBit/worldRadius_);
	}

	public int getScreenDeltaX(double angle, double radius) {

		return (int)(convertRadius(radius)*screenRadius*Math.cos(angle*TWO_PI/worldAngle_
								)/worldRadius_ );

	}

	public int getScreenY(double angle, double radius) {
		int offset = (int)(convertRadius(radius)*screenRadius*Math.sin(angle*TWO_PI/worldAngle_)/worldRadius_);

		return (invertY_ ? (screenCentreY + offset) : (screenCentreY - offset));
	}
	public int getScreenY(double angle, double radius, int outdent) {
		double trigBit = Math.sin(angle*TWO_PI/worldAngle_);
		int offset =  (int)(outdent*trigBit + convertRadius(radius)*screenRadius*trigBit/worldRadius_);
		return (invertY_ ? (screenCentreY + offset) : (screenCentreY - offset));
	}

	public int getScreenDeltaY(double angle, double radius) {
		return (invertY_ ? 1 : -1 )*(int)(convertRadius(radius)*screenRadius*Math.sin(angle*TWO_PI/worldAngle_)/worldRadius_);
	}
	public void fillPoint(double angle, double radius, int size) {
		int x = getScreenX(angle,radius);
		int y = getScreenY(angle,radius);

		g_.fillOval(x-size,y-size,2*size,2*size);
	}


	public void drawPoint(double angle, double radius, int size) {
		int x = getScreenX(angle,radius);
		int y = getScreenY(angle,radius);

		g_.drawOval(x-size,y-size,2*size,2*size);
	}

	public void drawSymbol(double angle, double radius, int outdent, int width, int symbol) {

		int halfWidth = width / 2;
		int x = getScreenX(angle,radius,outdent);
		int y = getScreenY(angle,radius,outdent);

		switch (symbol% 6) {
			case 0: g_.fillRect(x, y, width, width); break;
			case 1: g_.drawRect(x, y, width, width); break;
			case 2: g_.fillOval(x, y, width, width); break;
			case 3: g_.drawOval(x, y, width, width); break;
			case 4: // draw triangle
				g_.drawLine(x, y + width, x + halfWidth, y);
				g_.drawLine(x + halfWidth, y, x + width, y + width);
				g_.drawLine(x, y + width, x + width, y + width);
				break;
			case 5: // draw X
				g_.drawLine(x, y, x + width, y + width);
				g_.drawLine(x, y + width, x + width, y);
				break;
		}
	}








}
