package core.util.string.similarity;

/*
 Matching two strings
 */

final class Leven implements Similarity {

	private static int computeDistance(String s, String t) {
		int n = s.length();
		int m = t.length();
		int[][] distance = new int[n + 1][m + 1]; // matrix
		int cost = 0;

		if (n == 0)
			return m;
		if (m == 0)
			return n;
		// init1
		for (int i = 0; i <= n; distance[i][0] = i++);
		for (int j = 0; j <= m; distance[0][j] = j++);

		// find min distance
		for (int i = 1; i <= n; i++)
			for (int j = 1; j <= m; j++) {
				cost = t.charAt(j - 1) == s.charAt(i - 1) ? 0 : 1;
				distance[i][j] = min3(distance[i - 1][j] + 1, distance[i][j - 1] + 1, distance[i - 1][j - 1] + cost);
			}

		return distance[n][m];
	}

	private static int min3(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	public int getSimilarity(String string1, String string2) {

		int dis = computeDistance(string1, string2);
		int maxLen = Math.max(string1.length(), string2.length());

		if (maxLen == 0)
			return 1;
		return maxLen - dis;
		// return (maxLen - dis) / (float) maxLen;
		// return (float) Math.Round(1.0F - dis/maxLen, 1) * 10 ;
	}
}
