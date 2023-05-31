package Sintatico;

public class Label {
	
	private static Integer nLabel = 0;
	private static Integer tLabel = 0;
	
	public static String NovoLabel() {
		String label = "L" + nLabel;
		nLabel += 1;
		return label;
	}

	public static String NovaTemp() {
		String t = "T" + tLabel;
		tLabel += 1;
		return t;
	}

}
