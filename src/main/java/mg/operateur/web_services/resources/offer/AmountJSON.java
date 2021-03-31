/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.offer;

import mg.operateur.business_logic.offer.Amount;
import mg.operateur.business_logic.offer.Application;

/**
 *
 * @author dodaa
 */
public class AmountJSON {
    private ApplicationJSON application;
    private double value;
    private UtilizationJSON utilization;
    private boolean isUnlimited;

    public boolean getIsUnlimited() {
        return isUnlimited;
    }

    public void setIsUnlimited(boolean isUnlimited) {
        this.isUnlimited = isUnlimited;
    }

    public UtilizationJSON getUtilization() {
        return utilization;
    }

    public void setUtilization(UtilizationJSON utilization) {
        this.utilization = utilization;
    }

    public ApplicationJSON getApplication() {
        return application;
    }

    public void setApplication(ApplicationJSON application) throws Exception {
        this.application = application;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) throws Exception {
        this.value = value;
    }
}
