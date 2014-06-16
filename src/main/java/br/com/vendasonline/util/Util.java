package br.com.vendasonline.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BadPdfFormatException;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

public class Util {

	private static final int[] pesoCPF;
	private static final int[] pesoCNPJ;
	private static final SimpleDateFormat dateFormat;
	private static final SimpleDateFormat hourFormat;
	private static final NumberFormat numberFormat;

	static {
		pesoCPF = new int[] { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
		pesoCNPJ = new int[] { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		hourFormat = new SimpleDateFormat("hh:mm");
		numberFormat = NumberFormat.getInstance(new Locale("PT", "BR"));
	}

	/**
	 * Converte o cpf formatado para um cpf sem formatação. Os cpfs devem ser
	 * salvos no banco de dados sem a formatação. <li>Parâmetro com o cpf
	 * 000.000.000-00 <li>Retorno com o cpf 00000000000
	 * 
	 * @param cpf
	 * @return String com os numeros sem formatação (sem pontos e hifen)
	 */
	public static String unformatCpf(String cpf) {

		if (cpf == null) {
			return null;
		}

		try {
			cpf = cpf.replaceAll("\\.", "").replaceAll("-", "");
			return cpf;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converte o cpf sem formatação para um cpf formatado. Os cpfs devem ser
	 * exibidos com a seguinte formatação 000.000.000-00 <li>Parâmetro com o cpf
	 * 00000000000 <li>Retorno com o cpf 000.000.000-00
	 * 
	 * @param cpf
	 * @return String com os numeros com formatação (com pontos e hifen)
	 */
	public static String formatCpf(String cpf) {

		if (cpf == null) {
			return null;
		}

		int pos = cpf.indexOf('.');
		if (pos != -1) {
			return cpf;
		}

		try {
			cpf = cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
					"$1\\.$2\\.$3\\-$4");
			return cpf;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converte o cnpj formatado para um cnpj sem formatação. Os cpfs devem ser
	 * salvos no banco de dados sem a formatação. <li>Parâmetro com o cnpj
	 * 00.000.000/0000-00 <li>Retorno com o cnpj 00000000000000
	 * 
	 * @param cnpj
	 * @return String com os numeros sem formatação (sem pontos e hifen)
	 */
	public static String unformatCnpj(String cnpj) {
		if (isNull(cnpj)) {
			return null;
		}

		try {
			cnpj = cnpj.replaceAll("\\.", "").replaceAll("-", "")
					.replaceAll("/", "");
			return cnpj;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converte o cnpj sem formatação para um cnpj formatado. Os cnpjs devem ser
	 * exibidos com a seguinte formatação 00.000.000/0000-00 <li>Parâmetro com o
	 * cpf 00000000000000 <li>Retorno com o cpf 00.000.000/0000-00
	 * 
	 * @param cnpj
	 * @return String com os numeros com formatação (com pontos e hifen)
	 */
	public static String formatCnpj(String cnpj) {
		try {
			return getStringCnpj(cnpj);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converte o cep não formatado para um cep formatado para exibição <li>
	 * Parâmetro com o cep <li>Retorno com o cep 99999-999, se o numero possuir
	 * 8 caraceteres. Sem formatação caso contrário.
	 * 
	 * @param cep
	 * @return String com os numeros formatados
	 */
	public static String formatCep(String cep) {

		if (isNull(cep)) {
			return null;
		}

		try {
			cep = cep.replaceAll("\\.", "").replaceAll("-", "")
					.replaceAll("/", "");
			return cep.length() >= 8 ? cep.substring(0, 5) + "-"
					+ cep.substring(5, 8) : cep;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converte o Telefone sem Formatação para o Formatado (XX) XXXX-XXXX <li>
	 * Parâmetro com o Telefone XXXXXXXXXX <li>Retorno com o Telefone (XX)
	 * XXXX-XXXX
	 */
	public static String formatTelefone(String telefone) {

		if (isNull(telefone)) {
			return null;
		}

		try {
			telefone = unformat(telefone);
			if (telefone.length() == 11) {
				if (telefone.trim().startsWith("0")) {
					telefone = "(" + telefone.substring(1, 3) + ")" + " "
							+ telefone.substring(3, 7) + "-"
							+ telefone.substring(7, 11);
				} else {
					telefone = "(" + telefone.substring(0, 2) + ")" + " "
							+ telefone.substring(2, 7) + "-"
							+ telefone.substring(7, 11);
				}
			} else if (telefone.length() == 10) {
				telefone = "(" + telefone.substring(0, 2) + ")" + " "
						+ telefone.substring(2, 6) + "-"
						+ telefone.substring(6, 10);
			} else if (telefone.length() == 10) {
				telefone = "3" + telefone.substring(0, 3) + "-"
						+ telefone.substring(3, telefone.length());
			} else {
				telefone = telefone.substring(0, 4) + "-"
						+ telefone.substring(4, telefone.length());
			}
			return telefone;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Método que recebe uma string e limpa os espaços em branco do texto. Caso
	 * exista mais de um espaço considerará apenas 1 branco.
	 * 
	 * @param valor
	 * @return valor limpo dos espaços
	 */
	public static String compactarEspacoBranco(String valor) {
		if (valor == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		valor = valor.trim();

		boolean passBlank = false;

		char[] c = valor.toCharArray();
		for (int i = 0; i < c.length; i++) {
			switch (c[i]) {
			case ' ':
				if (!passBlank) {
					sb.append(c[i]);
				}
				passBlank = true;
				break;
			default:
				sb.append(c[i]);
				passBlank = false;
				break;
			}
		}

		return sb.toString();
	}

	public static String compactarEspacoBrancoUpper(String valor) {
		return (valor == null ? valor : compactarEspacoBranco(valor)
				.toUpperCase());
	}

	/**
	 * UTILIZAR método compactarEspacoBranco(String) Método que recebe uma
	 * string e limpa os espaços em branco do texto. Caso exista mais de um
	 * espaço considerará apenas 1 branco.
	 * 
	 * @param valor
	 * @return valor limpo dos espaços
	 */
	public static String limpaEspacoBranco(String valor) {
		return compactarEspacoBranco(valor);
	}

	/**
	 * @ Método que recebe uma string e limpa os espaços em branco do texto.
	 * Caso exista mais de um espaço considerará apenas 1 branco. Também força o
	 * valor a ficar todo em maiúsculo
	 * 
	 * @param valor
	 * @return valor limpo dos espaços
	 */
	public static String limpaEspacoBrancoUpper(String valor) {
		if (valor == null) {
			return null;
		}
		return limpaEspacoBranco(valor.toUpperCase());
	}

	public static boolean isNull(Number valor) {
		return ((valor == null) || (valor.intValue() == 0));
	}

	public static boolean isNull(Short valor) {
		return ((valor == null) || (valor.shortValue() == 0));
	}

	public static boolean isNull(Byte valor) {
		return ((valor == null) || (valor.byteValue() == 0));
	}

	public static boolean isNull(Integer valor) {
		return ((valor == null) || (valor.intValue() == 0));
	}

	public static boolean isNull(Long valor) {
		return ((valor == null) || (valor.longValue() == 0));
	}

	public static boolean isNull(Double valor) {
		return ((valor == null));
	}

	public static boolean isNull(BigDecimal valor) {
		return ((valor == null));
	}

	public static boolean isNull(BigInteger valor) {
		return ((valor == null));
	}

	public static boolean isNull(String valor) {
		return (("0".equals(valor)) || (valor == null)
				|| (valor.trim().length() == 0)
				|| ("undefined".equals(valor.trim().toLowerCase())) || ("null"
					.equals(valor.trim())));
	}

	public static boolean isNull(Character valor) {
		return ((valor == null) || (Character.isWhitespace(valor.charValue())));
	}

	public static boolean isNull(Date valor) {
		return ((valor == null));
	}

	public static boolean isNull(char valor) {
		return ((valor == 0x0));
	}

	public static boolean isNull(Boolean value) {
		return value == null;
	}

	public static boolean isNull(List<?> lista) {
		return (lista == null || lista.isEmpty());
	}

	public static boolean isNull(Object[] array) {
		return array == null || array.length == 0;
	}

	public static String toString(Clob clob) throws IOException, SQLException {
		String retorno = "";

		if (clob == null) {
			return "";
		}

		/*
		 * String str = ""; if ( clob.length() > 0 ) str = clob.getSubString(1,
		 * (int) clob.length()); return str;
		 */

		try {
			StringBuffer strOut = new StringBuffer();
			String aux;

			BufferedReader br = new BufferedReader(clob.getCharacterStream());

			while ((aux = br.readLine()) != null) {
				strOut.append(aux + "\n");
			}
			retorno = strOut.toString();
		} catch (IOException e) {
		} catch (SQLException e) {
		}

		return retorno;
	}

	/**
	 * Preenche uma String com valores e quantidades passadas por parâmetros<br>
	 * <li>parametros: texto=xx, letra=A, tamanho=5 <li>retorno: string com o
	 * valor AAAxx
	 * 
	 * @param texto
	 * @param letra
	 * @param tamanho
	 * @return String com o valor final gerado
	 */
	public static String preencherStringEsquerda(String texto, String letra,
			int tamanho) {

		if (letra == null) {
			return texto;
		}

		if (texto == null) {
			texto = "";
		}

		while (texto.length() < tamanho) {
			texto = letra + texto;
		}

		return texto;
	}

	public static String format(String valor, String mascara) {
		String dado = "";
		// remove caracteres nao numericos
		for (int i = 0; i < valor.length(); i++) {
			char c = valor.charAt(i);
			if (Character.isDigit(c)) {
				dado += c;
			}
		}
		int indMascara = mascara.length();
		int indCampo = dado.length();
		for (; indCampo >= 0 && indMascara > 0;) {
			if (mascara.charAt(--indMascara) == '#') {
				indCampo--;
			}
		}
		String saida = "";
		for (; indMascara < mascara.length(); indMascara++) {
			saida += ((mascara.charAt(indMascara) == '#') ? dado
					.charAt(indCampo++) : mascara.charAt(indMascara));
		}
		return saida;
	}

	public static String unformat(String valor) {
		if (isNull(valor)) {
			return null;
		}
		try {
			String r = "";
			for (char c : valor.toCharArray()) {
				if (Character.isDigit(c) || Character.isLetter(c)) {
					r += c;
				}
			}
			return r;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getStringCpf(String cpf) {
		if (isNull(cpf)) {
			return "";
		}
		try {
			while (cpf.length() < 11) {
				cpf = "0" + cpf;
			}
			return format(cpf, "###.###.###-##");
		} catch (Exception e) {
			return "";
		}
	}

	public static String getStringCnpj(String cnpj) {
		if (isNull(cnpj)) {
			return "";
		}
		try {
			while (cnpj.length() < 14) {
				cnpj = "0" + cnpj;
			}
			return format(cnpj, "##.###.###/####-##");
		} catch (Exception e) {
			return "";
		}
	}

	public static List<String> separarPalavras(String s) {

		StringTokenizer tok = new StringTokenizer(s, " ");

		List<String> lista = new ArrayList<String>();
		while (tok.hasMoreElements()) {
			lista.add(tok.nextToken());
		}

		return lista;
	}

	/**
	 * Recebe uma array de qualquer coisa e retorna uma String com os valores
	 * separados por vírgula. <b>Observação: </b> Valores em branco não serão
	 * desconsiderados .Segue exemplo: <li>Parâmetro com a array de String
	 * {"pass","return","available"} <li>Retorno com a String
	 * "pass,return,available"
	 * 
	 * @param values
	 * @return
	 */
	public static String criarStringPorArray(Object[] values) {

		if (values == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(values[i].toString());
		}

		return sb.toString();
	}

	/**
	 * Converte uma array de strings para um array de long
	 * 
	 * @param value
	 *            Array de string
	 * @return array de long
	 */
	public static Long[] convertStringByLong(String[] value) {

		if (value != null && value.length > 0) {
			if (!"".equals(value[0])) {
				Long[] toReturn = new Long[value.length];
				for (int i = 0; i < value.length; i++) {
					toReturn[i] = new Long(value[i]);
				}
				return toReturn;
			}
		}
		return null;
	}

	/**
	 * Converte um array de byte para uma string com valor hexadecimal
	 * 
	 * @param data
	 * @return string
	 */
	public static String convertByteToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int twoHalfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9)) {
					buf.append((char) ('0' + halfbyte));
				} else {
					buf.append((char) ('a' + (halfbyte - 10)));
				}
				halfbyte = data[i] & 0x0F;
			} while (twoHalfs++ < 1);
		}
		return buf.toString();
	}

	/**
	 * Deixa somente números nesta String
	 * 
	 * @param str
	 * @return
	 */
	public static String cleanNumber(String str) {

		if (str == null) {
			return "";
		}

		char[] chars = str.toCharArray();
		StringBuffer ret = new StringBuffer();
		int size = chars.length;
		for (int i = 0; i < size; i++) {
			switch (chars[i]) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				ret.append(chars[i]);
				break;
			default:
				break;
			}
		}
		return ret.toString();
	}

	/**
	 * Disponibiliza um arquivo para download no lado do cliente.
	 * 
	 * @param file
	 * @param response
	 * @param fileName
	 * @throws Exception
	 */
	public static void disponibilizarArquivoDownload(File file,
			HttpServletResponse response, String fileName) throws Exception {

		response.setContentType("application/x-msdownload; charset=iso-8859-1");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ URLEncoder.encode(fileName, "utf-8"));
		response.resetBuffer();

		ServletOutputStream out = response.getOutputStream();
		InputStream in = new FileInputStream(file);

		byte[] buffer = new byte[(int) file.length()];
		int nLidos;
		while ((nLidos = in.read(buffer)) >= 0) {
			out.write(buffer, 0, nLidos);
		}

		out.flush();
		out.close();
		in.close();

		try {
			file.delete();
		} catch (Exception e) {
		}

	}

	/**
	 * Concatena dois arrays de objetos, eliminando duplicações
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static Object[] arrayMerge(Object[] array1, Object[] array2) {
		ArrayList<Object> listaTotal = new ArrayList<Object>(
				Arrays.asList(array1));
		listaTotal.removeAll(new ArrayList<Object>(Arrays.asList(array2)));
		listaTotal.addAll(new ArrayList<Object>(Arrays.asList(array2)));
		return (listaTotal.toArray(array1));
	}

	/**
	 * Concatena duas listas, eliminando duplicações
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List listMerge(List list1, List list2) {
		if (list1 != null) {
			list1.removeAll(list2);
			list1.addAll(list2);
			return list1;
		} else {
			return list2;
		}
	}

	/**
	 * Retorna o valor de itens preenchidos de uma array. Esse método irá
	 * desconsiderar os posições que sejam: <li>Nulas <li>Vazia ("")
	 * 
	 * @param array
	 *            de string
	 * @return int informando a quantidade real de itens
	 */
	public static int arrayLength(Object[] array) {

		if (array == null) {
			return 0;
		}

		int toReturn = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null && !"".equals(array[i])) {
				toReturn++;
			}
		}

		return toReturn;
	}

	/**
	 * Trunca um valor em um determinado número de casas decimais
	 * 
	 * @param valor
	 * @return
	 */
	public static double truncar(Double valor, int numeroDecimais) {
		long temp = new Double(Math.pow(10d, new Double(numeroDecimais)))
				.longValue();
		if (valor > 0) {
			return Math.floor(valor * temp) / temp;
		} else {
			return Math.ceil(valor * temp) / temp;
		}
	}

	/**
	 * Converte Strings do Banco para HTML
	 * 
	 * @param valor
	 * @return
	 */
	public static String convertDadosBancoToHtml(String valor) {
		if (valor != null) {
			valor = valor.replaceAll("\n", "<br/>").replaceAll("(?<=(^\\s*)) ",
					"&nbsp;");
		}
		return valor;
	}

	/**
	 * Converte Strings do Banco para HTML
	 * 
	 * @param valor
	 * @return
	 */
	public static String convertDadosBancoToHtml(Clob clob) {
		String retorno = "";
		if (clob != null) {
			try {
				StringBuffer strOut = new StringBuffer();
				String aux;

				BufferedReader br = new BufferedReader(
						clob.getCharacterStream());

				while ((aux = br.readLine()) != null) {
					aux = aux.replaceAll("(?<=(^\\s*)) ", "&nbsp;");
					strOut.append(aux + "<br/>");
				}
				retorno = strOut.toString();
			} catch (IOException e) {
			} catch (SQLException e) {
			}
		}

		return retorno;
	}

	/**
	 * Converte o pis/pasep formatado para um pis/pasep sem formatação. <li>
	 * Parâmetro com o pis/pasep 00000000000 <li>Retorno com o pis/pasep
	 * 000.00000.00.0
	 * 
	 * @param cpf
	 * @return String com os numeros sem formatação (sem pontos e hifen)
	 */
	public static String formatPisPasepDB(String pisPasep) {

		if (pisPasep == null) {
			return null;
		}

		try {
			return pisPasep.replaceAll("\\.", "");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converte o pis/pasep sem formatação para um pis/pasep formatado. <li>
	 * Parâmetro com o pis/pasep 000.00000.00.0 <li>Retorno com o pis/pasep
	 * 00000000000
	 * 
	 * @param cpf
	 * @return String com os numeros com formatação (com pontos e hifen)
	 */
	public static String unformatPisPasepDB(String pisPasep) {

		if (pisPasep == null) {
			return null;
		}

		int pos = pisPasep.indexOf('.');
		if (pos != -1) {
			return pisPasep;
		}

		try {
			return pisPasep.substring(0, 3) + "." + pisPasep.substring(3, 8)
					+ "." + pisPasep.substring(8, 10) + "."
					+ pisPasep.substring(10, 11);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Converte a hora formatada para uma hora sem formatação. As horas devem
	 * ser salvas no banco de dados sem a formatação. <li>Parâmetro com a hora
	 * 00:00 <li>Retorno com a hora 0000
	 * 
	 * @param hora
	 * @return String com os numeros sem formatação (sem pontos e hifen)
	 */
	public static String formatHoraDB(String hora) {

		if (hora == null) {
			return null;
		}

		try {
			hora = hora.replaceAll("\\:", "");
			return hora;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converte a hora sem formatação para uma hora formatada. As horas devem
	 * ser exibidas com a seguinte formatação 00:00 <li>Parâmetro com a hora
	 * 0020 <li>Retorno com a hora 00:00
	 * 
	 * @param hora
	 * @return String com os numeros com formatação (com pontos e hifen)
	 */
	public static String unformatHoraDB(String hora) {

		if (hora == null) {
			return null;
		}

		int pos = hora.indexOf(':');
		if (pos != -1) {
			return hora;
		}

		try {

			while (hora.length() < 4) {
				hora = 0 + hora;
			}

			StringBuffer str = new StringBuffer();
			char[] chrHora = hora.toCharArray();
			for (int i = 0; i < chrHora.length; i++) {
				if ((i % 2) == 0 && i != 0) {
					str.append(":");
				}
				str.append(chrHora[i]);
			}
			return str.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * Converte uma data do tipo string com formatação (barras) para um formato
	 * a ser inserido no banco de dados, segue exemplo: <li>Parametro com a data
	 * 01/01/2007 <li>Retorna com a data 01012007
	 * 
	 * @param data
	 * @return String com a data formatada sem as barras
	 */
	public static String formatDateDB(String data) {

		if (data == null || data.length() < 10) {
			return null;
		}

		try {
			StringBuffer dataStringBuffer = new StringBuffer();
			dataStringBuffer.append(data.substring(0, 2));
			dataStringBuffer.append(data.substring(3, 5));
			dataStringBuffer.append(data.substring(6, 10));
			return dataStringBuffer.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * Converte uma data do tipo string com formatação (barras) para um formato
	 * a ser invertido, segue exemplo: <li>Parametro com a data 02/01/2007 <li>
	 * Retorna com a data 20070102
	 * 
	 * @param data
	 * @return String com a data formatada sem as barras
	 */
	public static String inverteDataDB(String data) {

		if (data == null || data.length() < 10) {
			return null;
		}

		try {
			StringBuffer dataStringBuffer = new StringBuffer();
			dataStringBuffer.append(data.substring(6, 10));
			dataStringBuffer.append(data.substring(3, 5));
			dataStringBuffer.append(data.substring(0, 2));
			return dataStringBuffer.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converte uma data do tipo string sem formatação no banco de dados para
	 * uma data formatada, segue exemplo: <li>Parametros com a data 01012007 <li>
	 * Retorno com a data 01/01/2007
	 * 
	 * @param data
	 * @return String com a data formatada com as barras
	 */
	public static String unformatDateDB(String data) {
		if (data == null || data.length() < 8) {
			return null;
		}

		try {
			StringBuffer dataStringBuffer = new StringBuffer();
			dataStringBuffer.append(data.substring(0, 4));
			dataStringBuffer.append("/");
			dataStringBuffer.append(data.substring(4, 6));
			dataStringBuffer.append("/");
			dataStringBuffer.append(data.substring(6, 8));
			return dataStringBuffer.toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isValidaCPF(String cpf) {
		if ((cpf == null) || (cpf.length() != 11)) {
			return false;
		}

		Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
		Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);
		return cpf.equals(cpf.substring(0, 9) + digito1.toString()
				+ digito2.toString());
	}

	public static boolean isValidaCNPJ(String cnpj) {
		if ((cnpj == null) || (cnpj.length() != 14)) {
			return false;
		}

		Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
		Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1,
				pesoCNPJ);
		return cnpj.equals(cnpj.substring(0, 12) + digito1.toString()
				+ digito2.toString());
	}

	public static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	public static boolean isValidaEmail(String email) {
		Pattern p = Pattern
				.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
		Matcher m = p.matcher(email);
		if (!m.find()) {
			return false;
		}
		return true;
	}

	public static Date getData(final String data) {
		if (isNull(data)) {
			return null;
		}
		try {
			return dateFormat.parse(data);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getStringData(final Date data) {
		if (isNull(data)) {
			return "";
		}
		try {
			return dateFormat.format(data);
		} catch (Exception e) {
			return "";
		}
	}

	public static String getStringHora(final Date data) {
		if (isNull(data)) {
			return "";
		}
		try {
			return hourFormat.format(data);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Usado para comparar datas desprezando-se o time.
	 * 
	 * @param data1
	 *            , data2
	 * @return 0 para datas iguais; -1 para data1 menor que data2; 1 para data1
	 *         maior que data2
	 * @throws ParseException
	 */
	public static int compareTo(Date data1, Date data2) throws ParseException {
		String dt1 = dateFormat.format(data1);
		String dt2 = dateFormat.format(data2);
		return dateFormat.parse(dt1).compareTo(dateFormat.parse(dt2));
	}

	public static String formatarNumeroOcorrencia(String numero) {
		if (Util.isNull(numero)) {
			return null;
		}

		if (numero.contains("#")) {
			String txNumeroOcorrencia[] = numero.split("#");
			return txNumeroOcorrencia[0];

		} else {
			String regex = "(\\d\\d)(\\w)(\\d\\d\\d\\d)(\\d\\d\\d\\d\\d\\d\\d)";
			return numero.trim().replaceAll(regex, "$1\\.$2\\.$3\\.$4");
		}

	}

	public static String formatarNumeroTabela(String numero) {

		if (numero != null && numero.length() == 14) {
			return numero.substring(0, 2) + "." + numero.substring(2, 3) + "."
					+ numero.substring(3, 7) + "."
					+ numero.substring(7, numero.length());
		}

		return null;
	}

	public static Integer calculaIdade(Date dtNascimento) {
		Calendar dataAtual = Calendar.getInstance();
		int dd = dataAtual.get(Calendar.DAY_OF_MONTH);
		int mm = dataAtual.get(Calendar.MONTH);
		int yy = dataAtual.get(Calendar.YEAR);
		Calendar dataNascimento = Calendar.getInstance();
		dataNascimento.setTime(dtNascimento);
		int dd2 = dataNascimento.get(Calendar.DAY_OF_MONTH);
		int mm2 = dataNascimento.get(Calendar.MONTH);
		int yy2 = dataNascimento.get(Calendar.YEAR);

		int diferencaMes = mm - mm2;
		int diferencaDia = dd - dd2;
		int idade = yy - yy2;

		if (idade > 0
				&& (diferencaMes < 0 || (diferencaMes == 0 && diferencaDia < 0))) {
			idade--;
		}

		return idade;
	}

	/**
	 * Usado para normatizar uma String tirando os caracteres especias
	 * 
	 * @param String
	 *            no com caracteres especiais ex: é à î _ @"
	 * @return String no sem os caracteres especias e a i _ @
	 */
	public static String normalizaString(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("");
	}

	/**
	 * 
	 * @param <T>
	 *            corresponde ao objeto do tipo do parametro <b>clazz</b>
	 *            passado
	 * @param json
	 *            String contendo o json no formato padrao ou no formato sisp
	 * @param clazz
	 *            qualquer class
	 * @return instancia do objeto <T>
	 * 
	 * @see
	 * 
	 *      <li><b>Formato sisp:</b>
	 *      !obj!:|(!atributo!:!valor_literal!,!atributo!:valor)\</li> <li>
	 *      <b>Formato json:</b>
	 *      "obj":[{"atributo":"valor_literal","atributo":valor}]</li>
	 * 
	 */
	public static <T> T getObjectTOStringJsonByGson(String json, Class<T> clazz) {
		Gson gson = new Gson();
		try {
			return gson.fromJson(json, clazz);
		} catch (Exception e) {
			return gson.fromJson(formatStringTOjson(json), clazz);
		}
	}

	/**
	 * Recebe uma string json no formato sisp e transforma para o formato json
	 * 
	 * @param json
	 * @return String formatada para json
	 * @see
	 * 
	 *      <li><b>Formato sisp:</b>
	 *      !obj!:|(!atributo!:!valor_literal!,!atributo!:valor)\</li> <li>
	 *      <b>Formato json:</b>
	 *      "obj":[{"atributo":"valor_literal","atributo":valor}]</li>
	 */
	public static String formatStringTOjson(String json) {

		json = json.replaceAll("\\!", "\"");
		json = json.replaceAll("\\(", "\\{");
		json = json.replaceAll("\\)", "\\}");
		json = json.replaceAll("[\\|]", "[");
		json = json.replaceAll("[\\\\]", "]");

		return json;
	}

	public static String getStringJsonByGson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	public static byte[] getByteFrom(Blob blob) {
		if (blob == null) {
			return null;
		}
		try {
			return blob.getBytes(1l, (int) blob.length());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Realiza a formatação dos dados da localização.
	 * 
	 * @param tpLogradouro
	 *            o tipo do logradouro.
	 * @param nmLogradouro
	 *            o nome do logradouro.
	 * @param tpCruzamento
	 *            o tipo do cruzamento.
	 * @param nmCruzamento
	 *            o nome do cruzamento.
	 * @param nmBairro
	 *            o nome do bairro.
	 * @param nmZona
	 *            o nome da zona.
	 * @param nrResidencia
	 *            o número da residência.
	 * @param nrAndar
	 *            o número do andar.
	 * @param nrApartamento
	 *            o número do apartamento.
	 * @return String formatada com a localização.
	 */
	public static String formataLocalizacao(String tpLogradouro,
			String nmLogradouro, String tpCruzamento, String nmCruzamento,
			String nmBairro, String nmZona, String nrResidencia,
			String nrAndar, String nrApartamento) {

		return tpLogradouro
				+ " "
				+ nmLogradouro
				+ (Util.isNull(tpCruzamento) ? "" : (" com " + tpCruzamento
						+ " " + nmCruzamento))
				+ ", Bairro "
				+ nmBairro
				+ (Util.isNull(nrResidencia) ? "" : (", Nº " + nrResidencia))
				+ (Util.isNull(nrAndar) ? "" : (nrAndar + "o Andar"))
				+ (Util.isNull(nrApartamento) ? ""
						: (", Apto. " + nrApartamento)) + ", " + nmZona;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static byte[] juntaPDF(List<PdfReader> lstReader, String outFile)
			throws DocumentException, FileNotFoundException, IOException,
			BadPdfFormatException {

		// preparando as variaveis pra juntar os pdfs
		ArrayList master = new ArrayList();

		int pageOffset = 0;
		int f = 0;

		Document document = null;
		PdfCopy writer = null;

		for (PdfReader reader : lstReader) {
			// junta tudo
			reader.consolidateNamedDestinations();
			int n = reader.getNumberOfPages();
			List bookmarks = SimpleBookmark.getBookmark(reader);
			if (bookmarks != null) {
				if (pageOffset != 0) {
					SimpleBookmark
							.shiftPageNumbers(bookmarks, pageOffset, null);
				}
				master.addAll(bookmarks);
			}
			pageOffset += n;

			if (f == 0) {
				// step 1: creation of a document-object
				document = new Document(reader.getPageSizeWithRotation(1));

				// step 2: we create a writer that listens to the document
				writer = new PdfCopy(document, new FileOutputStream(outFile));

				// step 3: we open the document
				document.open();
			}

			// step 4: we add content
			PdfImportedPage page;
			for (int i = 0; i < n;) {
				++i;
				page = writer.getImportedPage(reader, i);
				writer.addPage(page);
			}
			PRAcroForm paAcroForm = reader.getAcroForm();
			if (paAcroForm != null) {
				writer.copyAcroForm(reader);
			}
			f++;
		}

		if (!master.isEmpty()) {
			writer.setOutlines(master);
		}

		// step 5: we close the document
		document.close();
		writer.close();

		File arquivoFile = new File(outFile);
		InputStream in;
		byte[] buffer = new byte[0];
		try {
			in = new FileInputStream(arquivoFile);
			buffer = new byte[(int) arquivoFile.length()];
			in.read(buffer);
			in.close();
			arquivoFile = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static byte[] juntaPDFArquivoTemporario(List<PdfReader> lstReader,
			String nameFile) throws DocumentException, FileNotFoundException,
			IOException, BadPdfFormatException {

		// preparando as variaveis pra juntar os pdfs
		ArrayList master = new ArrayList();

		int pageOffset = 0;
		int f = 0;

		Document document = null;
		PdfCopy writer = null;
		File arquivoFile = File.createTempFile(nameFile, "pdf");

		for (PdfReader reader : lstReader) {
			// junta tudo
			reader.consolidateNamedDestinations();
			int n = reader.getNumberOfPages();
			List bookmarks = SimpleBookmark.getBookmark(reader);
			if (bookmarks != null) {
				if (pageOffset != 0) {
					SimpleBookmark
							.shiftPageNumbers(bookmarks, pageOffset, null);
				}
				master.addAll(bookmarks);
			}
			pageOffset += n;

			if (f == 0) {
				// step 1: creation of a document-object
				document = new Document(reader.getPageSizeWithRotation(1));

				// step 2: we create a writer that listens to the document
				writer = new PdfCopy(document,
						new FileOutputStream(arquivoFile));

				// step 3: we open the document
				document.open();
			}

			// step 4: we add content
			PdfImportedPage page;
			for (int i = 0; i < n;) {
				++i;
				page = writer.getImportedPage(reader, i);
				writer.addPage(page);
			}
			PRAcroForm paAcroForm = reader.getAcroForm();
			if (paAcroForm != null) {
				writer.copyAcroForm(reader);
			}
			f++;
		}

		if (!master.isEmpty()) {
			writer.setOutlines(master);
		}

		// step 5: we close the document
		document.close();
		writer.close();

		InputStream in;
		byte[] buffer = new byte[0];
		try {
			in = new FileInputStream(arquivoFile);
			buffer = new byte[(int) arquivoFile.length()];
			in.read(buffer);
			in.close();
			arquivoFile = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer;

	}

	public static String formataSituacaoNatureza(String inSituacaoNatureza) {
		String situacaoNatureza = "";

		if (inSituacaoNatureza == null) {
			return situacaoNatureza;
		} else if (inSituacaoNatureza.equals("C")) {
			situacaoNatureza = "Consumado";
		} else if (inSituacaoNatureza.equals("T")) {
			situacaoNatureza = "Tentado";
		}

		return situacaoNatureza;
	}

	public static String toCamelCase(String texto, String regex) {
		if (texto == null || regex == null) {
			return null;
		}

		String[] palavra = texto.split(regex);
		String retorno = "";

		for (int i = 0; i < palavra.length; i++) {
			if (i > 0) {
				retorno += palavra[i].substring(0, 1).toUpperCase()
						+ palavra[i].substring(1);
			} else {
				retorno += palavra[i].toLowerCase();
			}
		}

		return retorno;
	}

	public static String toCaseInsensitive(String value) {
		return "UPPER(" + value + ")";
	}

	public static String toCaseAndAccentInsensitive(String value) {
		return "TRANSLATE(UPPER("
				+ value
				+ "),'ÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕÄËÏÖÜÇÑYÝ','AEIOUAEIOUAEIOUAOAEIOUCNYY')";
	}

	public static File byteArrayToFilePDF(byte[] buffer) {
		File file = new File(new Date().getTime() + ".pdf");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static byte[] fileToByteArray(File file) {
		try {
			return IOUtils.toByteArray(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String normalizeWhitespace(String texto) {
		if (texto == null) {
			return null;
		}

		return texto.trim().replaceAll("( +)", " ");
	}

	/***
	 * fuunção retira o tab e normaliza os espaços em branco
	 */

	public static String normalizeTabWithWhitespac(String texto) {
		if (texto == null) {
			return null;
		}

		texto = texto.replaceAll("\t", " ");
		return texto.trim().replaceAll("( +)", " ");
	}

	public static Object[] getParameters(Object... args) {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object value : args) {

			if (value instanceof Number) {

				if (!Util.isNull((Number) value)) {
					list.add(value);
				}

			} else if (value instanceof String) {

				if (!Util.isNull((String) value)) {
					list.add(value);
				}

			}

		}

		return list.toArray();

	}

	public static Object[] getParameters(Map<Integer, Object> values) {
		ArrayList<Object> list = new ArrayList<Object>();

		for (Integer key : values.keySet()) {
			list.add(values.get(key));
		}

		return list.toArray();
	}

	/**
	 * 
	 * @param array
	 *            - objeto que será utilizado como lista em clausula IN, NOT IN,
	 *            etc
	 * @param cont
	 *            - Utilizado como chave do objeto Map
	 * @param values
	 *            - map que armazena todos os parametros da consulta
	 * @return String formatada para utilizar preparedStatement com Arrays.
	 */
	public static String formatterParameterByArray(Object[] array, int cont,
			Map<Integer, Object> values) {
		StringBuilder formatterParameter = new StringBuilder();
		int i = 0;
		for (Object value : array) {
			i++;
			cont++;
			if (i == array.length) {
				formatterParameter.append("?");
				values.put(cont, value);
			} else {
				formatterParameter.append("?, ");
				values.put(cont, value);
			}

		}
		return formatterParameter.toString();
	}

	public static String addModoLike(String valor) {
		StringBuilder like = new StringBuilder();
		return like.append("%").append(valor).append("%").toString();
	}

	public static Float parseFloat(String value) {
		if (Util.isNull(value)) {
			return 0f;
		}

		try {
			return numberFormat.parse(value).floatValue();
		} catch (ParseException e) {
			return 0f;
		}
	}

	public static Date calcularDataOntem(Date dataHoje) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataHoje);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * This method checks if a String contains only numbers
	 */
	public static boolean containsOnlyNumbers(String str) {

		// It can't contain only numbers if it's null or empty...
		if (str == null || str.length() == 0)
			return false;

		for (int i = 0; i < str.length(); i++) {

			// If we find a non-digit character we return false.
			if (!Character.isDigit(str.charAt(i)))
				return false;
		}

		return true;
	}

	public static byte[] convertBufferedImageToArray(
			BufferedImage bufferedImage, String tipo) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {

			if (bufferedImage == null) {

				throw new IllegalArgumentException("buffer não pode ser 'null'");
			}

			ImageIO.write(bufferedImage, tipo, output);
			output.flush();
			byte[] imageInByte = output.toByteArray();
			output.close();
			return imageInByte;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

	}

	public static BufferedImage convertByteArrayToBufferedImage(byte[] byteArray) {
		InputStream input = new ByteArrayInputStream(byteArray);
		BufferedImage buffer = null;
		try {
			buffer = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer;

	}

	public static void invokeConcurrentThreadFactory(Runnable run) {
		ThreadFactory executor = Executors.defaultThreadFactory();
		Thread t = executor.newThread(run);
		t.start();
	}

	/**
	 * <p>
	 * Método utilizado para realizar a cópia dos valores dos atributos comuns
	 * entre dois objetos.
	 * </p>
	 * 
	 * @param dest
	 *            - objeto que receberá a cópia dos valores em seus atributos
	 * @param orig
	 *            - objeto que terá os valores de seus atributos copiados
	 * @exception IllegalAccessException
	 * @exception InvocationTargetException
	 * @see BeanUtilsBean.getInstance().copyProperties(dest, orig)
	 */
	public static void copyProperties(Object dest, Object orig)
			throws IllegalAccessException, InvocationTargetException {
		BeanUtilsBean.getInstance().copyProperties(dest, orig);
	}

	public String formatHorario(String hora) {
		if (hora == null) {
			return "";
		}
		return hora.replaceAll("(d{2})(d{2})", "$1h$2min");
	}

	/**
	 * Recupera um array contendo os nomes dos atributos de uma classe
	 * 
	 * @param clazz
	 *            do bean
	 * @return String[]
	 */
	public static String[] copyAllAttributesNames(Class<?> clazz) {

		List<String> attributesList = new ArrayList<String>();

		for (Field field : clazz.getDeclaredFields()) {
			attributesList.add(field.getName());
		}

		String[] attributes = Arrays.copyOf(attributesList.toArray(),
				attributesList.toArray().length, String[].class);

		return attributes;
	}

	/**
	 * Recupera um array contendo os nomes dos atributos de uma classe
	 * 
	 * @param clazz
	 * @param notAttributes
	 *            - atributos que não terão seus nomes copiados
	 * @return String[]
	 */
	public static String[] copyAllAttributesNames(Class<?> clazz,
			String... notAttributes) {

		List<String> attributesList = new ArrayList<String>();

		for (Field field : clazz.getDeclaredFields()) {

			boolean isAdd = true;

			for (String attribute : notAttributes) {
				if (field.getName().equalsIgnoreCase(attribute)) {
					isAdd = false;
					break;
				}
			}

			if (isAdd) {
				attributesList.add(field.getName());
			}
		}

		String[] attributes = Arrays.copyOf(attributesList.toArray(),
				attributesList.toArray().length, String[].class);

		return attributes;
	}


	/**
	 * Nova formatação para o telefone com 4 ou 5 dígitos no prefixo. <li>
	 * Parâmetro com o Telefone XXXXXXXXXX ou XXXXXXXXXXX <li>Retorno com o
	 * Telefone (XX) XXXX-XXXX ou (XX) XXXXX-XXXX
	 */
	public static String formatTelefone4ou5DigitosPrefixo(String telefone) {

		if (isNull(telefone)) {
			return null;
		}

		try {
			telefone = unformat(telefone);
			if (telefone.length() == 11) {
				telefone = "(" + telefone.substring(0, 2) + ")" + " "
						+ telefone.substring(2, 7) + "-"
						+ telefone.substring(7, 11);
			} else if (telefone.length() == 10) {
				telefone = "(" + telefone.substring(0, 2) + ")" + " "
						+ telefone.substring(2, 6) + "-"
						+ telefone.substring(6, 10);
			} else {
				telefone = telefone.substring(0, 4) + "-"
						+ telefone.substring(4, telefone.length());
			}
			return telefone;
		} catch (Exception e) {
			return null;
		}
	}

	public static String converterCaracterHTMLParaString(String texto){
		texto = texto.replaceAll("&nbsp;", " ");
		
		texto = texto.replaceAll("&Aacute;","Á");  
		texto = texto.replaceAll("&aacute;","á");  
		texto = texto.replaceAll("&Acirc;","Â");  
		texto = texto.replaceAll("&acirc; ","â");  
		texto = texto.replaceAll("&Agrave; ","À");  
		texto = texto.replaceAll("&agrave; ","à");  
		texto = texto.replaceAll("&Atilde;","Ã");  
		texto = texto.replaceAll("&atilde;","ã");
		
		texto = texto.replaceAll("&Eacute;","É");  
		texto = texto.replaceAll("&eacute;","é");  
		texto = texto.replaceAll("&Ecirc;","Ê");  
		texto = texto.replaceAll("&ecirc; ","ê");  
		texto = texto.replaceAll("&Egrave; ","È");  
		texto = texto.replaceAll("&egrave; ","è");  
		
		texto = texto.replaceAll("&Iacute;","Í");  
		texto = texto.replaceAll("&iacute;","é");  
		texto = texto.replaceAll("&Icirc;","Î");  
		texto = texto.replaceAll("&icirc; ","î");  
		texto = texto.replaceAll("&Igrave; ","Ì");  
		texto = texto.replaceAll("&igrave; ","ì");  
		
		texto = texto.replaceAll("&Oacute;","Ó");  
		texto = texto.replaceAll("&oacute;","ó");  
		texto = texto.replaceAll("&Ocirc;","Ô");  
		texto = texto.replaceAll("&ocirc;","ô");  
		texto = texto.replaceAll("&Ograve;","Ò");  
		texto = texto.replaceAll("&ograve;","ò");  
		texto = texto.replaceAll("&Otilde;","Õ");  
		texto = texto.replaceAll("&otilde;","õ");
		
		texto = texto.replaceAll("&Uacute;","Ú");  
		texto = texto.replaceAll("&uacute;","ú");  
		texto = texto.replaceAll("&Ucirc;","Û");  
		texto = texto.replaceAll("&ucirc;","û");  
		texto = texto.replaceAll("&Ugrave;","Ù");  
		texto = texto.replaceAll("&ugrave;","ù");  
		
		texto = texto.replaceAll("&Ccedil;","Ç");  
		texto = texto.replaceAll("&ccedil;","ç");  
		texto = texto.replaceAll("&Ntilde;","Ñ");  
		texto = texto.replaceAll("&ntilde;","ñ");   
		
		
		
        return texto;  
	}
	
}