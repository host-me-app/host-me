package ch.epfl.sweng.hostme.wallet;

import ch.epfl.sweng.hostme.R;

public enum Document {

    RESIDENCE_PERMIT("Residence permit", "application/pdf", "documents/residence_permit/", "residence_permit", ".pdf", R.id.button_browse, R.id.button_download, R.id.check_residence_permit, 1);

    private final String documentName;
    private final String type;
    private final String path;
    private final String fileName;
    private final String fileExtension;
    private final int buttonBrowseId;
    private final int buttonDownloadId;
    private final int checkImageId;
    private final int codePermission;

    Document(String documentName, String type, String path, String fileName, String fileExtension, int buttonBrowseId, int buttonDownloadId, int checkImageId, int codePermission) {
        this.documentName = documentName;
        this.type = type;
        this.path = path;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.buttonBrowseId = buttonBrowseId;
        this.buttonDownloadId = buttonDownloadId;
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

    public int getButtonDownloadId() {
        return this.buttonDownloadId;
    }

    public int getCheckImageId() {
        return this.checkImageId;
    }

    public int getCodePermission() {
        return codePermission;
    }
}
