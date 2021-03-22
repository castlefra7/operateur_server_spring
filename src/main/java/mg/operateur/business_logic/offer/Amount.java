/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import mg.operateur.gen.InvalidAmountException;

/**
 *
 * @author dodaa
 */

// Facebook 1000Mo; Appel 20 min; etc
public class Amount {
    private Application application;
    private double value;
    private Utilization utilization;

    public Utilization getUtilization() {
        return utilization;
    }

    public void setUtilization(Utilization utilization) {
        this.utilization = utilization;
    }

    public Amount() {
    }

    public Amount(Application application, double value, Utilization utilization) throws Exception {
        setApplication(application);
        setValue(value);
        setUtilization(utilization);
    }
    
    public Amount (Amount amount) throws Exception {
        setApplication(amount.getApplication());
        setValue(amount.getValue());       
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) throws Exception {
        if (application == null)
            throw new Exception("application is required");
        this.application = application;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) throws InvalidAmountException {
        if (value < 0)
            throw new InvalidAmountException("value must not be negative");
        this.value = value;
    }
    
    public Amount add(Amount amount) throws Exception {
        if (!amount.getApplication().equals(this.application))
            throw new Exception("Amounts of different applications cannot be added");
        double val = this.value + amount.getValue();
        return new Amount(application, val, amount.getUtilization());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Amount other = (Amount) obj;
        if (!this.application.equals(other.application)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Amount{" + "\t\napplication=" + application + ", \t\nvalue=" + value + '}';
    }
}
