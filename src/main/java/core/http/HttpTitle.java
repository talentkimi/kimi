package core.http;

/**
 * An HTTP Message Title.
 */
public abstract class HttpTitle {

	/** The title. **/
	private Object title = null;

	/**
	 * Returns the title.
	 */
	public Object get() {
		return title;
	}
	/**
	 * Returns a string representation of this title.
	 */
	public String toString() {
		return (title != null) ? title.toString() : null;
	}
	/**
	 * Sets the title.
	 * @param title the title.
	 */
	protected final void setTitle( Object title ) {
		if( title == null ) throw new NullPointerException();
		Object[] supported = getSupported();
		if( supported != null ) {
			boolean isSupported = false;
			for( int i=0; i<supported.length; i++ ) {
				if( supported[i].equals( title ) ) {
					isSupported = true;
					break;
				}
			}
			if( !isSupported ) throw new IllegalArgumentException( title.toString() );
		}
		this.title = title;
	}

	/**
	 * Returns true if this title is equal to the given object.
	 * @param obj the object to compare.
	 */
	public boolean equals( Object obj ) {
		if( obj instanceof HttpTitle ) {
			HttpTitle ht = (HttpTitle) obj;
			return this.title.equals( ht.title );
		}
		return title.equals( obj );
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/**
	 * Sets the title.
	 * @param title the title.
	 */
	public abstract void set( String title );

	/**
	 * Returns an array of supported values.
	 * @return an array of supported values.
	 */
	public abstract Object[] getSupported();

}
