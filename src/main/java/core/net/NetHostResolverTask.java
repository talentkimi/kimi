package core.net;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.lang.thread.ThreadManager;
import core.util.Task;

public class NetHostResolverTask extends Task {

	private static final Logger log = LoggerFactory.getLogger(NetHostResolverTask.class);


	private static final String[] HOSTS = {"www.vasp.com.br", "www.flysnowflake.com", "www.airtran.com", "www.vueling.com", "67.19.20.146", "66.139.75.199", "www.airbaltic.com", "www4.flybe.com", "www.flynordic.com", "www10.germanwings.com", "c3dsp.westjet.com", "www1.sunexpress.de", "book.flydba.com", "www.virginblue.com.au", "book.thomsonfly.com", "69.44.59.77", "www28.germanwings.com", "booking.transavia.com", "www.maersk-air.com", "www.easyjet.com", "www.opodo.co.uk", "www.spiritair.com",
			"www.flyzoom.com", "epass.malmoaviation.se", "tickets.vueling.com", "www.delta.com", "194.128.159.142", "ssl.futuradirect.com", "www.meridiana.it", "uk.airkiosk.com", "ibp2.scandinavian.net", "www.orbitz.com", "bookings.flyme.com", "sutra112.airkiosk.com", "www27.germanwings.com", "www.jetsgo.com", "www.flybe.com", "www.itn.net", "www.freedomair.co.nz", "www.aerarann.com", "www.firstchoice.co.uk", "www.gotlandsflyg.se", "www.thomsonflightsearch.com", "booking.canjet.com",
			"www.hellas-jet.com", "www.booksecure.net", "www.smartwings.net", "www.helvetic.com", "www.airluxor.com", "www.regionalexpress.com.au", "www.bookryanair.com", "web.numera.it", "www.easyJet.com", "www.flybmi.com", "www.lastminute.com", "www.flyzb.com", "www.skyeurope.com", "www.frontierairlines.com", "67.19.20.154", "www.norwegian.no", "www.mytravellite.com", "83.142.24.194", "www.flyfrontier.com", "www1.flybe.com", "booking.sterlingticket.com", "www.bmibaby.com",
			"www.voegol.com.br", "secure.xl.com", "www.airwales.com", "www.transavia.com", "www16.germanwings.com", "c1dsp.westjet.com", "www.avro.co.uk", "www.ata.com", "www.americawest.com", "www.midwestairlines.com", "www.flysaa.com", "www.flyi.com", "www.thomsonfly.com", "www.malmoaviation.se", "www.flyaerlingus.com", "ebooking.sig2net.com", "www.flyted.com", "www.jetblue.com", "tickets.airtran.com", "ip.norwegian.no", "booking.icelandexpress.com", "www.tradesecure.co.uk",
			"book.destina.ca", "www.alaskaair.com", "www.travelport.net", "partner.airberlin.com", "www.destina.ca", "www.aircanada.com", "www.hlx.com", "www.thomsonflights.com", "www.freedomair.com", "www.virgin-express.com", "www.airberlin.com", "www.westjet.com", "www.scandjet.se", "wizzair.com", "www.flyglobespan.com", "www.air-scotland.com", "compre.voegol.com.br", "www.helios-airways.com", "www31.germanwings.com", "www.snalskjutsen.com", "www.dat.dk", "www.swedline.com",
			"www.cheaptickets.com", "book.hlx.com", "www29.germanwings.com", "www.airasia.com", "www.jet2.com", "jetstar.com", "bookings.virginblue.com.au", "www30.germanwings.com", "www.xl.com", "flysmart.canjet.com", "www.jetstar.com", "www.jetblueairways.com", "sutra125.airkiosk.com", "www.gexx.de", "www2.flybe.com", "www.virgin-atlantic.com", "www.spanair.com", "sabresonicweb.com"};

	private final NetHostResolver resolver;
	private final String host;

	public NetHostResolverTask(NetHostResolver resolver, String host) {
		if (resolver == null || host == null) {
			throw new NullPointerException();
		}
		this.host = host;
		this.resolver = resolver;
		this.start();
	}

	public void runTask() throws Throwable {
		resolver.resolve(host);
	}

	public static void main(String[] args) {
		resolveAll();
	}

	public static void resolveAll() {
		NetHostResolver resolver = new NetHostResolver();
		NetHostResolverTask[] tasks = new NetHostResolverTask[HOSTS.length];
		for (int i = 0; i < HOSTS.length; i++) {
			tasks[i] = new NetHostResolverTask(resolver, HOSTS[i]);
		}
		boolean finished = false;
		while (true) {
			finished = true;
			for (int i = 0; i < HOSTS.length; i++) {
				if (!tasks[i].hasFinished()) {
					finished = false;
					break;
				}
			}
			if (finished) {
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				if (log.isErrorEnabled()) log.error(e.getMessage(),e);
			}
		}
		for (int i = 0; i < tasks.length; i++) {
			if (tasks[i].hasCrashed()) {
				if (log.isDebugEnabled()) log.debug (tasks[i].host, tasks[i].getThrowable());
			}
		}
	}
}
