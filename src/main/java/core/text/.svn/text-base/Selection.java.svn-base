package core.text;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.util.UtilList;

/**
 * A selector for selection and extraction of text.
 */
public class Selection implements SelectionOptions {

	/** The list of selected buffers. */
	private final UtilList bufferList = new UtilList();
	
	/**
	 * Selects the given text.
	 * @param text the text to select.
	 */
	public final void selectText(String text) {
		if (text == null) {
			throw new NullPointerException();
		}
		bufferList.add(0, text);
	}

	/**
	 * Deselects the currently selected text.
	 */
	public final String deselectText() {
		return (String) bufferList.remove(0);
	}

	/**
	 * Returns the currently selected text.
	 */
	public final String getText() {
		return (String) bufferList.get(0);
	}

	/**
	 * Returns true if the given options contain optional!
	 * @param options the options.
	 * @return true if the given options contain optional!
	 */
	public final boolean isOptional(int options) {
		return (options & OPTIONAL) > 0;
	}

	/**
	 * Returns true if the given options contain ignore case!
	 * @param options the options.
	 * @return true if the given options contain ignore case!
	 */
	public final boolean ignoreCase(int options) {
		return (options & IGNORE_CASE) > 0;
	}

	/**
	 * Returns true if the given options contain no move.
	 * @param options the options.
	 * @return true if the given options contain no move.
	 */
	public final boolean noMove(int options) {
		return (options & NO_MOVE) > 0;
	}

	/**
	 * Returns true if the given options contain include delimiters.
	 * @param options the options.
	 * @return true if the given options contain include delimiters.
	 */
	public final boolean includeDelimiters(int options) {
		return (options & INCLUDE_DELIMITERS) > 0;
	}

	/**
	 * Extracts and returns the first string beginning and ending with the given delimiters.
	 * @param begin the beginning text.
	 * @param end the end text.
	 * @param options the options.
	 * @return the extracted text.
	 */
	public final String extractText(String begin, String end, int options) throws SelectionException {
		if (bufferList.size() == 0) {
			if (isOptional(options))
				return null;
			throw new SelectionException("nothing selected to extract from");
		}
		String selection = getText();
		int beginIndex = 0;
		int beginLength = 0;
		if (begin != null) {
			if (ignoreCase(options)) {
				beginIndex = Text.indexOfIgnoreCase(selection, begin, 0);
			} else {
				beginIndex = selection.indexOf(begin);
			}
			if (beginIndex == -1) {
				if (isOptional(options))
					return null;
				throw new SelectionException("begin text not found: \"" + begin + "\"");
			}
			beginLength = begin.length();
		}
		int endIndex = selection.length();
		int endLength = 0;
		if (end != null) {
			if (ignoreCase(options)) {
				endIndex = Text.indexOfIgnoreCase(selection, end, beginIndex + beginLength);
			} else {
				endIndex = selection.indexOf(end, beginIndex + beginLength);
			}
			endLength = end.length();
			if (endIndex == -1) {
				if (isOptional(options))
					return null;
				throw new SelectionException("end text not found: \"" + end + "\"");
			}
			endLength = end.length();
		}
		String extraction;
		if (includeDelimiters(options)) {
			extraction = selection.substring(beginIndex, endIndex + endLength);
		} else {
			extraction = selection.substring(beginIndex + beginLength, endIndex);
		}
		String newSelection;
		if (noMove(options)) {
			newSelection = selection;
		} else {
			newSelection = selection.substring(endIndex + endLength, selection.length());
		}
		deselectText();
		selectText(newSelection);
		return extraction;
	}

	/**
	 * Selects the given text beginning and ending with the given delimiters.
	 * @param begin the beginning text.
	 * @param end the end text.
	 * @param options the options.
	 */
	public final boolean selectText(String begin, String end, int options) throws SelectionException {
		String extraction = extractText(begin, end, options);
		if (extraction == null) {
			return false;
		}
		selectText(extraction);
		return true;
	}

	/**
	 * Clear the selection.
	 */
	public void clear() {
		bufferList.clear();
	}
	
	/**
	 * Utility method used to search the current selection for a given regex and return the first matching instance.
	 * Note: For performance reasons it is always better to use a pre-compiled regex for anything other than adhoc requests.
	 * @param regex The <code>String</code> regex to be used to perform the search.
	 * @return A <code>String</code> that matches the regex or <code>null</code> if a match is not found.
	 */
	public String matchFirst(String regex)
	{
		return matchFirst(Pattern.compile(regex, Pattern.MULTILINE));

	}
	
	/**
	 * Utility method used to search the current selection for a given regex and return all matching cases.
	 * Note: For performance reasons it is always better to use a pre-compiled regex for anything other than adhoc requests.
	 * @param regex The <code>String</code> regex to be used to perform the search.
	 * @return A <code>Vector</code> containing all matching cases as <code>String</code>s.
	 */
	public Vector<String> match(String regex)
	{
		return match(Pattern.compile(regex, Pattern.MULTILINE));
	}

	/**
	 * Utility method used to search the current selection for a given regex and return the first matching instance.
	 * @param regex The <code>Pattern</code> regex to be used to perform the search.
	 * @return A <code>String</code> that matches the regex or <code>null</code> if a match is not found.
	 * Note: If there are any concerns about performance the results of the replaceAll can be cached.
	 */
	public String matchFirst(Pattern regex)
	{
		return matchFirst(regex, getText().replaceAll("\\s+", " "));
	}
	
	/**
	 * Utility method used to search the current selection for a given regex and return all matching cases.
	 * @param regex The <code>Pattern</code> regex to be used to perform the search.
	 * @return A <code>Vector</code> containing all matching cases as <code>String</code>s.
	 * Note: If there are any concerns about performance the results of the replaceAll can be cached.
	 */
	public Vector<String> match(Pattern regex)
	{
		return match(regex, getText().replaceAll("\\s+", " "));
	}
	/**
	 * Utility method used to search an input string for a given regex and return the first matching instance.
	 * Note: For performance reasons it is always better to use a pre-compiled regex for anything other than adhoc requests.
	 * @param regex The <code>String</code> regex to be used to perform the search.
	 * @param input The input <code>String</code> to be searched.
	 * @return A <code>String</code> that matches the regex or <code>null</code> if a match is not found.
	 */
	public String matchFirst(String regex, String input)
	{
		Pattern pat = Pattern.compile(regex, Pattern.MULTILINE);
		return matchFirst(pat, input);

	}
	
	/**
	 * A utility method used to search an input string for a given regex and return all matching cases.
	 * Note: For performance reasons it is always better to use a pre-compiled regex for anything other than adhoc requests.
	 * @param regex The <code>Pattern</code> regex to be used to perform the search.
	 * @param input The input <code>String</code> to be searched.
	 * @return A <code>Vector</code> containing all matching cases as <code>String</code>s.
	 */
	public Vector<String> match(String regex, String input)
	{
		Pattern pat = Pattern.compile(regex, Pattern.MULTILINE);
		return match(pat, input);
	}
	
	/**
	 * Search an input string for a given regex and return the first matching instance.
	 * @param regex The <code>Pattern</code> regex to be used to perform the search.
	 * @param input The input <code>String</code> to be searched.
	 * @return A <code>String</code> that matches the regex or <code>null</code> if a match is not found.
	 */
	public String matchFirst(Pattern regex, String input)
	{
		String match = null;
		
		Matcher mat = regex.matcher(input);
		
		if (mat.find())
		{
			if (mat.groupCount() > 0)
			{
				match = mat.group(1);
			}
			else
			{
				match = mat.group();
		    }
		}
		
		return match;
	}
	
	/**
	 * Search an input string for a given regex and return all matching cases.
	 * @param regex The <code>Pattern</code> regex to be used to perform the search.
	 * @param input The input <code>String</code> to be searched.
	 * @return A <code>Vector</code> containing all matching cases as <code>String</code>s.
	 */
	public Vector<String> match(Pattern regex, String input)
	{
		Vector<String> resultV = new Vector<String>();
		
		Matcher mat = regex.matcher(input);
		
		while (mat.find())
		{
			if (mat.groupCount() > 0)
			{
				for (int i = 1; i <= mat.groupCount(); i++)
				{
				    resultV.add(mat.group(i));	
				}				
			}
			else
			{
				resultV.add(mat.group());
			}
			
		}
		
		return resultV;
	}
	
	/**
	 * Utility method to replace a matching regex with a replacement <code>String</code>.
	 * @param regex The <code>Pattern</code> regex to be used to perform the search.
	 * @param input The input <code>String</code> to be searched.
	 * @param replacement The <code>String</code> that will replace the matching cases.
	 * @param global A <code>boolean</code> flag used to indicate whether a global replace is being performed, 
	 * otherwise only the first matching case will be replaced. 
	 * @return The resulting string after the matching patterns have been replaced.
	 */
	public String replace(Pattern regex, String input, String replacement, boolean global)
	{
		Matcher mat = regex.matcher(input);
		
		if (mat.matches())
		{
			input = global ? mat.replaceAll(replacement) : mat.replaceFirst(replacement);
		}
		
		return input;
	}
}