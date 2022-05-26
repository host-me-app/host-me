package ch.epfl.sweng.hostme.wallet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.sweng.hostme.R;

public class DocumentExpirationDateTest {

    @Test
    public void ResidenceDescriptionFieldTextIdIsSame() {
        assertEquals(R.id.rpDateDescText, DocumentExpirationDate.RESIDENCE_PERMIT.getDescriptionFieldTextId());
    }

    @Test
    public void ResidenceExpirationDateTextIdIsSame() {
        assertEquals(R.id.rpDateText, DocumentExpirationDate.RESIDENCE_PERMIT.getExpirationDateTextId());
    }

    @Test
    public void ResidencePickDateButtonIdIsSame() {
        assertEquals(R.id.rpPickDateButton, DocumentExpirationDate.RESIDENCE_PERMIT.getPickDateButtonId());
    }


    @Test
    public void SalaryDescriptionFieldTextIdIsSame() {
        assertEquals(R.id.spDateDescText, DocumentExpirationDate.SALARY_SLIPS.getDescriptionFieldTextId());
    }

    @Test
    public void SalaryExpirationDateTextIdIsSame() {
        assertEquals(R.id.spDateText, DocumentExpirationDate.SALARY_SLIPS.getExpirationDateTextId());
    }

    @Test
    public void SalaryPickDateButtonIdIsSame() {
        assertEquals(R.id.spPickDateButton, DocumentExpirationDate.SALARY_SLIPS.getPickDateButtonId());
    }


    @Test
    public void ExeOfficeDescriptionFieldTextIdIsSame() {
        assertEquals(R.id.exDateDescText, DocumentExpirationDate.EXECUTION_OFFICE.getDescriptionFieldTextId());
    }

    @Test
    public void ExeOfficeExpirationDateTextIdIsSame() {
        assertEquals(R.id.exDateText, DocumentExpirationDate.EXECUTION_OFFICE.getExpirationDateTextId());
    }

    @Test
    public void ExeOfficePickDateButtonIdIsSame() {
        assertEquals(R.id.exPickDateButton, DocumentExpirationDate.EXECUTION_OFFICE.getPickDateButtonId());
    }


}
