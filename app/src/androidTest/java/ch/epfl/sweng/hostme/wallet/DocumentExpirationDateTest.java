package ch.epfl.sweng.hostme.wallet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.sweng.hostme.R;

public class DocumentExpirationDateTest {

    @Test
    public void ResidenceDescriptionFieldTextIdIsSame() {
        assertEquals(R.id.textExpirationDate1, DocumentExpirationDate.RESIDENCE_PERMIT.getDescriptionFieldTextId());
    }

    @Test
    public void ResidenceExpirationDateTextIdIsSame() {
        assertEquals(R.id.textExpirationDate_ResidencePermit, DocumentExpirationDate.RESIDENCE_PERMIT.getExpirationDateTextId());
    }

    @Test
    public void ResidencePickDateButtonIdIsSame() {
        assertEquals(R.id.buttonPickDate_ResidencePermit, DocumentExpirationDate.RESIDENCE_PERMIT.getPickDateButtonId());
    }


    @Test
    public void SalaryDescriptionFieldTextIdIsSame() {
        assertEquals(R.id.textExpirationDate2, DocumentExpirationDate.SALARY_SLIPS.getDescriptionFieldTextId());
    }

    @Test
    public void SalaryExpirationDateTextIdIsSame() {
        assertEquals(R.id.textExpirationDate_SalarySlips, DocumentExpirationDate.SALARY_SLIPS.getExpirationDateTextId());
    }

    @Test
    public void SalaryPickDateButtonIdIsSame() {
        assertEquals(R.id.buttonPickDate_SalarySlips, DocumentExpirationDate.SALARY_SLIPS.getPickDateButtonId());
    }


    @Test
    public void ExeOfficeDescriptionFieldTextIdIsSame() {
        assertEquals(R.id.textExpirationDate3, DocumentExpirationDate.EXECUTION_OFFICE.getDescriptionFieldTextId());
    }

    @Test
    public void ExeOfficeExpirationDateTextIdIsSame() {
        assertEquals(R.id.textExpirationDate_ExecOffice, DocumentExpirationDate.EXECUTION_OFFICE.getExpirationDateTextId());
    }

    @Test
    public void ExeOfficePickDateButtonIdIsSame() {
        assertEquals(R.id.buttonPickDate_ExecOffice, DocumentExpirationDate.EXECUTION_OFFICE.getPickDateButtonId());
    }


}
