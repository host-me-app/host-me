package ch.epfl.sweng.hostme.wallet;

import ch.epfl.sweng.hostme.R;

public enum Document {

    RESIDENCE_PERMIT("Residence permit", "application/pdf", "documents/residence_permit/", "residence_permit", ".pdf", R.id.rpImportButton, R.id.rpImportText, R.id.rpDownloadButton, R.id.rpDownloadText, R.id.rpcheck, 1),
    SALARY_SLIPS("Salary Slips", "application/pdf", "documents/salary_slips/", "salary_slips", ".pdf", R.id.spImportButton, R.id.spImportText, R.id.spDownloadButton, R.id.spDownloadText, R.id.spcheck, 2),
    EXECUTION_OFFICE("Extract from the Execution Office", "application/pdf", "documents/exec_office/", "exec_office", ".pdf", R.id.exImportButton, R.id.exImportText, R.id.exDownloadButton, R.id.exDownloadText, R.id.excheck, 3);

    private final String documentName;
    private final String type;
    private final String path;
    private final String fileName;
    private final String fileExtension;
    private final int buttonBrowseId;
    private final int buttonBrowseTextId;
    private final int buttonDownloadId;
    private final int buttonDownloadTextId;
    private final int checkImageId;
    private final int codePermission;

    Document(String documentName, String type, String path, String fileName, String fileExtension, int buttonBrowseId, int buttonBrowseTextId, int buttonDownloadId, int buttonDownloadTextId, int checkImageId, int codePermission) {
        this.documentName = documentName;
        this.type = type;
        this.path = path;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.buttonBrowseId = buttonBrowseId;
        this.buttonBrowseTextId = buttonBrowseTextId;
        this.buttonDownloadId = buttonDownloadId;
        this.buttonDownloadTextId = buttonDownloadTextId;
        this.checkImageId = checkImageId;
        this.codePermission = codePermission;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public String getType() {
        return this.type;
    }

    public String getPath() {
        return this.path;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFileExtension() {
        return this.fileExtension;
    }

    public int getButtonBrowseId() {
        return this.buttonBrowseId;
    }

    public int getButtonBrowseTextId() {
        return this.buttonBrowseTextId;
    }

    public int getButtonDownloadId() {
        return this.buttonDownloadId;
    }

    public int getButtonDownloadTextId() {
        return this.buttonDownloadTextId;
    }

    public int getCheckImageId() {
        return this.checkImageId;
    }

    public int getCodePermission() {
        return codePermission;
    }
}
