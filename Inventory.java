public class Inventory { // java InventoryMain 5 10 } proper compilation
    private int size;

    public Inventory() {
        this.size = 0;
    }

    //sync
    public synchronized void addItem() {
        size++;
        System.out.println("Added. Inventory size: " + size);
    }

    //sync
    public synchronized void removeItem() {
        size--;
        System.out.println("Removed. Inventory size: " + size);
    }

    //sync
    public synchronized int getSize() {
        return size;
    }
}
