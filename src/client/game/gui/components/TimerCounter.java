package client.game.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import client.game.misc.ResourceManager;

public class TimerCounter extends JPanel {

	private ImageIcon timerCircleColorImg;
	
	private ImageIcon timerCircleGrayImg;
	
	private ImageIcon timerCapImg;
	
	private Point capPosition;

	private int secondsNumber = 10;
	
	private static final int FPS = 15;
	
	private static AtomicInteger currentSeconds;
	
	private Timer drawTimer;
	
	private Timer secondsTimer;
	
	private Font timerFont;
	
	private Arc2D.Double arc;

	public TimerCounter() {

		setOpaque(false);
		
		timerCircleColorImg = ResourceManager.getImage(ResourceManager.TIMER_CIRCLE_COLOR);
		
		timerCircleGrayImg = ResourceManager.getImage(ResourceManager.TIMER_CIRCLE_GRAY);
		
		timerCapImg = ResourceManager.getImage(ResourceManager.TIMER_CAP);
	
		arc = new Arc2D.Double(-3, -3, timerCircleColorImg.getIconWidth() + 6, timerCircleColorImg.getIconHeight() + 6, 90, 0, Arc2D.PIE);
		
		setPreferredSize(new Dimension(timerCircleColorImg.getIconWidth(), timerCircleColorImg.getIconHeight()));
		
		capPosition = new Point((timerCircleColorImg.getIconWidth() - timerCapImg.getIconWidth()) / 2, (timerCircleColorImg.getIconHeight() - timerCapImg.getIconHeight()) / 2);
	
		timerFont = new Font("Arial",Font.PLAIN, 20);
		
		currentSeconds = new AtomicInteger(secondsNumber);
	}

	public int getSecondsNumber() {
		return secondsNumber;
	}

	public void setSecondsNumber(int secondsNumber) {
		this.secondsNumber = secondsNumber;
		currentSeconds = new AtomicInteger(secondsNumber);
	}

	public Font getTimerFont() {
		return timerFont;
	}

	public void setTimerFont(Font timerFont) {
		this.timerFont = timerFont;
	}

	public void start() {
		
		currentSeconds.set(secondsNumber);
		
		drawTimer = new Timer();
		
		secondsTimer = new Timer();
		
		drawTimer.schedule(new DrawTimerTask(), 0, 1000 / FPS );
		
		secondsTimer.schedule(new CountdownTimerTask(), 1000, 1000);		
	}

	public void stop() {
		drawTimer.cancel();
		secondsTimer.cancel();
	}
	
	private void setAngle(double angle) {
		if(angle > 360) angle = 360;
		arc = new Arc2D.Double(-3, -3, timerCircleColorImg.getIconWidth() + 6, timerCircleColorImg.getIconHeight() + 6, 90, angle, Arc2D.PIE);
	}
	
	@Override
	protected void paintComponent(Graphics g) {

		g.drawImage(timerCircleGrayImg.getImage(), 0, 0, null);

		if(currentSeconds.get() > 0) {
			
			BufferedImage bi = new BufferedImage(timerCircleColorImg.getIconWidth(), timerCircleColorImg.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D bg = (Graphics2D)bi.getGraphics();

			bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			bg.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			
			bg.setClip(arc);
			
			bg.drawImage(timerCircleColorImg.getImage(), 0, 0, null);

			bi.flush();
			
			g.drawImage(bi, 0, 0, null);
		
		}
		
		g.drawImage(timerCapImg.getImage(), capPosition.x, capPosition.y, null);
		

		String seconds = currentSeconds.toString();
		
		g.setColor(Color.BLACK);
		
		g.setFont(timerFont);
		
		Graphics2D g2 = (Graphics2D)g;
		
		FontRenderContext frc = g2.getFontRenderContext();
		
		LineMetrics lm = timerFont.getLineMetrics(seconds, frc);
		
		float textWith = (float) timerFont.getStringBounds(seconds, frc).getWidth();
		
		g2.drawString(currentSeconds.toString(), (timerCircleColorImg.getIconWidth() - textWith) / 2, (timerCircleColorImg.getIconHeight() + lm.getHeight()) / 2 - lm.getDescent());
	}
	
	private class DrawTimerTask extends TimerTask {

		private int secondFraction;  
		
		@Override
		public void run() {
			
			if(currentSeconds.get() <= 0) {
				
				this.cancel();
				
				setAngle(-360);
				
				repaint();
				
				return;
			}
			
			double ang = ((double)360 / (secondsNumber * FPS)) * secondFraction++;

			setAngle(-ang);
			
			repaint();
		}
	}

	private class CountdownTimerTask extends TimerTask {

		@Override
		public void run() {
			
			if(currentSeconds.decrementAndGet() <= 0)
				this.cancel();
		}
	}
}
