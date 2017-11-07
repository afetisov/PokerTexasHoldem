package client.game.misc;


import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;

import commons.log.Logger;
import commons.log.LoggingLevel;

public class ResourceManager implements GameResourcesConstants {
	
	private static final int IMAGE_MAX_SIZE = 2000000;
	
	private static final String IMAGES_PATH = "resources/images/";
	
	private static final String SOUNDS_PATH = "resources/sounds/";
	
	private static final Map<String,ImageIcon> images = new ConcurrentHashMap<String,ImageIcon>();
	
	private static final Map<String,File> sounds = new ConcurrentHashMap<String, File>();
	
	private static String [] imageNames = {
		BACKGROUND,
		ACTION_BUTTONS,
		CARDS_BIG, 
		CARDS_SMALL, 
		CHECKBOX, 
		DEALER_BUTTON, 
		BET_AMOUNT_FIELD, 
		PLAYER_INFO_BOX, 
		SLIDER_BACKGROUND,
		SLIDER_FILLER, 
		SLIDER_THUMB, 
		TIMER_CAP,
		TIMER_CIRCLE_COLOR,
		TIMER_CIRCLE_GRAY,
		TIMER_CIRCLE_LEFT,
		TIMER_CIRCLE_RIGHT,
		CHATTING_BOX,
		MICROPHONE,
		MENU_ARROW,
		MENU_BACKGROUND,
		MENU_BUTTONS
	};
	
	private ResourceManager() {}

	
	public static void addImage(String tag, ImageIcon image) {
		images.put(tag, image);
	}
	
	public static ImageIcon getImage(String tag) {
		return images.get(tag);
	}
	
	public static void addSoundFile(String tag, File file) {
		sounds.put(tag, file);
	}

	public static File getSoundFile(String tag) {
		return sounds.get(tag);
	}
	
	public static void loadResources() {
		
		for(String imgName : imageNames) {
		
			ImageIcon img = loadImage(IMAGES_PATH + imgName + ".png");
			
			images.put(imgName, img);
		}
	}
	
	private static ImageIcon loadImage(String imgName) {
		
		int length = 0;
		
		InputStream is = ResourceManager.class.getClassLoader().getResourceAsStream(imgName);
		
		BufferedInputStream inStream = new BufferedInputStream(is);
		
		byte buf[] = new byte [IMAGE_MAX_SIZE];
		
		try {
			
			length = inStream.read(buf);
			
			inStream.close();
		
		} catch (Exception exc) {
			
			Logger.write(LoggingLevel.ERROR, "Failed to load image: " + imgName, exc);
			
			return null;
		}
		
		if (length <= 0) {
			return null;
		}
		
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf));
	}
}
