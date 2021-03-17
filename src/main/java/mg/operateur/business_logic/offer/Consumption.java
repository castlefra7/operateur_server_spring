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
public class Consumption {
    private int id;
    private int customerId;
    private Application application;
    private SmartDate date;
    private Amount consumed;

    public Consumption(int id, int customerId, Application application, SmartDate date, Amount conumed) throws Exception {
        setId(id);
        setCustomerId(customerId);
        setApplication(application);
        setDate(date);
        setConsumed(conumed);
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public Application getApplication() { return application; }
    public SmartDate getDate() { return date; }
    public Amount getConsumed() { return consumed; }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setApplication(Application application) throws Exception {
        if (application == null)
            throw new Exception("application is required");
        this.application = application;
    }

    public void setDate(SmartDate date) throws Exception {
        if (date == null)
            throw new Exception("date is required");
        this.date = date;
    }

    public void setConsumed(Amount consumed) throws Exception {
        if (consumed == null)
            throw new Exception("consumed is required");
        this.consumed = consumed;
    }
}
