package eu.righettod.pstdigger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pff.PSTAttachment;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;

/**
 * Class in charge of performing the processing of the PST file.
 *
 */
public class Digger {

	private static final Logger LOG = LoggerFactory.getLogger(Digger.class);

	private boolean caseSensitive = false;
	private boolean dumpAttachments = false;
	private List<String> searchedKeywords = null;
	private File pst = null;
	private File output = null;

	/**
	 * Constructor
	 *
	 * @param caseSensitive Perform search of terms in case sensitive way
	 * @param searchedKeywords Keywords to search in mail, separated by a pipe '|'
	 * @param pst PST file to analyze
	 * @param output Folder in which store copy of mails containing the keywords specified
	 * @param dumpAttachments Dump linked attachments for interesting emials found
	 */
	public Digger(boolean caseSensitive, String searchedKeywords, File pst, File output, boolean dumpAttachments) {
		super();
		if ((pst == null) || !pst.exists() || !pst.isFile() || !pst.canRead()) {
			throw new IllegalArgumentException("The PST file must be a valid readable file !");
		}
		if ((output == null) || !output.exists() || !output.isDirectory() || !pst.canWrite()) {
			throw new IllegalArgumentException("The output directory must be a valid folder !");
		}
		if ((searchedKeywords == null) || (searchedKeywords.trim().length() == 0)) {
			throw new IllegalArgumentException("A least one keyword must be specified");
		}
		this.caseSensitive = caseSensitive;
		this.searchedKeywords = Arrays.asList(searchedKeywords.split("\\|"));
		this.pst = pst;
		this.output = output;
		this.dumpAttachments = dumpAttachments;
	}

	/**
	 * Perform the search using context specified
	 * 
	 * @return Number of interesting mails found
	 * @throws IOException
	 * @throws PSTException
	 * @throws FileNotFoundException
	 */
	public int dig() throws FileNotFoundException, PSTException, IOException {
		PSTFile pstFile = new PSTFile(this.pst);
		int interestingMailCount = processFolder(pstFile.getRootFolder());
		return interestingMailCount;
	}

	/**
	 * Perform analysis of a folder in the PST file
	 * 
	 * @param folder PST folder object
	 * @return Number of interesting mails found
	 * @throws IOException
	 * @throws PSTException
	 */
	private int processFolder(PSTFolder folder) throws PSTException, IOException {
		int interestingMailCount = 0;
		// Process mails in the current folder
		if (folder.getEmailCount() > 0) {
			PSTMessage mail = null;
			do {
				// Get and process the mail
				mail = (PSTMessage) folder.getNextChild();
				if (processMessage(mail)) {
					interestingMailCount++;
				}

			}
			while (mail != null);
		}
		// Process sub folders using recursive call
		if (folder.hasSubfolders()) {
			Vector<PSTFolder> childFolders = folder.getSubFolders();
			for (PSTFolder childFolder : childFolders) {
				interestingMailCount += processFolder(childFolder);
			}
		}

		return interestingMailCount;
	}

	/**
	 * Perform the analysis of a message
	 * 
	 * @param msg PST message object
	 * @return Flag indicating if the mail was interesting or not
	 * @throws IOException
	 * @throws PSTException
	 */
	@SuppressWarnings("boxing")
	private boolean processMessage(PSTMessage msg) throws IOException, PSTException {
		boolean isInteresting = false;
		String bodyInPlainTextForWork = "";
		String subjectForWork = "";
		String keywordForWork = null;
		String filename = null;
		File interestingMailLog = new File(this.output, "interesting-mails.txt");
		PSTAttachment attach = null;

		// Quick exit
		if (msg == null) {
			return isInteresting;
		}

		// Fill subject and body values used for the search according to case sensitive flag
		if (msg.getBody() != null) {
			bodyInPlainTextForWork = (this.caseSensitive) ? msg.getBody().trim() : msg.getBody().toLowerCase().trim();
		}
		if (msg.getSubject() != null) {
			subjectForWork = (this.caseSensitive) ? msg.getSubject().trim() : msg.getSubject().toLowerCase().trim();
		}

		// Quick exit
		if (bodyInPlainTextForWork.isEmpty() && subjectForWork.isEmpty()) {
			return isInteresting;
		}

		for (String keyword : this.searchedKeywords) {
			keywordForWork = (this.caseSensitive) ? keyword : keyword.toLowerCase();
			if (bodyInPlainTextForWork.contains(keywordForWork) || subjectForWork.contains(keywordForWork)) {
				// Dump mail subject and body
				isInteresting = true;
				StringBuilder buffer = new StringBuilder();
				buffer.append("\n\r**********************\n\r");
				buffer.append("** MAIL ID:").append(msg.getDescriptorNodeId()).append("\n\r");
				buffer.append("**********************\n\r");
				buffer.append("SUBJECT:\n\r");
				buffer.append(msg.getSubject());
				buffer.append("\n\rBODY PLAIN TEXT:\n\r");
				buffer.append(msg.getBody()).append("\n\r");

				// Dump attachments
				if (this.dumpAttachments && (msg.getNumberOfAttachments() > 0)) {
					buffer.append("\n\rATTACHEMENT INFOS:\n\r");
					for (int i = 0; i < msg.getNumberOfAttachments(); i++) {
						// Process attachement in mode "Save as much attachment as you can"
						// So do not stop if we meet an error on one attachment extraction...
						try {
							attach = msg.getAttachment(i);
							// Get attachment filename and prefix it with mail ID in order to auto sort files in folder
							filename = msg.getDescriptorNodeId() + "_" + i + "_" + attach.getLongFilename();
							if (filename.endsWith("_")) {
								filename += ".txt";
							}
							buffer.append(attach.getLongFilename());
							buffer.append(" => ");
							buffer.append(filename);
							buffer.append("\n\r");
							// Save attachment
							Files.copy(attach.getFileInputStream(), new File(this.output, filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
						}
						catch (Exception e) {
							LOG.warn("[MAIL ID:'{}' - TITLE:'{}'] => Cannot extract attachment from email: {}", msg.getDescriptorNodeId(), subjectForWork, e.getMessage());
						}
					}
				}
				Files.write(interestingMailLog.toPath(), buffer.toString().getBytes(), interestingMailLog.exists() ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);

				// Stop because we have found one keyword hit
				break;
			}
		}

		return isInteresting;
	}

}
