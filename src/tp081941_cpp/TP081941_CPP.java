
package tp081941_cpp;
import java.util.Random;
import java.util.ArrayList;

public class TP081941_CPP {

    public static void main(String[] args) {
        
        Random rand = new Random();
        
        // Resource
        Airport airport = new Airport();
        
        ArrayList<Airplane> planes = new ArrayList<>();
        
        // Actors
        for(int i = 1; i < 7; i++){
            int delay = rand.nextInt(2000);
            String planeName = "P" + i; 
            
            try {
                Thread.sleep(delay); 
                
                Airplane plane;
                if(i == 6){   // Emergency
                    plane = new Airplane(planeName, airport, true);
                } else{
                    plane = new Airplane(planeName, airport , false);
                }
                
                planes.add(plane);
                plane.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        for(Airplane plane : planes) {
            try {
                plane.join(); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        airport.printStats();
    }   
}
