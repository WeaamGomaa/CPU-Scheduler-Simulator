import java.util.*;
import java.util.stream.Collectors;

public class CPUSchedulerSimulator {

    // ============ MAIN METHOD ============
    public static void main(String[] args) {
        // TODO: Get input from user
        // TODO: Create Process objects
        // TODO: Run all 4 schedulers
        // TODO: Print results for each scheduler
    }

    // ============ PROCESS CLASS ============
    static class Process implements Comparable<Process> {
        // TODO: Add fields: name, arrival, burst, priority, remainingTime, etc.
        // TODO: Add AG-specific fields: quantum, quantumUsed, quantumHistory
        // TODO: Add constructors, getters, setters
        // TODO: Add copy constructor for creating duplicates

        String name;
        int arrivalTime;
        int burstTime;
        int priority;
        int remainingTime;
        int waitingTime;
        int turnaroundTime;
        int completionTime;
        int startTime = -1;

        // For AG Scheduler
        int quantum;
        int originalQuantum;
        int quantumUsed;
        List<Integer> quantumHistory;

        Process(String name, int arrival, int burst, int priority, int quantum) {
            this.name = name;
            this.arrivalTime = arrival;
            this.burstTime = burst;
            this.remainingTime = burst;
            this.priority = priority;
            this.quantum = quantum;
            this.originalQuantum = quantum;
            this.quantumHistory = new ArrayList<>();
        }

        // Copy constructor
        Process(Process other) {
            this.name = other.name;
            this.arrivalTime = other.arrivalTime;
            this.burstTime = other.burstTime;
            this.priority = other.priority;
            this.remainingTime = other.burstTime;
            this.quantum = other.quantum;
            this.originalQuantum = other.originalQuantum;
            this.quantumHistory = new ArrayList<>(other.quantumHistory);
        }

        @Override
        public int compareTo(Process other) {
            return Integer.compare(this.arrivalTime, other.arrivalTime);
        }

//        @Override
//        public String toString() {
//            return name + " (A:" + arrivalTime + ", B:" + burstTime + ", P:" + priority + ")";
//        }

    }


    // ============ RESULTS CLASS ============
    static class SchedulerResult {
        // TODO: Store execution order as List<String>
        // TODO: Store waiting times per process
        // TODO: Store turnaround times per process
        // TODO: Store averages
        // TODO: Store quantum history (for AG only)
        // TODO: Add method to print results
    }

    // ============ ABSTRACT SCHEDULER BASE CLASS ============
    static abstract class Scheduler {
        // TODO: Common fields: currentTime, contextSwitchTime, executionOrder
        protected int contextSwitchTime;
        protected List<Process> processes;
        // TODO: Abstract method: schedule()
//        abstract SchedulerResult schedule(List<Process> processes, int contextSwitchTime);
        // TODO: Helper methods: applyContextSwitch(), calculateMetrics()
    }

    // ============ MEMBER 1: SJF SCHEDULER ============
    static class SJFScheduler extends Scheduler {
        // TODO: Implement preemptive SJF algorithm
        // TODO: Always pick process with shortest remaining time
        // TODO: Handle preemption when shorter job arrives
        // TODO: Track execution order
        // TODO: Calculate waiting and turnaround times
        // TODO: Return SchedulerResult

//        SchedulerResult schedule(List<Process> processes, int contextSwitchTime){
//
//        }
    }

    // ============ MEMBER 2: RR SCHEDULER ============
    static class RRScheduler extends Scheduler {
        // TODO: Take quantum as constructor parameter
        // TODO: Implement Round Robin algorithm
        // TODO: Use queue for ready processes
        // TODO: Allocate quantum to each process
        // TODO: If not finished, add back to queue
        // TODO: Apply context switching between processes
        // TODO: Track execution order
        // TODO: Calculate waiting and turnaround times
        // TODO: Return SchedulerResult

//        SchedulerResult schedule(List<Process> processes, int contextSwitchTime){
//
//        }
    }

    // ============ MEMBER 3: PRIORITY SCHEDULER ============
    static class PriorityScheduler extends Scheduler {
        // TODO: Implement preemptive priority scheduling
        // TODO: Always pick highest priority (lowest number)
        // TODO: Implement aging mechanism to solve starvation
        // TODO: Handle preemption when higher priority arrives
        // TODO: Apply context switching
        // TODO: Track execution order
        // TODO: Calculate waiting and turnaround times
        // TODO: Return SchedulerResult

//        SchedulerResult schedule(List<Process> processes, int contextSwitchTime){
//
//        }
    }

    // ============ MEMBER 4 & 5: AG SCHEDULER ============
    static class AGScheduler extends Scheduler {
        // TODO: MEMBER 4 - CORE LOGIC:
        // 1. Initialize AG-specific fields for each process
        private int AG_quantum;
        private int AG_originalQuantum;
        private int quntumUsed;
        private int currentPhase;
        private int phaseStartTime;
        private int currentTime;
        private int completedProcesses;
        Process currentRunningProcess;
        private ArrayList<Integer> quantumHistory;
        private static int FCFS_PHASE = 1;
        private static int PRIORITY_PHASE = 2;
        private static int SJF_PHASE = 3;

        //queue for ready processes
        private Queue<Process> readyQueue;
        //for tracking quantum history for all processes
        private Map<String, List<Integer>> quantumHistories = new HashMap<>();

        AGScheduler(List<Process> processes){
            for (Process p : processes){
                this.AG_quantum = p.quantum;
                this.AG_originalQuantum = p.originalQuantum;
                this.quntumUsed = 0;
                this.currentPhase = FCFS_PHASE;
                this.phaseStartTime = 0;
                this.quantumHistory = new ArrayList<>(p.quantum);
            }
            currentTime = 0;
            completedProcesses = 0;
            //Sort all processes by arrival time
            processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
            this.readyQueue = new LinkedList<>();
            currentRunningProcess = null;
        }

        private int calculatePhaseBoundry(int quantum, int percentage){
            double value = (quantum * percentage) / 100.0;
            return (int) Math.ceil(value);
        }

        public Process selectNextProcess(Queue<Process> readyQueue, AGScheduler currentProcess){
            if(readyQueue.isEmpty()) return null;
            else if(currentProcess != null &&(currentProcess.currentPhase == FCFS_PHASE || currentProcess.currentPhase == PRIORITY_PHASE)){

            }
        }

        public int updatePhase(Process process){
            int phase1Boundry = calculatePhaseBoundry(AG_quantum, 25);
            int phase2Boundry = calculatePhaseBoundry(AG_quantum, 50);

            if(quntumUsed < phase1Boundry){
                return FCFS_PHASE;
            } else if (quntumUsed < phase2Boundry){
                return PRIORITY_PHASE;
            } else{
                return SJF_PHASE;
            }
        }

        public String checkStopCondition(Process process){

        }

        public void handleProcessStop(Process process, Queue<Process> readyQueue, String stopReason){

        }

        // 2. Implement 3-phase scheduling logic:
        //    - First 25% of quantum: FCFS (non-preemptive)
        //    - Next 25%: Non-preemptive Priority
        //    - Remaining 50%: Preemptive SJF

        public SchedulerResult schedule(List<Process> processes, int contextSwitchTime){
            while (completedProcesses < processes.size()){
                //Add arriving processes
                for (Process p : processes){
                    if(p.arrivalTime < currentTime && p.remainingTime > 0 && p != currentRunningProcess && readyQueue.contains(p)){
                        readyQueue.add(p);
                    }
                }

                //Select process to run
                if(currentRunningProcess == null || currentRunningProcess.remainingTime == 0){
                    currentRunningProcess = selectNextProcess(readyQueue, currentRunningProcess);
                    if(currentRunningProcess == null){
                        currentTime++;
                        continue;
                    }
                }

                //Execute 1 time unit
                currentRunningProcess.remainingTime--;
                currentRunningProcess.quantumUsed++;

                //Phase transition check
                updatePhase(currentRunningProcess);

                //Check stop conditions
                if(!checkStopCondition(currentRunningProcess).equals("Continue")){
                    handleProcessStop(currentRunningProcess, readyQueue, checkStopCondition(currentRunningProcess));
                }

                currentTime++;
            }

            // Phase Management

        }
        // 3. Handle 4 scenarios when process stops:
        //    i. Used full quantum but not finished -> quantum += 2
        //    ii. Preempted in Priority phase -> quantum += ceil(remaining/2)
        //    iii. Preempted in SJF phase -> quantum += remaining
        //    iv. Finished before quantum ends -> quantum = 0
        // 4. Track phase transitions
        // 5. Manage ready queue

        // TODO: MEMBER 5 - METRICS & INTEGRATION:
        // 1. Track quantum history updates
        // 2. Calculate waiting and turnaround times
        // 3. Integrate with main simulator
        // 4. Ensure proper output formatting
        // 5. Return complete SchedulerResult with quantum history
    }
}
