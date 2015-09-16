package core.html;

import core.text.Text;
import core.util.UtilMap;

/**
 * An HTML Decoder.
 */
public final class HtmlDecoder {

	/** The decoder. * */
	private static final HtmlDecoder DECODER = new HtmlDecoder();

	/**
	 * Returns the singleton HTML decoder.
	 * @return the singleton HTML decoder.
	 */
	public static final HtmlDecoder getHtmlDecoder() {
		return DECODER;
	}

	/**
	 * HTML decode the given object.
	 * @param toDecode the object to decode.
	 */
	public final String decode(Object toDecode) {
		if (toDecode == null) {
			throw new NullPointerException();
		}
		String value = toDecode.toString();
		int indexBegin = -1;
		int indexEnd = 0;
		while (indexBegin < value.length()) {
			indexBegin = value.indexOf('&', indexBegin);
			if (indexBegin == -1) {
				break;
			}
			indexEnd = value.indexOf(';', indexBegin);
			if (indexEnd == -1) {
				break;
			}
			indexEnd++;
			String from = value.substring(indexBegin, indexEnd);
			String to = (String) map.get(from.toLowerCase());
			if (to != null) {
				value = Text.replace(value, from, to);
			}
			indexBegin++;
		}
		return value;
	}

	/** The map. * */
	private final UtilMap map = new UtilMap(32, false, false);

	/**
	 * Add the given code mapping.
	 * @param from the code to map from.
	 * @param to the code to map to.
	 */
	private void addCode(String from, String to) {
		from = "&" + from + ";";
		from = from.toLowerCase();
		map.put(from, to);
	}

	/**
	 * Add the given code mapping.
	 * @param from the code to map from.
	 * @param to the code to map to.
	 */
	private void addCode(String from, char to) {
		addCode(from, String.valueOf(to));
	}

	/**
	 * Add the given code mapping.
	 * @param from the code to map from.
	 * @param to the code to map to.
	 */
	private void addCode(int from, String to) {
		String hexFrom = Integer.toHexString(from);
		addCode("#" + from, to);
		addCode("#0" + from, to);
		addCode("#00" + from, to);
		addCode("#000" + from, to);
		addCode("#x" + hexFrom, to);
		addCode("#x0" + hexFrom, to);
		addCode("#x00" + hexFrom, to);
		addCode("#x000" + hexFrom, to);
	}

	/**
	 * Add the given code mapping.
	 * @param from the code to map from.
	 * @param to the code to map to.
	 */
	private void addCode(int from, char to) {
		addCode(from, String.valueOf(to));
	}

	/**
	 * Create a new HTML decoder.
	 */
	private HtmlDecoder() {

		// Numeric Codes
		addCode(10, '\n');
		addCode(13, '\r');
		addCode(33, '!');
		addCode(34, '\"');
		addCode(35, '#');
		addCode(36, '$');
		addCode(37, '%');
		addCode(38, '&');
		addCode(39, '\'');
		addCode(40, '(');
		addCode(41, ')');
		addCode(42, '*');
		addCode(43, '+');
		addCode(44, ',');
		addCode(45, '-');
		addCode(46, '.');
		addCode(47, '/');
		addCode(48, '0');
		addCode(49, '1');
		addCode(50, '2');
		addCode(51, '3');
		addCode(52, '4');
		addCode(53, '5');
		addCode(54, '6');
		addCode(55, '7');
		addCode(56, '8');
		addCode(57, '9');
		addCode(58, ':');
		addCode(59, ';');
		addCode(60, '<');
		addCode(61, '=');
		addCode(62, '>');
		addCode(63, '?');
		addCode(64, '@');
		addCode(65, 'A');
		addCode(66, 'B');
		addCode(67, 'C');
		addCode(68, 'D');
		addCode(69, 'E');
		addCode(70, 'F');
		addCode(71, 'G');
		addCode(72, 'H');
		addCode(73, 'I');
		addCode(74, 'J');
		addCode(75, 'K');
		addCode(76, 'L');
		addCode(77, 'M');
		addCode(78, 'N');
		addCode(79, 'O');
		addCode(80, 'P');
		addCode(81, 'Q');
		addCode(82, 'R');
		addCode(83, 'S');
		addCode(84, 'T');
		addCode(85, 'U');
		addCode(86, 'V');
		addCode(87, 'W');
		addCode(88, 'X');
		addCode(89, 'Y');
		addCode(90, 'Z');
		addCode(91, '[');
		addCode(92, '\\');
		addCode(93, ']');
		addCode(94, '^');
		addCode(95, '_');
		addCode(96, '`');
		addCode(97, 'a');
		addCode(98, 'b');
		addCode(99, 'c');
		addCode(100, 'd');
		addCode(101, 'e');
		addCode(102, 'f');
		addCode(103, 'g');
		addCode(104, 'h');
		addCode(105, 'i');
		addCode(106, 'j');
		addCode(107, 'k');
		addCode(108, 'l');
		addCode(109, 'm');
		addCode(110, 'n');
		addCode(111, 'o');
		addCode(112, 'p');
		addCode(113, 'q');
		addCode(114, 'r');
		addCode(115, 's');
		addCode(116, 't');
		addCode(117, 'u');
		addCode(118, 'v');
		addCode(119, 'w');
		addCode(120, 'x');
		addCode(121, 'y');
		addCode(122, 'z');
		addCode(123, '{');
		addCode(124, '|');
		addCode(125, '}');
		addCode(126, '~');
//		addCode(127, '');
//		addCode(128, '�');
//		addCode(129, '?');
//		addCode(130, '�');
//		addCode(131, '�');
//		addCode(132, '�');
//		addCode(133, '�');
//		addCode(134, '�');
//		addCode(135, '�');
//		addCode(136, '�');
//		addCode(137, '�');
//		addCode(138, '�');
//		addCode(139, '�');
//		addCode(140, '�');
//		addCode(141, '?');
//		addCode(142, '�');
//		addCode(143, '?');
//		addCode(144, '?');
//		addCode(145, '�');
//		addCode(146, '�');
//		addCode(147, '�');
//		addCode(148, '�');
//		addCode(149, '�');
//		addCode(150, '�');
//		addCode(151, '�');
//		addCode(152, '�');
//		addCode(153, '�');
//		addCode(154, '�');
//		addCode(155, '�');
//		addCode(156, '�');
//		addCode(157, '?');
//		addCode(158, '�');
//		addCode(159, '�');
//		addCode(160, ' ');
//		addCode(161, '�');
//		addCode(162, '�');
//		addCode(163, '�');
//		addCode(164, '�');
//		addCode(165, '�');
//		addCode(166, '�');
//		addCode(167, '�');
//		addCode(168, '�');
//		addCode(169, '�');
//		addCode(170, '�');
//		addCode(171, '�');
//		addCode(172, '�');
//		addCode(173, '�');
//		addCode(174, '�');
//		addCode(175, '�');
//		addCode(176, '�');
//		addCode(177, '�');
//		addCode(178, '�');
//		addCode(179, '�');
//		addCode(180, '�');
//		addCode(181, '�');
//		addCode(182, '�');
//		addCode(183, '�');
//		addCode(184, '�');
//		addCode(185, '�');
//		addCode(186, '�');
//		addCode(187, '�');
//		addCode(188, '�');
//		addCode(189, '�');
//		addCode(190, '�');
//		addCode(191, '�');
//		addCode(192, '�');
//		addCode(193, '�');
//		addCode(194, '�');
//		addCode(195, '�');
//		addCode(196, '�');
//		addCode(197, '�');
//		addCode(198, '�');
//		addCode(199, '�');
//		addCode(200, '�');
//		addCode(201, '�');
//		addCode(202, '�');
//		addCode(203, '�');
//		addCode(204, '�');
//		addCode(205, '�');
//		addCode(206, '�');
//		addCode(207, '�');
//		addCode(208, '�');
//		addCode(209, '�');
//		addCode(210, '�');
//		addCode(211, '�');
//		addCode(212, '�');
//		addCode(213, '�');
//		addCode(214, '�');
//		addCode(215, '�');
//		addCode(216, '�');
//		addCode(217, '�');
//		addCode(218, '�');
//		addCode(219, '�');
//		addCode(220, '�');
//		addCode(221, '�');
//		addCode(222, '�');
//		addCode(223, '�');
//		addCode(224, '�');
//		addCode(225, '�');
//		addCode(226, '�');
//		addCode(227, '�');
//		addCode(228, '�');
//		addCode(229, '�');
//		addCode(230, '�');
//		addCode(231, '�');
//		addCode(232, '�');
//		addCode(233, '�');
//		addCode(234, '�');
//		addCode(235, '�');
//		addCode(236, '�');
//		addCode(237, '�');
//		addCode(238, '�');
//		addCode(239, '�');
//		addCode(240, '�');
//		addCode(241, '�');
//		addCode(242, '�');
//		addCode(243, '�');
//		addCode(244, '�');
//		addCode(245, '�');
//		addCode(246, '�');
//		addCode(247, '�');
//		addCode(248, '�');
//		addCode(249, '�');
//		addCode(250, '�');
//		addCode(251, '�');
//		addCode(252, '�');
//		addCode(253, '�');
//		addCode(254, '�');
//		addCode(255, '�');
//		addCode(8212, '�');
//		addCode(8220, '�');
//		addCode(8221, '�');
//		addCode(8226, '�');

		// Text Codes
//		addCode("nbsp", String.valueOf((char) 160));
//		addCode("lt", '<');
//		addCode("gt", '>');
//		addCode("amp", '&');
//		addCode("cent", '�');
//		addCode("quot", '\"');
//		addCode("pound", '�');
//		addCode("copy", '�');
//		addCode("reg", '�');
//		addCode("trade", '�');
//		addCode("frac12", '�');
//		addCode("frac14", '�');
//		addCode("frac34", '�');
//		addCode("brvbar", '�');
//		addCode("deg", '�');
//		addCode("iexcl", '�');
//		addCode("iquest", '�');
//		addCode("laquo", '�');
//		addCode("micro", '�');
//		addCode("middot", '�');
//		addCode("not", '�');
//		addCode("para", '�');
//		addCode("raquo", '�');
//		addCode("sect", '�');
//		addCode("sup1", '�');
//		addCode("sup2", '�');
//		addCode("sup3", '�');
//		addCode("tilde", '�');
//		addCode("yen", '�');
//		addCode("ndash", '�');
//		addCode("mdash", '�');
//		addCode("Agrave", '�');
//		addCode("Aacute", '�');
//		addCode("Acirc", '�');
//		addCode("Atilde", '�');
//		addCode("Auml", '�');
//		addCode("Aring", '�');
//		addCode("AElig", '�');
//		addCode("Ccedil", '�');
//		addCode("Egrave", '�');
//		addCode("Eacute", '�');
//		addCode("Ecirc", '�');
//		addCode("Euml", '�');
//		addCode("Igrave", '�');
//		addCode("Iacute", '�');
//		addCode("Icirc", '�');
//		addCode("Iuml", '�');
//		addCode("ETH", '�');
//		addCode("Ntilde", '�');
//		addCode("Ograve", '�');
//		addCode("Oacute", '�');
//		addCode("Ocirc", '�');
//		addCode("Otilde", '�');
//		addCode("Ouml", '�');
//		addCode("times", '�');
//		addCode("Oslash", '�');
//		addCode("Ugrave", '�');
//		addCode("Uacute", '�');
//		addCode("Ucirc", '�');
//		addCode("Uuml", '�');
//		addCode("Yacute", '�');
//		addCode("THORN", '�');
//		addCode("szlig", '�');
//		addCode("agrave", '�');
//		addCode("aacute", '�');
//		addCode("acirc", '�');
//		addCode("atilde", '�');
//		addCode("auml", '�');
//		addCode("aring", '�');
//		addCode("aelig", '�');
//		addCode("ccedil", '�');
//		addCode("egrave", '�');
//		addCode("eacute", '�');
//		addCode("ecirc", '�');
//		addCode("euml", '�');
//		addCode("igrave", '�');
//		addCode("iacute", '�');
//		addCode("icirc", '�');
//		addCode("iuml", '�');
//		addCode("eth", '�');
//		addCode("ntilde", '�');
//		addCode("ograve", '�');
//		addCode("oacute", '�');
//		addCode("ocirc", '�');
//		addCode("otilde", '�');
//		addCode("ouml", '�');
//		addCode("divide", '�');
//		addCode("oslash", '�');
//		addCode("ugrave", '�');
//		addCode("uacute", '�');
//		addCode("ucirc", '�');
//		addCode("uuml", '�');
//		addCode("yacute", '�');
//		addCode("thorn", '�');
//		addCode("yuml", '�');
//		addCode("OElig", '�');
//		addCode("oelig", '�');
//		addCode("Scaron", '�');
//		addCode("scaron", '�');
//		addCode("Yuml", '�');
//		addCode("fnof", '�');
//		addCode("circ", '�');
//		addCode("tilde", '�');
//		addCode("rdquo","�");
//		addCode("rsquo","�");
//		addCode("ordm","�");
//		addCode("lsquo","�");
//		addCode("bull","�");
//		addCode("apos","'");
	}

	public static void main(String[] args) {
		String decode = "Alt du dr&oslash;mmer om.:";
		System.out.println(decode);
		decode = getHtmlDecoder().decode(decode);
		System.out.println(decode);
		decode = getHtmlDecoder().decode(decode);
		System.out.println(decode);
	}
}
