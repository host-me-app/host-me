package ch.epfl.sweng.hostme.wallet;

import ch.epfl.sweng.hostme.R;

public enum DocumentExpirationDate {

    RESIDENCE_PERMIT(R.id.rpDateDescText, R.id.rpDateText, R.id.rpPickDateButton),
    SALARY_SLIPS(R.id.spDateDescText, R.id.spDateText, R.id.spPickDateButton),
    EXECUTION_OFFICE(R.id.exDateDescText, R.id.exDateText, R.id.exPickDateButton);

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
