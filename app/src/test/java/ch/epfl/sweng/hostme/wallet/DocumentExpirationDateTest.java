package ch.epfl.sweng.hostme.wallet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.wallet.DocumentExpirationDate;

public class DocumentExpirationDateTest {

    @Test
    public void ResidenceDescriptionFieldTextIdIsSame() {
        assertEquals(R.id.rp_date_desc_text, DocumentExpirationDate.RESIDENCE_PERMIT.getDescriptionFieldTextId());
    }

    @Test
    public void ResidenceExpirationDateTextIdIsSame() {
        assertEquals(R.id.rp_date_text, DocumentExpirationDate.RESIDENCE_PERMIT.getExpirationDateTextId());
    }

    @Test
    public void ResidencePickDateButtonIdIsSame() {
        assertEquals(R.id.rp_pick_date_button, DocumentExpirationDate.RESIDENCE_PERMIT.getPickDateButtonId());
    }


    @Test
    public void SalaryDescriptionFieldTextIdIsSame() {
        assertEquals(R.id.sp_date_desc_text, DocumentExpirationDate.SALARY_SLIPS.getDescriptionFieldTextId());
    }

    @Test
    public void SalaryExpirationDateTextIdIsSame() {
        assertEquals(R.id.sp_date_text, DocumentExpirationDate.SALARY_SLIPS.getExpirationDateTextId());
    }

    @Test
    public void SalaryPickDateButtonIdIsSame() {
        assertEquals(R.id.sp_pick_date_button, DocumentExpirationDate.SALARY_SLIPS.getPickDateButtonId());
    }


    @Test
    public void ExeOfficeDescriptionFieldTextIdIsSame() {
        assertEquals(R.id.ex_date_desc_text, DocumentExpirationDate.EXECUTION_OFFICE.getDescriptionFieldTextId());
    }

    @Test
    public void ExeOfficeExpirationDateTextIdIsSame() {
        assertEquals(R.id.ex_date_text, DocumentExpirationDate.EXECUTION_OFFICE.getExpirationDateTextId());
    }

    @Test
    public void ExeOfficePickDateButtonIdIsSame() {
        assertEquals(R.id.ex_pick_date_button, DocumentExpirationDate.EXECUTION_OFFICE.getPickDateButtonId());
    }


}
