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
public class Utilization {
    private Price intra;
    private Price extra;

    public Utilization() {
    }

    public Utilization(mg.operateur.business_logic.offer.Price intra, mg.operateur.business_logic.offer.Price extra) {
        this.intra = intra;
        this.extra = extra;
    }
    
    public Price getIntra() {
        return intra;
    }

    public void setIntra(Price intra) {
        this.intra = intra;
    }

    public Price getExtra() {
        return extra;
    }

    public void setExtra(Price extra) {
        this.extra = extra;
    }
}
