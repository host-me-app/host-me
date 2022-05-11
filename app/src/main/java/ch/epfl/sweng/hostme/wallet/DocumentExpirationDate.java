package ch.epfl.sweng.hostme.wallet;

import ch.epfl.sweng.hostme.R;

public enum DocumentExpirationDate {

    RESIDENCE_PERMIT(R.id.textExpirationDate1, R.id.textExpirationDate_ResidencePermit, R.id.buttonPickDate_ResidencePermit),
    SALARY_SLIPS(R.id.textExpirationDate2, R.id.textExpirationDate_SalarySlips, R.id.buttonPickDate_SalarySlips),
    EXECUTION_OFFICE(R.id.textExpirationDate3, R.id.textExpirationDate_ExecOffice, R.id.buttonPickDate_ExecOffice);

    private final int descriptionFieldTextId;
    private final int expirationDateTextId;
    private final int pickDateButtonId;

    DocumentExpirationDate(int descriptionFieldTextId, int expirationDateTextId, int pickDateButtonId) {
        this.descriptionFieldTextId = descriptionFieldTextId;
        this.expirationDateTextId = expirationDateTextId;
        this.pickDateButtonId = pickDateButtonId;

    }

    public int getPickDateButtonId() {
        return pickDateButtonId;
    }

    public int getExpirationDateTextId() {
        return expirationDateTextId;
    }

    public int getDescriptionFieldTextId() {
        return descriptionFieldTextId;
    }
}
