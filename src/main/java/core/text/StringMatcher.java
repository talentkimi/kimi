package core.text;

import core.util.UtilList;

/**
 * A String Matcher.
 */
public class StringMatcher {

	/** Both must exactly match this. */
	public static final int EQUALS = 1;
	/** Both must start with this match. */
	public static final int STARTS_WITH = 2;
	/** Both must end with this match. */
	public static final int ENDS_WITH = 4;
	/** Both must contain this match. */
	public static final int CONTAINS = 8;
	/** Either string can equal this match. */
	public static final int EITHER = 16;

	/** The levels. */
	private int levels = 1;
	/** The match list. */
	private final UtilList matchList = new UtilList();

	/**
	 * Returns the levels.
	 */
	public int levels() {
		return levels;
	}

	/**
	 * Adds a new match.
	 * @param options the options.
	 * @param match the match.
	 * @return the match.
	 */
	public Match newMatch(int options, String match) {
		int level = this.levels - 1;
		if (level == 0) {
			level = 1;
		}
		return newMatch(level, options, match);
	}

	/**
	 * Adds a new match.
	 * @param level the level.
	 * @param options the options.
	 * @param match the match.
	 * @return the match.
	 */
	public Match newMatch(int level, int options, String text) {
		if (text == null) {
			throw new NullPointerException();
		}
		if (level < 1 || level > levels()) {
			throw new IllegalArgumentException("level=" + level);
		}
		if (this.levels == level) {
			this.levels++;
		}
		Match match = new Match(level, options, text);
		matchList.add(match);
		return match;
	}

	/**
	 * Returns true if the given text matches.
	 * @param text1 the first text.
	 * @param text2 the second text.
	 * @param level the matching level (-1 to try all levels).
	 * @return true if the given text matches.
	 */
	public final boolean isMatch(String text1, String text2) {
		return isMatch(text1, text2, -1);
	}

	/**
	 * Returns true if the given text matches.
	 * @param text1 the first text.
	 * @param text2 the second text.
	 * @param level the matching level (-1 to try all levels).
	 * @return true if the given text matches.
	 */
	public final boolean isMatch(String text1, String text2, int level) {
		if (text1 == null || text2 == null) {
			throw new NullPointerException();
		}
		if (level == -1) {
			// if (log.isDebugEnabled()) log.debug ("attempting simple match");
			for (int i = 0; i < levels(); i++) {
				if (isMatch(text1, text2, i)) {
					return true;
				}
			}
			return false;
		}
		if (level < 0 || level >= levels()) {
			throw new IllegalArgumentException("level=" + level);
		}
		text1 = Text.strip(text1);
		text2 = Text.strip(text2);

		// Check length
		if (text1.length() == 0 || text2.length() == 0) {
			// if (log.isDebugEnabled()) log.debug ("empty match failure");
			return false;
		}

		// Exact match!
		if (text1.equals(text2)) {
			// if (log.isDebugEnabled()) log.debug ("exact match");
			return true;
		}

		// Options
		if (level > 0) {
			for (int i = 0; i < matchList.size(); i++) {
				boolean text1Matched = false;
				boolean text2Matched = false;
				Match match = (Match) matchList.get(i);
				while (match != null) {
					// Match?
					if (!text1Matched && match.bothMatch(text1, level)) {
						text1Matched = true;
					}
					if (!text2Matched && match.bothMatch(text2, level)) {
						text2Matched = true;
					}
					if (text1Matched && text2Matched) {
						// if (log.isDebugEnabled()) log.debug ("both match success (" + text1 + "/" + text2 + "): " + match);
						return true;
					}
					if (match.eitherMatch(text1, level)) {
						// if (log.isDebugEnabled()) log.debug ("either match success (" + text1 + "): " + match);
						return true;
					}
					if (match.eitherMatch(text2, level)) {
						// if (log.isDebugEnabled()) log.debug ("either match success (" + text2 + "): " + match);
						return true;
					}
					match = match.nextMatch;
				}
			}
		}
		return false;
	}

	/**
	 * A Match.
	 */
	public static final class Match {

		/** The level. */
		private final int level;
		/** The match. */
		private final String match;
		/** The options. */
		private final int options;
		/** The next match. */
		private Match nextMatch = null;

		public String toString() {
			return level + "/" + match + "/" + options + ":: " + nextMatch;
		}

		/**
		 * Returns true if this text matches the given string.
		 * @param text the text.
		 * @return true if this text matches the given string.
		 */
		public boolean bothMatch(String text, int level) {
			if (this.level == level) {
				if ((options & CONTAINS) > 0) {
					return text.indexOf(match) != -1;
				}
				if ((options & STARTS_WITH) > 0) {
					if (text.startsWith(match)) {
						return true;
					}
				}
				if ((options & ENDS_WITH) > 0) {
					if (text.endsWith(match)) {
						return true;
					}
				}
				if ((options & EQUALS) > 0) {
					return text.equals(match);
				}
			}
			return false;
		}

		/**
		 * Returns true if this text matches the given string.
		 * @param text the text.
		 * @return true if this text matches the given string.
		 */
		public boolean eitherMatch(String text, int level) {
			if (this.level == level) {
				if ((options & EITHER) > 0) {
					// System.out.println("checking either");
					return text.equals(match);
				}
			}
			return false;
		}

		/**
		 * Creates a new Match.
		 * @parm level the level.
		 * @param options the matching options.
		 * @param match the string to match.
		 */
		private Match(int level, int options, String match) {
			this.level = level;
			this.match = Text.strip(match);
			this.options = options;
		}

		/**
		 * Add the given match to this.
		 * @param toMatch the string to match.
		 * @return the match.
		 */
		public Match add(String toMatch) {
			return add(options, toMatch);
		}

		/**
		 * Add the given match to this.
		 * @param toMatch the string to match.
		 * @param options the matching options.
		 * @return the match.
		 */
		public Match add(int options, String toMatch) {
			if (toMatch == null) {
				throw new NullPointerException();
			}
			if (options < 0) {
				throw new IllegalArgumentException("options=" + options);
			}
			if (nextMatch != null) {
				throw new IllegalStateException("match already added to this one");
			}
			Match match = new Match(level, options, toMatch);
			nextMatch = match;
			return match;
		}
	}
}
