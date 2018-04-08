package com.shinylife.smalltools.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class HttpResponse {

	private boolean DEBUG = Constants.DEBUG;

	private static ThreadLocal<DocumentBuilder> builders = new ThreadLocal<DocumentBuilder>() {
		@Override
		protected DocumentBuilder initialValue() {
			try {
				return DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
			} catch (ParserConfigurationException ex) {
				throw new ExceptionInInitializerError(ex);
			}
		}
	};

	private int statusCode;
	private Document responseAsDocument = null;
	private String responseAsString = null;
	private InputStream is;
	private HttpURLConnection con;
	private boolean streamConsumed = false;
	private String charset = "utf-8";

	public HttpResponse() {

	}

	public HttpResponse(HttpURLConnection con, String charset)
			throws IOException {
		this.con = con;
		this.statusCode = con.getResponseCode();
		this.charset = charset;
		if (null == (is = con.getErrorStream())) {
			is = con.getInputStream();
		}
		if (null != is && "gzip".equals(con.getContentEncoding())) {
			is = new GZIPInputStream(is);
		}
	}

	HttpResponse(String content) {
		this.responseAsString = content;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getResponseHeader(String name) {
		if (con != null)
			return con.getHeaderField(name);
		else
			return null;
	}

	public InputStream asStream() {
		if (streamConsumed) {
			throw new IllegalStateException("Stream has already been consumed.");
		}
		return is;
	}

	public String asString() throws HttpException {
		if (null == responseAsString) {
			BufferedReader br;
			try {
				InputStream stream = asStream();
				if (null == stream) {
					return null;
				}
				br = new BufferedReader(new InputStreamReader(stream, charset));
				StringBuffer buf = new StringBuffer();
				String line;
				while (null != (line = br.readLine())) {
					buf.append(line).append("\n");
				}
				this.responseAsString = buf.toString();
				log(responseAsString);
				stream.close();
				con.disconnect();
				streamConsumed = true;
			} catch (NullPointerException npe) {
				// don't remember in which case npe can be thrown
				throw new HttpException(npe.getMessage(), npe);
			} catch (IOException ioe) {
				throw new HttpException(ioe.getMessage(), ioe);
			} finally {
				try {
					con.disconnect();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		return responseAsString;
	}

	public Document asDocument() throws HttpException {
		if (null == responseAsDocument) {
			try {
				this.responseAsDocument = builders.get().parse(
						new ByteArrayInputStream(asString().getBytes("UTF-8")));
			} catch (SAXException saxe) {
				throw new HttpException(
						"The response body was not well-formed:\n"
								+ responseAsString, saxe);
			} catch (IOException ioe) {
				throw new HttpException(
						"There's something with the connection.", ioe);
			}
		}
		return responseAsDocument;
	}

	public InputStreamReader asReader() {
		try {
			return new InputStreamReader(is, charset);
		} catch (java.io.UnsupportedEncodingException uee) {
			return new InputStreamReader(is);
		}
	}

	public void disconnect() {
		con.disconnect();
	}

	private static Pattern escaped = Pattern.compile("&#([0-9]{3,5});");

	public static String unescape(String original) {
		Matcher mm = escaped.matcher(original);
		StringBuffer unescaped = new StringBuffer();
		while (mm.find()) {
			mm.appendReplacement(unescaped, Character.toString((char) Integer
					.parseInt(mm.group(1), 10)));
		}
		mm.appendTail(unescaped);
		return unescaped.toString();
	}

	@Override
	public String toString() {
		if (null != responseAsString) {
			return responseAsString;
		}
		return "Response{" + "statusCode=" + statusCode + ", response="
				+ responseAsDocument + ", responseString='" + responseAsString
				+ '\'' + ", is=" + is + ", con=" + con + '}';
	}

	private void log(String message) {
		if (DEBUG) {
			System.out.println("[" + new java.util.Date() + "]" + message);
		}
	}

	private void log(String message, String message2) {
		if (DEBUG) {
			log(message + message2);
		}
	}

	public String getResponseAsString() {
		return responseAsString;
	}

	public void setResponseAsString(String responseAsString) {
		this.responseAsString = responseAsString;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
