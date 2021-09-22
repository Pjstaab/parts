package c482.Model;

public class InHousePart extends Part {
    private int machineId;

    /**
     * Creates an instance of an InHousePart
     *
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId
     */
    public InHousePart(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Sets the machineId for a part
     *
     * @param machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Returns the parts machineId
     *
     * @return machineId
     */
    public int getMachineId() {
        return machineId;
    }
}