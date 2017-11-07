package client.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeWrapper implements Comparable<DateTimeWrapper>{

	private Date date;
	
	private DateFormat format = new SimpleDateFormat("MM/dd/yyyy at hh:mm", new Locale("US"));

	public DateTimeWrapper(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		
		if(date == null) return "";
		
		return format.format(date);
	}

	@Override
	public int compareTo(DateTimeWrapper o) {
		
		if(date == null || o.date == null) return 0;
		
		return date.compareTo(o.date);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
