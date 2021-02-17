package Infrastructure;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PersistenceDelegate;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class timers {

private int seconds = 0;
private pendulum pen;
public timers(pendulum pen) {
	this.pen = pen;
	
}
public void frame_transformation() {
	 final Timer timer = new Timer();
     timer.scheduleAtFixedRate(new TimerTask() {
    	 int seconds = 2;
         public void run() {
					--seconds;
					if (seconds <=  0) {
						timer.cancel();
						if(pendulum.frame.getExtendedState()!=JFrame.MAXIMIZED_BOTH)
							pendulum.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
						else
							pendulum.frame.setExtendedState(JFrame.NORMAL);
							pendulum.frame.setVisible(true);
					}
         }
     }, 0, 760);
}
public void half() {
	final Timer timer = new Timer();
	timer.scheduleAtFixedRate(new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			seconds++;
			
		}
		
	},0,1000);
}
public void roboPress() {
	final Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        public void run() {
        			++seconds;
        			for(joint2Path line: pendulum.joint2Lines) {
        				line.setAlpha(line.getAlpha()-30);
					}
		
						
				++pendulum.time;
				if((pendulum.a2_a>-5&&pendulum.a2_a<5)&&(pendulum.a1_a>5&&pendulum.a1_a<5)||seconds==255) {
					pendulum.reset();
					seconds =0;
				}
        }
    }, 0, 1000);
}
/**
 * @return the seconds
 */
public int getSeconds() {
	return seconds;
}
/**
 * @param seconds the seconds to set
 */
public void setSeconds(int seconds) {
	this.seconds = seconds;
}
}

