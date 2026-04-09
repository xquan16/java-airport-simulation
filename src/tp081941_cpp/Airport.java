    
package tp081941_cpp;
import java.util.concurrent.Semaphore;

// This class controls access to the Runway, Gates, and Trucks.
public class Airport {

    // resources
    private final Semaphore gates = new Semaphore(3); 
    private final Semaphore truck = new Semaphore(1);
    
    private boolean runwayFree = true;  // true = available ; false = non-available
    private boolean emergency = false;   // true = emergency ; false = non-emergency

    // collect data for statistics
    private int totalPlanesServed = 0;
    private int totalPassengersBoarded = 0;
    private long totalWaitTime = 0;
    private long maxWaitTime = 0;
    private long minWaitTime = Long.MAX_VALUE;

    // 1. GATE MANAGEMENT(must get gate permission then only can request runway)
    public void requireGate(String name) {
        try {
            System.out.println("  ATC: " + name + " is requesting a gate...");
            gates.acquire(); 
            System.out.println("  ATC: Gate assigned to " + name + ".");
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        }
    }

   
    public void releaseGate(String name) {
        System.out.println("  ATC: " + name + " is returning the gate ticket.");
        gates.release();
    }

    
    // 2. RUNWAY MANAGEMENT (Emergency Aware)
    public synchronized void requestRunway(String name, boolean isEmergency) {
        // check is the request's plane in emergency or not
        if (isEmergency) {
            System.out.println("  ATC: EMERGENCY! Priority landing requested for " + name + " ...");
            emergency = true;
        }

        // runway non-available OR (have other plane emergency AND request's plane not emergency) = need to wait()
        while (runwayFree == false || (emergency == true && isEmergency == false)) {
            try {
                if (!isEmergency) {
                    System.out.println("  ATC: " + name + " must wait (Runway busy or Emergency in progress).");
                }
                wait(); 
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }
        }
        
        // if reach here mean runway available, so update to non-available, request's plane get runway 
        runwayFree = false;
        System.out.println("  ATC: Runway granted to " + name + ".");
    }

    public synchronized void releaseRunway(String name, boolean wasEmergency) {
        runwayFree = true;
        notifyAll();
        System.out.println("  ATC: Runway is now free.");
    }

    
    // 3. REFUELING TRUCK
    public void requestRefuelTruck(String name) {
        try {
            System.out.println("  ATC: " + name + " is requesting the Refuel Truck...");
            truck.acquire();
            System.out.println("  ATC: Refuel Truck assigned to " + name + ".");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    public synchronized void releaseRefuelTruck(String name, boolean isEmergency) {
        System.out.println("  ATC: " + name + " finished refueling. Truck released.");
        truck.release();

        if (isEmergency) {
            emergency = false;
            System.out.println("  ATC: Emergency over. Normal operations resuming.\n[EMERGENCY OVER]");
            notifyAll();
        }
    }

    
    // PART 4: STATISTICS & REPORTING
    // synchronized because multiple planes finish at the same time(microsecond very close), even only one runway 
    public synchronized void stats(long waitTime, int passengers) {
        totalPlanesServed++;
        totalPassengersBoarded += passengers;
        totalWaitTime += waitTime;

        if (waitTime > maxWaitTime) {
            maxWaitTime = waitTime;
        }
        if (waitTime < minWaitTime) {
            minWaitTime = waitTime;
        }
    }

    public void printStats() {
        System.out.println("\n --------- ASIA PACIFIC AIRPORT REPORT -------- ");
        System.out.println("  Total Planes Served: " + totalPlanesServed);
        System.out.println("  Total Passengers Boarded: " + totalPassengersBoarded);
        
        if (totalPlanesServed > 0) {
            System.out.println("  Max Wait Time: " + maxWaitTime + "ms");
            System.out.println("  Min Wait Time: " + minWaitTime + "ms");
            System.out.println("  Avg Wait Time: " + (totalWaitTime / totalPlanesServed) + "ms");
        }
        
        System.out.println("  Gates Available: " + gates.availablePermits() + " (Should be 3)");
        System.out.println("  Truck Available: " + truck.availablePermits() + " (Should be 1)");
        System.out.println(" ----------------------- End ----------------------- ");
    }
}
