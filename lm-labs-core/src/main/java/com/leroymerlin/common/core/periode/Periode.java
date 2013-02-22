package com.leroymerlin.common.core.periode;

import java.util.Calendar;

public class Periode {

    private static final String DATE_MUST_NOT_BE_NULL = "Date must not be null";
    private static final String THE_BEGIN_DATE_MUST_BE_SMALLER_OR_EQUALS_TO_END_DATE = "The begin date must be smaller or equals to end date : ";
    private final Calendar dateDebut;
    private final Calendar dateFin;

    public Periode(Calendar dateDebut, Calendar dateFin) throws PeriodInvalidException {
        if (dateDebut == null || dateFin == null)
            throw new PeriodInvalidException(DATE_MUST_NOT_BE_NULL);
        if (dateDebut.after(dateFin))
            throw new PeriodInvalidException(THE_BEGIN_DATE_MUST_BE_SMALLER_OR_EQUALS_TO_END_DATE + dateDebut.getTime() + " > " + dateFin.getTime());
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    /**
     * 1. overlaps
     *  |-------|                   |-------|
     *  pd1    pf1		=> same		pd1    pf1
     *      |----------|      |----------|
     *      pd2       pf2    pd2	 	pf2
     *
     * 2. overlaps
     *  |------------|				|----|
     *  pd1			pf1				pd1  pf1
     *  |----|			=> same		|------------|
     *  pd2 pf2						pd2			pf2
     *
     *  3. overlaps
     *  |------------|						|----|
     *  pd1			pf1	=> same				pd1  pf1
     *  		|----|				|------------|
     *  	   pd2  pf2				pd2			pf2
     *
     *  4. overlaps
     *  |------------|						 |----|
     *  pd1			pf1	=> same				pd1  pf1
     *               |----|		|------------|
     *				pd2  pf2    pd2			pf2
     *
     *  4 - strict (strictlyOverlapping = true). no overlaps
     *  |------------|						 |----|
     *  pd1			pf1	=> same				pd1  pf1
     *               |----|		|------------|
     *				pd2  pf2    pd2			pf2
     *
     *  5. no overlaps
     *  |--------|								|--------|
     *  pd1     pf1		=> same				   pd1		pf1
     *  			|------|		|--------|
     *  			pd2   pf2	   pd2      pf2
     */
    public static boolean isPeriodsOverlap(final Periode periode1, final Periode periode2) {
        return isPeriodsOverlap(periode1, periode2, false);
    }

    public static boolean isPeriodsOverlap(final Periode periode1, final Periode periode2, boolean strictlyOverlapping) {
        // case 1
        if (periode2.dateDebut.after(periode1.dateDebut) && periode2.dateDebut.before(periode1.dateFin)
                || periode1.dateDebut.after(periode2.dateDebut) && periode1.dateDebut.before(periode2.dateFin))
            return true;

        // case 2
        if (periode2.dateDebut.compareTo(periode1.dateDebut) == 0)
            return true;

        // case 3
        if (periode2.dateFin.compareTo(periode1.dateFin) == 0)
            return true;

        if(!strictlyOverlapping){
            // case 4
            if (periode1.dateFin.compareTo(periode2.dateDebut) == 0
                    || periode2.dateFin.compareTo(periode1.dateDebut) == 0)
                return true;
        }
        // case 5
        return false;
    }

    @Override
    public String toString() {
        StringBuffer value = new StringBuffer();
        value.append("[");
        value.append(this.dateDebut.getTime());
        value.append(" - ");
        value.append(this.dateFin.getTime());
        value.append("]");
        return value.toString();
    }
}
