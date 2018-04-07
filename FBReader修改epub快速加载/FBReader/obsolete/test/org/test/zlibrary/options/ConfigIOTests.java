package org.test.zlibrary.options;

import java.io.File;

import org.geometerplus.zlibrary.core.options.*;
import org.geometerplus.zlibrary.core.config.ZLConfigManager;

/**
 * �������� ����� �� ZLConfigReader
 * 
 * @author �������������
 */
public class ConfigIOTests extends ZLOptionTests {

	final static String outputPath = "test/org/test/zlibrary/options/examples/output/";
	ConfigIOTests() {
		super("test/org/test/zlibrary/options/examples/", outputPath);
	}

	public void tearDown() {
		new File("test/org/test/zlibrary/options/examples/output/new_category.xml").delete();
		new File("test/org/test/zlibrary/options/examples/output/options.xml").delete();
	}
	// ���� �� ������ �������� ������������ ��������
	public void test01() {
		assertEquals(getConfig().getValue("Options", "KeyDelay", "100"), 0 + "");
	}

	// ������ �������� � �����, � ������� �� �������
	public void test02() {
		ZLIntegerOption option = new ZLIntegerOption("options", "Options",
				"KeyDelay", 100);
		option.setValue(1223);
		assertEquals(getConfig().getValue("Options", "KeyDelay", "100"), 1223 + "");
	}

	// ������ � ����� ��������, ����� ������ � ����, ������ ������
	// ����� � ������� ��� ����������
	public void test03() {
		ZLIntegerOption option = new ZLIntegerOption("options", "Options",
				"KeyDelay", 100);
		option.setValue(123);
		ZLConfigManager.getInstance().saveAll();

		// getConfig().unsetValue("Options", "KeyDelay");

		/*
		try {
			ZLConfigReaderFactory.createConfigReader(
					"test/org/test/" + "zlibrary/options/examples/output/")
					.read();
		} catch (IndexOutOfBoundsException e) {

		}
		*/
		assertEquals(getConfig().getValue("Options", "KeyDelay", "100"), 123 + "");
	}

	// ������ ���� �� unset
	public void test04() {
		/*
		ZLConfigReaderFactory.createConfigReader(
				"test/org/test/" + "zlibrary/options/examples/output/").read();
		getConfig().unsetValue("Options", "KeyDelay");
		*/
		assertEquals(getConfig().getValue("Options", "KeyDelay", "meaningless"),
				"meaningless");
	}

	// ���� �� set ��������� ���
	public void test05() {
		getConfig().setValue("Options", "KeyDelay", "VALUE1", "new_category");
		getConfig().setValue("Options", "KeyDelay", "VALUE2", "options");
		assertEquals(getConfig().getValue("Options", "KeyDelay", "meaningless"),
				"VALUE2");
	}

	// ���� �� ��������� ����� ���������
	public void test06() {
		getConfig().setValue("Options", "KeyDelay", "VALUE", "new_category");
		ZLConfigManager.getInstance().saveAll();
		String[] outDirFiles = new File(outputPath).list();
		for (int i = 0; i < outDirFiles.length; i++) {
			if (outDirFiles[i].equals("new_category.xml")) {
				return;
			}
		}
		fail("new_category.xml expected to exist");
	}
}
