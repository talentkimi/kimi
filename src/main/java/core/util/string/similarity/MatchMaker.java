package core.util.string.similarity;

import java.math.BigInteger;
import java.util.HashMap;

/*
 Matching two strings
 */

public final class MatchMaker {

	// private final static String BEFORE_CONVERSION = "�����������������������������������ǒ�";
	// private final static String AFTER_CONVERSION = "aAaAaAaAeEeEeEeEiIiIiIoOoOoOuUuUuUcC'n";

	private static final HashMap<Character, String> ALPHABET_MAPPING;

	private static final Similarity similarity = new Leven();

	static {
		ALPHABET_MAPPING = new HashMap<Character, String>(46);
//		ALPHABET_MAPPING.put('�', "a");
//		ALPHABET_MAPPING.put('�', "A");
//		ALPHABET_MAPPING.put('�', "a");
//		ALPHABET_MAPPING.put('�', "A");
//		ALPHABET_MAPPING.put('�', "a");
//		ALPHABET_MAPPING.put('�', "A");
//		ALPHABET_MAPPING.put('�', "a");
//		ALPHABET_MAPPING.put('�', "A");
//		ALPHABET_MAPPING.put('�', "e");
//		ALPHABET_MAPPING.put('�', "E");
//		ALPHABET_MAPPING.put('�', "e");
//		ALPHABET_MAPPING.put('�', "E");
//		ALPHABET_MAPPING.put('�', "e");
//		ALPHABET_MAPPING.put('�', "E");
//		ALPHABET_MAPPING.put('�', "e");
//		ALPHABET_MAPPING.put('�', "E");
//		ALPHABET_MAPPING.put('�', "i");
//		ALPHABET_MAPPING.put('�', "I");
//		ALPHABET_MAPPING.put('�', "i");
//		ALPHABET_MAPPING.put('�', "I");
//		ALPHABET_MAPPING.put('�', "i");
//		ALPHABET_MAPPING.put('�', "I");
//		ALPHABET_MAPPING.put('�', "o");
//		ALPHABET_MAPPING.put('�', "O");
//		ALPHABET_MAPPING.put('�', "o");
//		ALPHABET_MAPPING.put('�', "O");
//		ALPHABET_MAPPING.put('�', "o");
//		ALPHABET_MAPPING.put('�', "O");
//		ALPHABET_MAPPING.put('�', "u");
//		ALPHABET_MAPPING.put('�', "U");
//		ALPHABET_MAPPING.put('�', "u");
//		ALPHABET_MAPPING.put('�', "U");
//		ALPHABET_MAPPING.put('�', "u");
//		ALPHABET_MAPPING.put('�', "U");
//		ALPHABET_MAPPING.put('�', "c");
//		ALPHABET_MAPPING.put('�', "C");
//		ALPHABET_MAPPING.put('�', "'");
//		ALPHABET_MAPPING.put('�', "n");
//
//		ALPHABET_MAPPING.put('�', "Oe");
//		ALPHABET_MAPPING.put('�', "oe");
//		ALPHABET_MAPPING.put('�', "Oe");
//		ALPHABET_MAPPING.put('�', "oe");
//		ALPHABET_MAPPING.put('�', "Ae");
//		ALPHABET_MAPPING.put('�', "ae");
//		ALPHABET_MAPPING.put('�', "Aa");
//		ALPHABET_MAPPING.put('�', "aa");
	}

	public static void main(String[] a) {
		System.out.println(MatchMaker.removeAccents("� �"));
	}

	public static String removeAccents(String string) {
		StringBuilder stringBuilder = new StringBuilder(string);

		for (int i = 0; i < stringBuilder.length(); i++) {
			String replacement = ALPHABET_MAPPING.get(stringBuilder.charAt(i));
			if (replacement == null)
				continue;
			stringBuilder.replace(i, i + 1, replacement);
			i += replacement.length() - 1;
		}

		// for (int i = 0; i < BEFORE_CONVERSION.length(); i++) {
		// char beforeChar = BEFORE_CONVERSION.charAt(i);
		// char afterChar = AFTER_CONVERSION.charAt(i);
		//
		// string = string.replace(beforeChar, afterChar);
		// }
		//
		// string = string.replace("�", "Oe");
		// string = string.replace("�", "oe");
		// string = string.replace("�", "Oe");
		// string = string.replace("�", "oe");
		// string = string.replace("�", "Ae");
		// string = string.replace("�", "ae");
		// string = string.replace("�", "Aa");
		// string = string.replace("�", "aa");

		return stringBuilder.toString();
	}

	private int[][] cost;

	private int lcm = 1;

	private int leftLen;
	private String leftlString;
	private String[] leftTokens;
	private int rightLen;

	private String rightString;
	private String[] rightTokens;

	public MatchMaker(final String left, final String right) {
		this(left, right, false);
	}

	public MatchMaker(final String left, final String right, final boolean accentSensitive) {
		if (accentSensitive) {
			leftlString = left;
			rightString = right;
		}

		else {
			leftlString = removeAccents(left);
			rightString = removeAccents(right);
		}

		// similarity = new Leven();
		// getSimilarity=new Similarity(editdistance.GetSimilarity) ;

		// ISimilarity lexical=new LexicalSimilarity() ;
		// getSimilarity=new Similarity(lexical.GetSimilarity) ;

		initialise();

	}

	private void calculateCost() {
		int newLeftLen = leftLen + 1, newRightLen = rightLen + 1;
		cost = new int[newLeftLen][newRightLen];
		int lengths[][] = new int[newLeftLen][newRightLen];
		for (int i = 0; i < newLeftLen; i++)
			for (int j = 0; j < newRightLen; j++) {
				String left = leftTokens[i], right = rightTokens[j];

				// cost[i][j]=getSimilarity(_leftTokens[i], _rightTokens[j]);
				cost[i][j] = similarity.getSimilarity(left, right);
				int maxLength = Math.max(left.length(), right.length());
				if (maxLength == 0) {
					lengths[i][j] = 1;
					continue;
				}
				lengths[i][j] = maxLength;
				if (lcm % maxLength == 0)
					continue;
				BigInteger currentFactor = BigInteger.valueOf(lcm);
				int gcd = currentFactor.gcd(BigInteger.valueOf(maxLength)).intValue();
				lcm *= maxLength / gcd;
			}
		for (int i = 0; i < newLeftLen; i++)
			for (int j = 0; j < newRightLen; j++)
				cost[i][j] *= lcm / lengths[i][j];
	}

	public float getScore() {
		BipartiteMatcher match = new BipartiteMatcher(leftTokens, rightTokens, cost);
		return match.getScore(false) / lcm;
	}

	public int getTokenCount() {
		return (leftLen < rightLen ? leftLen : rightLen) + 1;
	}

	private void initialise() {
		tokenise();
		calculateCost();
	}

	private void swap() {
		String[] tmp = leftTokens;
		leftTokens = rightTokens;
		rightTokens = tmp;
		String s = leftlString;
		leftlString = rightString;
		rightString = s;
	}

	private void tokenise() {
		Tokeniser tokeniser = new Tokeniser();
		leftTokens = tokeniser.tokenise(leftlString);
		rightTokens = tokeniser.tokenise(rightString);

		if (leftTokens.length > rightTokens.length)
			swap();

		leftLen = leftTokens.length - 1;
		rightLen = rightTokens.length - 1;
	}
}
