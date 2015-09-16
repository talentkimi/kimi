package core.text;

/**
 * The available selection options.
 */
public interface SelectionOptions {

	/* No options. */
	int NO_OPTIONS = 0;
	/* Optional selection. */
	int OPTIONAL = 1;
	/* No move after selection. */
	int NO_MOVE = 2;
	/* Include delimiters in selection. */
	int INCLUDE_DELIMITERS = 4;
	/* Ignore case in selection. */
	int IGNORE_CASE = 8;

}