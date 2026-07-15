package org.nhindirect.xd.common;

import org.nhindirect.xd.common.type.DirectDocumentType;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

public class XDMFromCDA {

    private static final String SOURCE_ID = "2.16.840.1.113883.3.9900";

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: XDMFromCDA <ccda-file> [intended-recipient]");
            System.exit(1);
        }

        String ccdaFilePath = args[0];
        String intendedRecipient = args.length > 1 ? args[1] : null;

        Path ccdaPath = Paths.get(ccdaFilePath);
        byte[] ccdaBytes = Files.readAllBytes(ccdaPath);
        String ccdaStr = new String(ccdaBytes);

        DirectDocument2 doc = new DirectDocument2();
        doc.setData(ccdaBytes);
        doc.getMetadata().setMimeType("text/xml");
        doc.getMetadata().setUniqueId(generateOid());
        doc.getMetadata().setURI(ccdaPath.getFileName().toString());
        DirectDocumentType.CCD.parse(ccdaStr, doc.getMetadata());

        DirectDocuments.SubmissionSet submissionSet = new DirectDocuments.SubmissionSet();
        submissionSet.setSourceId(SOURCE_ID);
        submissionSet.setSubmissionTime(new Date());
        submissionSet.setUniqueId(generateOid());
        if (intendedRecipient != null) {
            submissionSet.getIntendedRecipient().add("||^^Internet^" + intendedRecipient);
        }
        DirectDocumentType.CCD.parse(ccdaStr, submissionSet);

        DirectDocuments documents = new DirectDocuments();
        documents.getDocuments().add(doc);
        documents.setSubmissionSet(submissionSet);

        File tempXdm = documents.toXdmPackage(UUID.randomUUID().toString()).toFile();

        String baseName = ccdaPath.getFileName().toString();
        int dotIdx = baseName.lastIndexOf('.');
        String outputName = (dotIdx > 0 ? baseName.substring(0, dotIdx) : baseName) + ".zip";
        Path outputPath = ccdaPath.getParent() != null
                ? ccdaPath.getParent().resolve(outputName)
                : Paths.get(outputName);

        Files.move(tempXdm.toPath(), outputPath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("XDM package created: " + outputPath.toAbsolutePath());
    }

    private static String generateOid() {
        UUID uuid = UUID.randomUUID();
        return "2.25." + new BigInteger(uuid.toString().replace("-", ""), 16);
    }
}
