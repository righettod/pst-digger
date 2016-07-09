package eu.righettod.pstdigger;

import java.io.File;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Program entry point
 *
 */
public class EntryPoint {

	private static final Logger LOG = LoggerFactory.getLogger(EntryPoint.class);

	@Option(name = "-i", usage = "Perform search of terms in case sensitive way", required = false)
	private boolean caseSensitive = false;

	@Option(name = "-k", usage = "Keyword to search in mail (subject/body), separated by a pipe '|'", required = true)
	private String searchedKeywords = null;

	@Option(name = "-f", usage = "PST file to analyze (absolute or relative path)", required = true)
	private File pst = null;

	@Option(name = "-o", usage = "Folder in which store copy of interesting mails containing the keywords specified", required = false)
	private File output = new File("out");

	@Option(name = "-a", usage = "Dump linked attachments for interesting mails found", required = false)
	private boolean dumpAttachments = false;

	/**
	 * Entry point
	 * 
	 * @param args Command line
	 */
	public static void main(String[] args) {
		new EntryPoint().doMain(args);
	}

	/**
	 * Perform the job
	 * 
	 * @param args Command line
	 */
	@SuppressWarnings("boxing")
	private void doMain(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);
		try {
			// Parse the arguments
			parser.getProperties().withUsageWidth(120);
			parser.parseArgument(args);

			// Create ouptut folder
			if ((this.output != null) && !this.output.exists()) {
				this.output.mkdirs();
			}

			// Dig the file
			LOG.info("Start digging on file '{}' ...", this.pst);
			int count = new Digger(this.caseSensitive, this.searchedKeywords, this.pst, this.output, this.dumpAttachments).dig();
			LOG.info("Digging finished, {} interesting mails found, see {} for details !", count, this.output.getAbsolutePath());
		}
		catch (CmdLineException cmde) {
			LOG.error("Bad syntax !");
			LOG.error(" ");
			LOG.error("Usage:");
			parser.printUsage(System.out);
			LOG.error(" ");
			String baseCmd = "java -jar pst-digger.jar";
			LOG.error("Example using required parameters:");
			LOG.error(baseCmd + parser.printExample(OptionHandlerFilter.REQUIRED));
			LOG.error(" ");
			LOG.error("Example using all parameters:");
			LOG.error(baseCmd + parser.printExample(OptionHandlerFilter.ALL));
			LOG.error(" ");
		}
		catch (Exception e) {
			LOG.error("Digging failed !", e);
		}
	}

}
