package ch.epfl.sweng.hostme.wallet;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import ch.epfl.sweng.hostme.R;

public class DocumentTest {

    @Test
    public void DocumentNameIsSame() {
        assertEquals("Residence permit", Document.RESIDENCE_PERMIT.getDocumentName());
    }

    @Test
    public void DocumentTypeIsSame() {
        assertEquals("application/pdf", Document.RESIDENCE_PERMIT.getType());
    }

    @Test
    public void DocumentPathIsSame() {
        assertEquals("documents/residence_permit/", Document.RESIDENCE_PERMIT.getPath());
    }

    @Test
    public void DocumentFileNameIsSame() {
        assertEquals("residence_permit", Document.RESIDENCE_PERMIT.getFileName());
    }

    @Test
    public void DocumentFileExtensionIsSame() {
        assertEquals(".pdf", Document.RESIDENCE_PERMIT.getFileExtension());
    }

    @Test
    public void DocumentButtonBrowseIdIsSame() {
        Assert.assertEquals(R.id.rp_import_button, Document.RESIDENCE_PERMIT.getButtonBrowseId());
    }

    @Test
    public void DocumentButtonBrowseTextIdIsSame() {
        Assert.assertEquals(R.id.rp_import_text, Document.RESIDENCE_PERMIT.getButtonBrowseTextId());
    }

    @Test
    public void DocumentButtonDownloadIdIsSame() {
        assertEquals(R.id.rp_download_button, Document.RESIDENCE_PERMIT.getButtonDownloadId());
    }

    @Test
    public void DocumentDownloadTextIdIsSame() {
        assertEquals(R.id.rp_download_text, Document.RESIDENCE_PERMIT.getButtonDownloadTextId());
    }


    @Test
    public void DocumentCheckImageIdIsSame() {
        assertEquals(R.id.rp_check, Document.RESIDENCE_PERMIT.getCheckImageId());
    }

    @Test
    public void DocumentCodePermissionIsSame() {
        assertEquals(1, Document.RESIDENCE_PERMIT.getCodePermission());
    }
}
