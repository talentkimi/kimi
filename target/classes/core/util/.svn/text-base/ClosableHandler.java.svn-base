package core.util;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClosableHandler {
	private static final Logger log = LoggerFactory.getLogger(ClosableHandler.class);
	
	public static void closeSafely(Closeable closable){
		if(closable == null)
			return;
		
		try{
			closable.close();
		} catch(IOException e) {
			if(log.isErrorEnabled()){
				log.error("failed to close closable", e);
			}
		}
	}
}
