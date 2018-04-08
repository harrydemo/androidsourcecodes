package com.shinylife.smalltools.helper;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

public class HttpPostParameter {
	String name;
	String value;
	private File file = null;

	private static final long serialVersionUID = -8708108746980739212L;

	public HttpPostParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public HttpPostParameter(String name, double value) {
		this.name = name;
		this.value = String.valueOf(value);
	}

	public HttpPostParameter(String name, int value) {
		this.name = name;
		this.value = String.valueOf(value);
	}

	public HttpPostParameter(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public File getFile() {
		return file;
	}

	public boolean isFile() {
		return null != file;
	}

	private static final String JPEG = "image/jpeg";
	private static final String GIF = "image/gif";
	private static final String PNG = "image/png";
	private static final String OCTET = "application/octet-stream";

	/**
	 * 
	 * @return content-type
	 */
	public String getContentType() {
		if (!isFile()) {
			throw new IllegalStateException("not a file");
		}
		String contentType;
		String extensions = file.getName();
		int index = extensions.lastIndexOf(".");
		if (-1 == index) {
			// no extension
			contentType = OCTET;
		} else {
			extensions = extensions.substring(extensions.lastIndexOf(".") + 1)
					.toLowerCase();
			if (extensions.length() == 3) {
				if ("gif".equals(extensions)) {
					contentType = GIF;
				} else if ("png".equals(extensions)) {
					contentType = PNG;
				} else if ("jpg".equals(extensions)) {
					contentType = JPEG;
				} else {
					contentType = OCTET;
				}
			} else if (extensions.length() == 4) {
				if ("jpeg".equals(extensions)) {
					contentType = JPEG;
				} else {
					contentType = OCTET;
				}
			} else {
				contentType = OCTET;
			}
		}
		return contentType;
	}

	public static boolean containsFile(HttpPostParameter[] params) {
		boolean containsFile = false;
		if (null == params) {
			return false;
		}
		for (HttpPostParameter param : params) {
			if (param.isFile()) {
				containsFile = true;
				break;
			}
		}
		return containsFile;
	}

	/* package */static boolean containsFile(List<HttpPostParameter> params) {
		boolean containsFile = false;
		for (HttpPostParameter param : params) {
			if (param.isFile()) {
				containsFile = true;
				break;
			}
		}
		return containsFile;
	}

	public static HttpPostParameter[] getParameterArray(String name,
			String value) {
		return new HttpPostParameter[] { new HttpPostParameter(name, value) };
	}

	public static HttpPostParameter[] getParameterArray(String name, int value) {
		return getParameterArray(name, String.valueOf(value));
	}

	public static HttpPostParameter[] getParameterArray(String name1,
			String value1, String name2, String value2) {
		return new HttpPostParameter[] { new HttpPostParameter(name1, value1),
				new HttpPostParameter(name2, value2) };
	}

	public static HttpPostParameter[] getParameterArray(String name1,
			int value1, String name2, int value2) {
		return getParameterArray(name1, String.valueOf(value1), name2,
				String.valueOf(value2));
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + value.hashCode();
		result = 31 * result + (file != null ? file.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof HttpPostParameter) {
			HttpPostParameter that = (HttpPostParameter) obj;

			if (file != null ? !file.equals(that.file) : that.file != null)
				return false;

			return this.name.equals(that.name) && this.value.equals(that.value);
		}
		return false;
	}

	@Override
	public String toString() {
		return "PostParameter{" + "name='" + name + '\'' + ", value='" + value
				+ '\'' + ", file=" + file + '}';
	}

	public int compareTo(Object o) {
		int compared;
		HttpPostParameter that = (HttpPostParameter) o;
		compared = name.compareTo(that.name);
		if (0 == compared) {
			compared = value.compareTo(that.value);
		}
		return compared;
	}

	public static String encodeParameters(HttpPostParameter[] httpParams) {
		if (null == httpParams) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < httpParams.length; j++) {
			if (httpParams[j].isFile()) {
				throw new IllegalArgumentException("parameter ["
						+ httpParams[j].name + "]should be text");
			}
			if (j != 0) {
				buf.append("&");
			}
			try {
				buf.append(URLEncoder.encode(httpParams[j].name, "UTF-8"))
						.append("=")
						.append(URLEncoder.encode(httpParams[j].value, "UTF-8"));
			} catch (java.io.UnsupportedEncodingException neverHappen) {
			}
		}
		return buf.toString();

	}
}
