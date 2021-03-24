/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.offer;

/**
 *
 * @author dodaa
 */
public class PriceJSON {
    private double price;
    private String per;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }
}
