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
        List<String> executionOrder = new ArrayList<>();
        Map<String, Integer> waitingTimes = new HashMap<>();
        Map<String, Integer> turnaroundTimes = new HashMap<>();
        double avgWaitingTime;
        double avgTurnaroundTime;
        Map<String, List<Integer>> quantumHistory; // For AG only
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
        private int quantumUsed;
        private int currentTime;
        private int contextSwitchTime;
        private int completedProcesses;
        Process currentRunningProcess;
        private static final int FCFS_PHASE = 1;
        private static final int PRIORITY_PHASE = 2;
        private static final int SJF_PHASE = 3;
        //queue for ready processes
        private Queue<Process> readyQueue;
        //for tracking quantum history for all processes
        private Map<String, List<Integer>> quantumHistories;

        AGScheduler(List<Process> processes){
            this.processes = processes;
            quantumHistories = new HashMap<>();
            currentTime = 0;
            completedProcesses = 0;
            this.readyQueue = new LinkedList<>();
            for (Process p : processes){
                quantumHistories.put(p.name, new ArrayList<>());
                quantumHistories.get(p.name).add(p.quantum); // storing initial quantum
                this.quantumUsed = 0;
            }
            //Sort all processes by arrival time
            processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
            currentRunningProcess = null;
        }

        SchedulerResult schedule(List<Process> processes, int contextSwitchTime){
            this.contextSwitchTime = contextSwitchTime;
            SchedulerResult result = new SchedulerResult();
            result.executionOrder = new ArrayList<>();
            result.quantumHistory = new HashMap<>();

            while (completedProcesses < processes.size()){
                //Add arriving processes
                for (Process p : processes){
                    if(p.arrivalTime <= currentTime && p.remainingTime > 0 && p != currentRunningProcess && !readyQueue.contains(p)){
                        readyQueue.add(p);
                    }
                }

                //Select next process to run
                if(currentRunningProcess == null || currentRunningProcess.remainingTime == 0){
                    if(currentRunningProcess != null && currentRunningProcess.remainingTime == 0){
                        handleCompletion(currentRunningProcess, currentTime);
                        completedProcesses++;
                    }
                    Process nextProcess = selectNextProcess(currentRunningProcess);

                    //Context Switch
                    if(nextProcess != null && currentRunningProcess != null
                            && currentRunningProcess != nextProcess
                            && contextSwitchTime > 0){
                        currentTime += contextSwitchTime;
                        result.executionOrder.add("[CS]");
                    }

                    currentRunningProcess = nextProcess;

                }

                //Execute current process for 1 time unit
                if(currentRunningProcess != null){
                    currentRunningProcess.remainingTime--;
                    currentRunningProcess.quantumUsed++;
                    result.executionOrder.add(currentRunningProcess.name);
                    String stopReason = checkStopCondition(currentRunningProcess);

                    if (!stopReason.equals("Continue")){
                        handleProcessStop(currentRunningProcess, stopReason, result);

                        if (stopReason.equals("Completed")){
                            completedProcesses++;
                            currentRunningProcess = null;
                        } else {
                            currentRunningProcess.quantumUsed = 0;
                            readyQueue.add(currentRunningProcess); //process goes back to queue
                            currentRunningProcess = null;
                        }
                    }
                }

                currentTime++; //move to next time unit

            }
            // Rofida's part will be integrated here
            calculateMetrics(processes, result);

            result.quantumHistory = quantumHistories;
            return result;
        }

        //=======Helper Functions========

        private int getCurrentPhase(Process process){
            if (process == null) return 0;

            int phase1End = (int) Math.ceil(process.quantum * 0.25);
            int phase2End = (int) Math.ceil(process.quantum * 0.50);

            if(process.quantumUsed < phase1End){
                return FCFS_PHASE;
            } else if (process.quantumUsed < phase2End){
                return PRIORITY_PHASE;
            } else{
                return SJF_PHASE;
            }
        }

        private Process selectNextProcess(Process currentProcess){
            if(readyQueue.isEmpty()) return null;

            if (currentProcess != null){
                int currentPhase = getCurrentPhase(currentProcess);

                if (currentPhase == FCFS_PHASE || currentPhase == PRIORITY_PHASE){
                    return currentProcess; // continue same process (these two phases are non_preemtive)
                }
                if(currentPhase == SJF_PHASE){ //this phase is preemtive
                    Process shortest = findShortestJob();
                    if(shortest != null && shortest != currentProcess){
                        return shortest;
                    }
                    return currentProcess;
                }
            }
            return readyQueue.poll();
        }

        private Process findShortestJob(){
            Process shortest = null;
            int minTime = Integer.MAX_VALUE;

            for (Process p : readyQueue){
                if(p.remainingTime < minTime){
                    minTime = p.remainingTime;
                    shortest = p;
                }
            }
            return shortest;
        }


        private String checkStopCondition(Process process){
            //case 4 : process finished its work
            if (process.remainingTime == 0){
                return "Completed";
            }
            // case 1 : process used all its quantum, but still has work
            if (process.quantumUsed >= process.quantum){
                return "Quantum_Expired";
            }
            int currentPhase = getCurrentPhase(process);

            //case 3 : process in SJF phase and shorter job available
            if (currentPhase == SJF_PHASE){
                Process shortest = findShortestJob();
                if (shortest != null && shortest != process){
                    return "SJF_Preempted";
                }
            }
            //case 2 : process in priority phase and higher priority arrives
            if (currentPhase == PRIORITY_PHASE){

            }
            return "Continue";
        }

        private void handleProcessStop(Process process, String stopReason, SchedulerResult result){
            int oldQuantum = process.quantum;

            switch (stopReason){
                // used all quantum but not finished
                case "Quantum_Expired":
                   process.quantum += 2;
                    System.out.println(process.name + ": Quantum " + oldQuantum + "->" + process.quantum + "(+2, full quantum used)");
                    break;
                // preempted in priority phase
                case "Priority_Preempted":
                    int remaining = process.quantum - process.quantumUsed;
                    int increase = (int) Math.ceil(remaining / 2.0);
                    process.quantum += increase;
                    System.out.println(process.name + ": Quantum " + oldQuantum + "->" + process.quantum + " (+" + increase + ", priority preempted");
                    break;
                // preempted in SJF phase
                case "SJF_Preempted":
                    remaining = process.quantum - process.quantumUsed;
                    process.quantum += remaining;
                    System.out.println(process.name + ": Quantum " + oldQuantum + " → " +
                            process.quantum + " (+" + remaining + ", SJF preempted)");
                    break;
                // finished before quantum ended
                case "Completed":
                    process.quantum = 0;
                    System.out.println(process.name + ": Quantum " + oldQuantum + " → 0 (completed)");
                    break;

            }
            // update quantum in history
            if (oldQuantum != process.quantum){
                quantumHistories.get(process.name).add(process.quantum);
            }
            process.quantumUsed = 0; // reset for next time

            if (!stopReason.equals("Completed")){
                readyQueue.add(process); // add back to ready queue if not completed
            }
        }

        // TODO: MEMBER 5 - METRICS & INTEGRATION:
        // 1. Track quantum history updates
        // 2. Calculate waiting and turnaround times
        // 3. Integrate with main simulator
        // 4. Ensure proper output formatting
        // 5. Return complete SchedulerResult with quantum history

        private void handleCompletion(Process process, int time){

        }

        private void calculateMetrics(List<Process> processes, SchedulerResult result){

        }

    }
}
