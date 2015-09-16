package core.http.request;

import core.http.HttpQueryContainer;
import core.http.HttpTitle;
import core.net.NetUrl;

/**
 * An HTTP URL.
 */
public class HttpUrl extends HttpTitle implements HttpQueryContainer {

	/**
	 * Sets the title.
	 * @param title the title.
	 */
	public void set( String title ) {
		if( title == null ) throw new NullPointerException();
		NetUrl url = null;
		try {
			url = new NetUrl( title );
		} catch( RuntimeException re ) {
			throw new IllegalArgumentException( "Illegal HTTP URI: \""+title+"\"" );
		}
		setTitle( url );
	}

	/**
	 * Returns an array of supported values.
	 * @return an array of supported values.
	 */
	public final Object[] getSupported() {
		return null;
	}

	/**
	 * Returns the URL.
	 * @return the URL.
	 */
	public NetUrl getNetUrl() {
		return (NetUrl) get();
	}

	/**
	 * Set the query in the container.
	 * @param query the query.
	 */
	public void setQuery( String query ) {
		getNetUrl().setQuery( query );
	}
	/**
	 * Returns the query in the container.
	 * @return the query in the container.
	 */
	public String getQuery() {
		return getNetUrl().getQuery();
	}

}
