package c482.Model;

public class OutsourcedPart extends Part {
    private String companyName;

    /**
     * Creates an instance of an OutsorcedPart
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public OutsourcedPart(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Sets the companyName for a part
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Returns the parts companyName
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }
}