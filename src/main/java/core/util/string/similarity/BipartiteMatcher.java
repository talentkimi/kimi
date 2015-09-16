package core.util.string.similarity;

/*
 Maximise the total weight of bipartite graph 
 */

final class BipartiteMatcher {

	private final int[][] cost;

	private final int[] leftLabel;
	private final int[] rightLabel;

	private final int leftLen;
	private final int rightLen;

	private final String[] leftTokens;
	private final String[] rightTokens;

	private final boolean[] leftVisited;
	private final boolean[] rightVisited;

	private final int[] previous;// connection between left and right
	private final int[] incomming;
	private final int[] outgoing;

	private boolean stop;

	public BipartiteMatcher(String[] left, String[] right, int[][] cost) {
		if (left == null || right == null || cost == null)
			throw new NullPointerException();

		// swap
		int leftArrayLength = left.length, rightArrayLength = right.length;
		if (leftArrayLength > rightArrayLength) {
			leftTokens = right;
			rightTokens = left;
			this.cost = new int[rightArrayLength][leftArrayLength];
			swap(cost);
		} else {
			leftTokens = left;
			rightTokens = right;
			this.cost = cost;
		}
		// _cost = new float[cost.length][];
		// System.arraycopy(cost, 0, _cost, 0, cost.length);

		int tempLeftLen = leftTokens.length, tempRightLen = rightTokens.length;
		leftLen = tempLeftLen - 1;
		rightLen = tempRightLen - 1;

		leftLabel = new int[tempLeftLen];
		rightLabel = new int[tempRightLen];
		leftVisited = new boolean[tempLeftLen];
		rightVisited = new boolean[tempRightLen];
		previous = new int[tempLeftLen + tempRightLen];
		outgoing = new int[tempLeftLen];
		incomming = new int[tempRightLen];
	}

	private void computeLabel() {
		// init distance
		for (int i = 0; i <= leftLen; i++) {
			int maxLeft = Integer.MIN_VALUE;
			for (int j = 0; j <= rightLen; j++)
				if (cost[i][j] > maxLeft)
					maxLeft = cost[i][j];

			leftLabel[i] = maxLeft;
		}

	}

	private void findPath(int source) {
		flush();
		traverse(source);
		// return stop;

	}

	private void flush() {
		stop = false;
		for (int i = 0, length = previous.length; i < length; i++)
			previous[i] = -1;
		for (int i = 0, length = leftVisited.length; i < length; i++)
			leftVisited[i] = false;
		for (int i = 0, length = rightVisited.length; i < length; i++)
			rightVisited[i] = false;
	}

	private int getMinDeviation() {
		int min = Integer.MAX_VALUE;

		for (int i = 0; i <= leftLen; i++) {
			if (!leftVisited[i])
				continue;
			for (int j = 0; j <= rightLen; j++) {
				if (rightVisited[j])
					continue;
				int deviation = leftLabel[i] + rightLabel[j] - cost[i][j];
				if (deviation < min)
					min = deviation;
			}
		}
		return min;
	}

	public float getScore(boolean penaliseStringLengthDifference) {
		computeLabel();
		makeMatching();
		int dis = getTotal();

		// float maxLen = rightLen + 1;
		// int l1 = 0;
		// int l2 = 0;
		// for (String s : rightTokens)
		// l1 += s.length();
		// for (String s : leftTokens)
		// l2 += s.length();

		float maxLen;

		if (penaliseStringLengthDifference)
			// maxLen = Math.max(l1, l2);
			maxLen = Math.max(leftTokens.length, rightTokens.length);
		else
			// maxLen = Math.min(l1, l2);
			maxLen = Math.min(leftTokens.length, rightTokens.length);

		if (maxLen > 0)
			return dis / maxLen;
		return 1;
	}

	private int getTotal() {
		// int nTotal = 0;
		int nA = 0;
		for (int i = 0; i <= leftLen; i++) {
			if (outgoing[i] == -1)
				continue;
			// nTotal += cost[i][outgoing[i]];
			// System.out.println(leftTokens[i] + " <-> " + rightTokens[outgoing[i]] + " : " + cost[i][outgoing[i]]);
			// float a = Math.max(leftTokens[i].length(), rightTokens[outgoing[i]].length()) != 0 ? cost[i][outgoing[i]] / Math.max(leftTokens[i].length(), rightTokens[outgoing[i]].length()) : 1;
			// int a = Math.max(leftTokens[i].length(), rightTokens[outgoing[i]].length()) != 0 ? cost[i][outgoing[i]] : 1;
			nA += leftTokens[i].length() + rightTokens[outgoing[i]].length() != 0 ? cost[i][outgoing[i]] : 1;
			// nA += a;

		}
		return nA;
	}

	private void increaseMatches(int li, int lj) {
		int outgoingLength = outgoing.length;
		int[] tmpOut = new int[outgoingLength];
		System.arraycopy(outgoing, 0, tmpOut, 0, outgoingLength);

		int i, j, k;
		i = li;
		j = lj;
		outgoing[i] = j;
		incomming[j] = i;
		while (previous[i] != -1) {
			j = tmpOut[i];
			k = previous[i];
			outgoing[k] = j;
			incomming[j] = k;
			i = k;
		}
	}

	// int FindPath(int source)
	// {
	// int head, tail, idxHead=0;
	// int[] visited=new int[(leftLen+rightLen)+2] ,
	// q=new int[(leftLen+rightLen)+2] ;
	// head=0;
	// for (int i=0; i < visited.Length; i++) visited[i]=0;
	// Flush ();
	//								
	// head=-1;
	// tail=0;
	// q[tail]=source;
	// visited[source]=1;
	// leftVisited[source]=true;
	// int nMerge=leftLen + rightLen + 1;
	//
	// while (head <= tail)
	// {
	// ++head;
	// idxHead=q[head];
	//
	//
	// for (int j=0; j <= (leftLen + rightLen + 1); j++)
	// if(visited[j] == 0)
	// {
	// if (j > leftLen) //j is stay at the RightSide
	// {
	// int idxRight=j - (leftLen + 1);
	// if (idxHead <= leftLen && (leftLabel[idxHead] + rightLabel[idxRight] == cost[idxHead, idxRight]))
	// {
	// ++tail;
	// q[tail]=j;
	// visited[j]=1;
	// previous[j]=idxHead;
	// rightVisited[idxRight]=true;
	// if (In[idxRight] == -1) // pretty good, found a path
	// return j;
	//							
	// }
	// }
	// else if ( j <= leftLen) // is stay at the left
	// {
	// if (idxHead > leftLen && In[idxHead - (leftLen + 1)] == j)
	// {
	// ++tail;
	// q[tail]=j;
	// visited[j]=1;
	// previous[j]=idxHead;
	// leftVisited[j]=true;
	// }
	// }
	// }
	// }
	//
	// return -1;//not found
	// }
	//
	// void Increase_Matchs(int j)
	// {
	// if (previous [j] != -1)
	// do
	// {
	// int i=previous[j];
	// Out[i]=j-(leftLen + 1);
	// In[j-(leftLen + 1)]=i;
	// j=previous[i];
	// } while ( j != -1);
	// }

	private void makeMatching() {
		for (int i = 0, length = outgoing.length; i < length; i++)
			outgoing[i] = -1;
		for (int i = 0, length = incomming.length; i < length; i++)
			incomming[i] = -1;

		for (int k = 0; k <= leftLen; k++) {
			if (outgoing[k] != -1)
				continue;
			// boolean found = false;
			do {
				findPath(k);
				if (!stop)
					relabel();
			} while (!stop);
		}
	}

	private void relabel() {
		int dev = getMinDeviation();

		for (int k = 0; k <= leftLen; k++)
			if (leftVisited[k])
				leftLabel[k] -= dev;

		for (int k = 0; k <= rightLen; k++)
			if (rightVisited[k])
				rightLabel[k] += dev;
	}

	private void swap(int[][] cost) {
		for (int i = 0, length = rightTokens.length; i < length; i++)
			for (int j = 0, lengthJ = leftTokens.length; j < lengthJ; j++)
				this.cost[i][j] = cost[j][i];

		// _cost = new float[_rightTokens.length][_leftTokens.length];
		// System.arraycopy(tmpCost, 0, _cost, 0, tmpCost.length);
	}

	private void traverse(int i) {
		leftVisited[i] = true;

		for (int j = 0; !stop && j <= rightLen; j++) {
			if (rightVisited[j] || leftLabel[i] + rightLabel[j] != cost[i][j])
				continue;
			if (incomming[j] == -1) {
				// if found a path
				stop = true;
				increaseMatches(i, j);
				return;
			}
			int k = incomming[j];
			rightVisited[j] = true;
			previous[k] = i;
			traverse(k);
		}
	}

}