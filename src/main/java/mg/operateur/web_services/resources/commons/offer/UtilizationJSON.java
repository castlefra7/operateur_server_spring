/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.commons.offer;

/**
 *
 * @author dodaa
 */
public class UtilizationJSON {
    private PriceJSON intra;
    private PriceJSON extra;

    public PriceJSON getIntra() {
        return intra;
    }

    public void setIntra(PriceJSON intra) {
        this.intra = intra;
    }

    public PriceJSON getExtra() {
        return extra;
    }

    public void setExtra(PriceJSON extra) {
        this.extra = extra;
    }
}
