package ch.epfl.sweng.hostme.wallet;

import ch.epfl.sweng.hostme.R;

public enum Document {

    RESIDENCE_PERMIT("Residence permit", "application/pdf", "documents/residence_permit/", "residence_permit", ".pdf", R.id.rp_import_button, R.id.rp_import_text, R.id.rp_download_button, R.id.rp_download_text, R.id.rp_check, 1),
    SALARY_SLIPS("Salary Slips", "application/pdf", "documents/salary_slips/", "salary_slips", ".pdf", R.id.sp_import_button, R.id.sp_import_text, R.id.sp_download_button, R.id.sp_download_text, R.id.sp_check, 2),
    EXECUTION_OFFICE("Extract from the Execution Office", "application/pdf", "documents/exec_office/", "exec_office", ".pdf", R.id.ex_import_button, R.id.ex_import_text, R.id.ex_download_button, R.id.ex_download_text, R.id.ex_check, 3);

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

    /**
     * get the document name
     *
     * @return the document name
     */
    public String getDocumentName() {
        return this.documentName;
    }

    /**
     * get the type
     *
     * @return type
     */
    public String getType() {
        return this.type;
    }

    /**
     * get the path
     *
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * get the file name
     *
     * @return the file name
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * get the file extension
     *
     * @return the file extension
     */
    public String getFileExtension() {
        return this.fileExtension;
    }

    /**
     * get the browse button id
     *
     * @return the browse button id
     */
    public int getButtonBrowseId() {
        return this.buttonBrowseId;
    }

    /**
     * get the browse button text id
     *
     * @return the browse button text id
     */
    public int getButtonBrowseTextId() {
        return this.buttonBrowseTextId;
    }

    /**
     * get the browse button download id
     *
     * @return the browse button download id
     */
    public int getButtonDownloadId() {
        return this.buttonDownloadId;
    }

    /**
     * get the browse button download text id
     *
     * @return the browse button download text id
     */
    public int getButtonDownloadTextId() {
        return this.buttonDownloadTextId;
    }

    /**
     * get check image id
     *
     * @return check image id
     */
    public int getCheckImageId() {
        return this.checkImageId;
    }

    /**
     * get the code permission
     *
     * @return the code permission
     */
    public int getCodePermission() {
        return codePermission;
    }
}
