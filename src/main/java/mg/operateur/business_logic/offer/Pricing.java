/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

/**
 *
 * @author dodaa
 */
public class Pricing {
    private double intra;
    private double extra;

    public Pricing() {
    }

    public Pricing(double intra, double extra) {
        this.intra = intra;
        this.extra = extra;
    }

    public double getIntra() {
        return intra;
    }

    public void setIntra(double intra) {
        this.intra = intra;
    }

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }
}
