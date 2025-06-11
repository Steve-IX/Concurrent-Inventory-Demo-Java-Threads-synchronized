public class InventoryMain {
    //java InventoryMain 5 10 } proper compilation
    
    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Use: java InventoryMain <number_of_adds> <number_of_removes>");
            return;
        }

        int numOfAdds = Integer.parseInt(args[0]);
        int numOfRemoves = Integer.parseInt(args[1]);
        
        Inventory inventory = new Inventory();

        Thread[] addThreads = new Thread[numOfAdds];
        for(int i = 0; i < numOfAdds; i++) {
            addThreads[i] = new Thread(() -> {
                inventory.addItem();
            });
            addThreads[i].start();
        }

        Thread[] removeThreads = new Thread[numOfRemoves];
        for(int i = 0; i < numOfRemoves; i++) {
            removeThreads[i] = new Thread(() -> {
                inventory.removeItem();
            });
            removeThreads[i].start();
        }

        // Joins all the required threads to print out
        for(Thread t : addThreads) {
            try {
                t.join();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(Thread t : removeThreads) {
            try {
                t.join();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Final inventory size: " + inventory.getSize());
    }
}

