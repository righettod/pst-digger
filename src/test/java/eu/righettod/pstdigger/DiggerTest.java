package eu.righettod.pstdigger;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for digger.
 *
 */
@SuppressWarnings("static-method")
public class DiggerTest {

	private static final File TEST_HOME = new File("target/wkp");

	/**
	 * Global test cases init
	 */
	@BeforeClass
	public static void globalInit() {
		FileUtils.deleteQuietly(TEST_HOME);
		Assert.assertTrue(TEST_HOME.mkdirs());
	}

	/**
	 * Test case using all options including attachments dumping.<br>
	 * One keyword searched
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFullOptionsSingleKeyword() throws Exception {
		// Prepare test workspace
		boolean caseSensitive = true;
		boolean dumpAttachments = true;
		String searchedKeyword = "password";
		File pst = new File("src/test/resources/test.pst");
		File output = new File("target/wkp/outTest01");
		output.mkdirs();
		// Run test
		int c = new Digger(caseSensitive, searchedKeyword, pst, output, dumpAttachments).dig();
		// Validate test result
		String mailsFoundLog = new String(Files.readAllBytes(new File(output, "interesting-mails.txt").toPath()));
		Assert.assertTrue(c == 2);
		Assert.assertTrue(new File(output, "2097348_0_test.docx").exists());
		Assert.assertTrue(new File(output, "2097348_1_test.pdf").exists());
		Assert.assertTrue(output.listFiles().length == 3);
		Assert.assertTrue(mailsFoundLog.contains("MAIL ID:2097188"));
		Assert.assertTrue(mailsFoundLog.contains("MAIL ID:2097348"));
	}

	/**
	 * Test case using case no sensitive without attachments dumping.<br>
	 * One keyword searched
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNoCaseSensitiveWithoutAttachmentsDumpingSingleKeyword() throws Exception {
		// Prepare test workspace
		boolean caseSensitive = false;
		boolean dumpAttachments = false;
		String searchedKeyword = "password";
		File pst = new File("src/test/resources/test.pst");
		File output = new File("target/wkp/outTest02");
		output.mkdirs();
		// Run test
		int c = new Digger(caseSensitive, searchedKeyword, pst, output, dumpAttachments).dig();
		// Validate test result
		String mailsFoundLog = new String(Files.readAllBytes(new File(output, "interesting-mails.txt").toPath()));
		Assert.assertTrue(c == 3);
		Assert.assertTrue(output.listFiles().length == 1);
		Assert.assertTrue(mailsFoundLog.contains("MAIL ID:2097220"));
		Assert.assertTrue(mailsFoundLog.contains("MAIL ID:2097188"));
		Assert.assertTrue(mailsFoundLog.contains("MAIL ID:2097348"));
	}

	/**
	 * Test case using case sensitive without attachments dumping.<br>
	 * Two keyword searched
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCaseSensitiveWithoutAttachmentsDumpingMultiKeyword() throws Exception {
		// Prepare test workspace
		boolean caseSensitive = true;
		boolean dumpAttachments = false;
		String searchedKeyword = "Login|Hello";
		File pst = new File("src/test/resources/test.pst");
		File output = new File("target/wkp/outTest03");
		output.mkdirs();
		// Run test
		int c = new Digger(caseSensitive, searchedKeyword, pst, output, dumpAttachments).dig();
		// Validate test result
		String mailsFoundLog = new String(Files.readAllBytes(new File(output, "interesting-mails.txt").toPath()));
		Assert.assertTrue(c == 2);
		Assert.assertTrue(output.listFiles().length == 1);
		Assert.assertTrue(mailsFoundLog.contains("MAIL ID:2097284"));
		Assert.assertTrue(mailsFoundLog.contains("MAIL ID:2097252"));
	}
}
