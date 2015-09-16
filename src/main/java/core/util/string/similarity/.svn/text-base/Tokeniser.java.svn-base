package core.util.string.similarity;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

/*
 Tokenisation
 */

final class Tokeniser {

	private static final Pattern PATTERN = Pattern.compile("[\\W&&[^-]]");

	private void normalizeCasing(final StringBuilder input) {
		// if it is formed by Pascal/Carmel casing
		for (int i = 0, size = input.length(); i < size; i++)
			if (Character.isWhitespace(input.charAt(i)))
				input.setCharAt(i, ' ');

		for (int i = 1; i < input.length() - 1; i++) {
			if (!Character.isUpperCase(input.charAt(i)))
				continue;
			if (Character.isWhitespace(input.charAt(i - 1)) || Character.isUpperCase(input.charAt(i - 1)))
				continue;
			if (Character.isUpperCase(input.charAt(i + 1)))
				continue;
			input.insert(i++, ' ');
		}
	}

	public String[] tokenise(String string) {
		StringBuilder stringBuilder = new StringBuilder(string);
		// Pattern r = Pattern.compile("([ \\t{}():;])");
		// Regex r=new Regex("([ \\t{}():;])");

		normalizeCasing(stringBuilder);

		// normalisation to the lower case
		string = PATTERN.matcher(stringBuilder.toString().toLowerCase()).replaceAll(" ");
		// string = stringBuilder.toString().toLowerCase().replaceAll("[\\W&&[^-]]", " ");

		StringTokenizer tokenizer = new StringTokenizer(string);
		// String[] tokens = r.split(string);

		final int tokenCount = tokenizer.countTokens();
		String tokens[] = new String[tokenCount];
		for (short i = 0; i < tokenCount; i++)
			tokens[i] = tokenizer.nextToken();
		return tokens;
		// List<String> filter = new ArrayList<String>(tokenizer.countTokens());
		// while (tokenizer.hasMoreTokens())
		// filter.add(tokenizer.nextToken());
		// // Matcher matcher = r.matcher(tokens[i]);
		// // System.out.println(matcher.groupCount());
		// // MatchCollection mc=r.Matches(tokens[i]);
		// // matcher.groupCount() == 0 &&
		// // if (token.trim().length() > 0)
		// // filter.add(tokenizer.nextToken());
		// // tokens = new String[filter.size()];
		// // for (int i = 0; i < tokens.length; i++)
		// // tokens[i] = (String) filter.get(i);
		//
		// return filter.toArray(new String[filter.size()]);
	}
}
