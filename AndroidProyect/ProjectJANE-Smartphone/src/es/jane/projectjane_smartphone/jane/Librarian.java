package es.jane.projectjane_smartphone.jane;

public class Librarian {

	static String[] DICTIONARY = { "encender", "apagar", "luz", "luces",
			"ventilador", "ordenador" };

	static public String processRecon(String str) {

		String[] words = str.split(" ");
		str = "";
		// float[] fuzzy = new float[words.length];
		for (int i = 0; i < words.length; i++) {
			if (words[i].equals("entender")) {
				words[i] = "encender";
			} else if (words[i].equals("encenderlo")) {
				words[i] = "encender luz";
			} else if (words[i].equals("entenderlo")) {
				words[i] = "encender luz";
			} else if (words[i].equals("pagar")) {
				words[i] = "apagar";
			} else if (words[i].equals("pagarlo")) {
				words[i] = "apagar luz";
			} else if (words[i].equals("apagarlo")) {
				words[i] = "apagar luz";
			} else if (words[i].equals("22")) {
				words[i] = "ventilador";
			}
			str = str + words[i];
			if (i != words.length - 1) {
				str = str + " ";
			}
		}

		return str;
	}

	static public String answer(String str) {
		if (str.equals("apagar luz")) {
			return "Luz apagada";
		} else if (str.equals("encender luz")) {
			return "Luz encendida";
		} else if (str.equals("apagar ventilador")) {
			return "Ventilador Apagado";
		} else if (str.equals("encender ventilador")) {
			return "Ventilador encendido";
		} else if (str.equals("apagar ordenador")) {
			return "Ordenador apagado";
		} else if (str.equals("encender ordenador")) {
			return "Ordenador encendido";
		} else if (str.contains("pene") || str.contains("teta")
				|| str.contains("toto") || str.contains("vagina")
				|| str.contains("mamada") || str.contains("polla")) {
			return "Hare como si no hubiera oido nada";
		} else {
			return "";
		}
	}
}
