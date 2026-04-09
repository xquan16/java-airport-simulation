package tp081941_cpp;

public class Airplane extends Thread {
    private Airport airport;
    private boolean isEmergency;

    // Constructor: accept an boolean for the Emergency Plane
    public Airplane(String name, Airport airport, boolean isEmergency) {
        this.setName(name);
        this.airport = airport;
        this.isEmergency = isEmergency;
    }

    @Override
    public void run() {
        // 1. START TIMER (For Statistics)
        long start = System.currentTimeMillis();
        if(!isEmergency){
            System.out.println("  " + getName() + " : Hey ATC, " + getName() + " here in the air and requesting a gate.");
        } else {
            System.out.println("  " + getName() + " : Hey ATC, " + getName() + " here in critical fuel levels. Requesting emergency gate access!");
        }
        
        // 2. RESERVE GATE (Critical Step to prevent Deadlock)
        airport.requireGate(getName()); 
        
        // 3. LANDING PHASE
        System.out.println("  " + getName() + " : Requesting Landing...");
        airport.requestRunway(getName(), isEmergency); 
        
        System.out.println("[" + getName() + " Landing]\n" + "  " + getName() + " : Landing.");
        try { 
            Thread.sleep(1000); // Simulating landing time
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
        
        // If we were the emergency plane, this tells ATC to turn off the alarm.
        airport.releaseRunway(getName(), isEmergency); 
        
        // 4. GATE PHASE (The Service Routine)
        System.out.println("  " + getName() + " : Docked at Gate.");
        
        try {
            if (isEmergency) {
                // Emergency
                System.out.println("[EMERGENCY !!!]\n  " + getName() + " : Low Fuel!!!");
                
                airport.requestRefuelTruck(getName());
                System.out.println("  " + getName() + " : Emergency Refuelling...");
                Thread.sleep(1000); 
                airport.releaseRefuelTruck(getName(), isEmergency);
                
                System.out.println("  " + getName() + " : Passengers Disembarking.");
                Thread.sleep(500);
            } else {
                // Normal 
                System.out.println("  " + getName() + " : Passengers Disembarking.");
                Thread.sleep(500);
                
                airport.requestRefuelTruck(getName());
                System.out.println("  " + getName() + " : Refuelling...");
                Thread.sleep(1000); 
                airport.releaseRefuelTruck(getName(), isEmergency);
            }
            
            // Cleaning & Supplies
            System.out.println("  " + getName() + " : Refilling supplies and cleaning.");
            Thread.sleep(500);

            // New Passengers Enter
            System.out.println("  " + getName() + " : Passengers Boarding.");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 5. DEPARTURE PHASE
        System.out.println("  " + getName() + " : Requesting Taking off.");
        
        airport.requestRunway(getName(), false);
        
        // when get the runway, we can give back the gate ticket.
        airport.releaseGate(getName()); 
        
        System.out.println("  " + getName() + " : Taking-off.");
        try { 
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        airport.releaseRunway(getName(), false);        
        System.out.println("  " + getName() + " : Flown away.\n[" + getName() + " Left]");
        
        // 6. FINISH STATISTICS
        long end = System.currentTimeMillis();
        long waitTime = end - start;
        
        airport.stats(waitTime, 50); // Adding 50 passengers as per requirement
    }
}