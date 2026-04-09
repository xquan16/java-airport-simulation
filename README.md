# ✈️ Asia Pacific Airport Simulation

A multi-threaded Air Traffic Control (ATC) and airport operations simulation built in Java. This project demonstrates core concurrent programming concepts including thread synchronization, mutual exclusion, and resource management.

## 📝 Description
This system simulates the concurrent operations of an airport with limited resources. It manages airplanes requesting landings, docking at gates, disembarking/embarking passengers, refueling, and taking off, all while preventing race conditions and deadlocks.

## ✨ Key Features
* **Thread Synchronization:** Utilizes Java `wait()` and `notifyAll()` to manage runway access.
* **Resource Management:** Implements `Semaphore` to strictly control the 3 available terminal gates and 1 refueling truck.
* **Emergency Handling:** Priority runway access and immediate refueling for planes simulating critically low fuel.
* **Concurrency Safety:** Fully synchronized shared data structures to prevent "Lost Update" bugs when calculating airport statistics.

## 🚀 How to Run
1. Clone the repository.
2. Open the project in NetBeans or compile the `.java` files in the `src/tp081941_cpp` directory.
3. Run the `TP081941_CPP.java` main class.
4. The simulation will output the concurrent thread events to the console and print a final statistics report once all 6 planes have departed.
