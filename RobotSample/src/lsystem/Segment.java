/*
  Copyright 2006 by Daniel Kuebrich
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

// Class Segment
package lsystem;
import java.awt.*;
import sim.portrayal.*;
import sim.util.*;

public  class Segment extends SimplePortrayal2D
    {
    private static final long serialVersionUID = 1519670365452291522L;
    public double x,y,x2,y2,x1,y1,x3,y3;
    public double w,h; //for comparing if robot at the end
    public double dist;
    
    public Segment(double x, double y, double dist, double theta)
        {
        this.x = x;
        this.y = y;
        this.x2 = /*Strict*/Math.cos(theta)*dist;
        this.y2 = /*Strict*/Math.sin(theta)*dist;
        this.dist = dist;
        }
    
    public void draw(Object object,  final Graphics2D g, final DrawInfo2D info )
        {
        g.setColor(new Color(0,220,0));
        w = info.draw.width;
        h = info.draw.height;
        
        x3 = (int)(info.draw.width*x) + (int)(info.draw.width*x2);
        y3 = (int)(info.draw.height*y) + (int)(info.draw.height*y2);  //not accurate
        x1 = (int)(info.draw.width*x);
        y1 = (int)(info.draw.height*y);
        
        g.drawLine((int)(info.draw.x),
            (int)(info.draw.y),
            (int)(info.draw.x) + (int)(info.draw.width*x2),
            (int)(info.draw.y) + (int)(info.draw.height*y2));
//        g.drawLine((int)(x*w),(int)(y*h) ,(int)((x+x2)*w),(int)((y+y2)*h));
        
//        System.out.println("drawline --------------"+x1+y1+x3+y3);
//        System.out.println("line2-----------"+(int)(info.draw.width*x2)+(int)(info.draw.height*y2));
        }

    public void hitObjects(DrawInfo2D range, Bag putInHere)
        {
        // don't want to have any inspectors
        }
    
    public double getX(){return x;}
    public double getY(){return y;}
    }
