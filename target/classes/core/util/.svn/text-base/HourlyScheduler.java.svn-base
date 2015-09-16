package core.util;

import java.util.Calendar;
 
import core.util.time.Days;
import core.util.time.Hours;
import core.util.time.Milliseconds;
 

/**
 * An Hourly Scheduler
 *@author jemy@travelfusion.com
 * */
public class HourlyScheduler extends Scheduler {
	/**
	 * Returns the time of the last update.
	 * @param   millis 			the start hour to update in milliseconds
	 * @param   minuteOfHour  	the specific minute in an hour to update
	 * @return  time			return last time of the update in milliseconds
	 * */
	private static final long getTimeOfLastUpdate(Milliseconds millis,int minuteOfHour) {
		if (millis.getMillis() >= new Days(1).getMillis()) {
			throw new IllegalArgumentException("millis=" + millis);
		}
		if(minuteOfHour<0 || minuteOfHour>=60){
			throw new IllegalArgumentException("minuteOfHour="+minuteOfHour);
		}
		UtilDate date = new UtilDate();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		date.add(Calendar.MILLISECOND, (int) millis.getMillis());
		 
		int minute=date.getMinute();
		if(minute<=minuteOfHour){
			date.add(Calendar.MILLISECOND,-(int)(new Hours(1)).getMillis());
			date.setMinutes(minuteOfHour);
			date.setSeconds(0);
		}else{
			int diff=minute-minuteOfHour;
			date.add(Calendar.MINUTE,-diff); 
		}
		long time = date.getTimeInMillis();
		return time;
	}
	/**
	 * Creates a new hourly scheduler.
	 * Here we want a scheduler to handle a job every {interval} hours,at minute {minuteOfHour}.
	 * The param millis stands for the time we start the server.We can get this by this way.
	 * UtilDate date=new UtilDate();
	 * long millis = new Hours(date.getHour()).getMillis()
	 * 			+ new Minutes(date.getMinute()).getMillis()
	 *          + new Seconds(date.getSecond()).getMillis();
	 * @param r 			the runnable.
	 * @param millis 		the start hour for the update.
	 * @param interval  	the interval hours for the update
	 * @param minuteOfHour  the specific minute in an hour to update
	 */
	public HourlyScheduler(Runnable r, Milliseconds millis,int interval,int minuteOfHour) {
		super(r, new Hours(interval),getTimeOfLastUpdate(millis,minuteOfHour));
	}
}
