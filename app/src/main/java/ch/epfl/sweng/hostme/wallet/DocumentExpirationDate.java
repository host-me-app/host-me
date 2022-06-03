package ch.epfl.sweng.hostme.wallet;

import ch.epfl.sweng.hostme.R;

public enum DocumentExpirationDate {

    RESIDENCE_PERMIT(R.id.rp_date_desc_text, R.id.rp_date_text, R.id.rp_pick_date_button),
    SALARY_SLIPS(R.id.sp_date_desc_text, R.id.sp_date_text, R.id.sp_pick_date_button),
    EXECUTION_OFFICE(R.id.ex_date_desc_text, R.id.ex_date_text, R.id.ex_pick_date_button);

    private final int descriptionFieldTextId;
    private final int expirationDateTextId;
    private final int pickDateButtonId;

    DocumentExpirationDate(int descriptionFieldTextId, int expirationDateTextId, int pickDateButtonId) {
        this.descriptionFieldTextId = descriptionFieldTextId;
        this.expirationDateTextId = expirationDateTextId;
        this.pickDateButtonId = pickDateButtonId;
    }

    /**
     * get the pick date button id
     * @return the pick date button id
     */
    public int getPickDateButtonId() {
        return pickDateButtonId;
    }

    /**
     * get the expiration date button text id
     * @return the expiration date button text id
     */
    public int getExpirationDateTextId() {
        return expirationDateTextId;
    }

    /**
     * get the description field text id
     * @return the description field text id
     */
    public int getDescriptionFieldTextId() {
        return descriptionFieldTextId;
    }
}
