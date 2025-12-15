public class CPUSchedulerSimulator {

    // ============ MAIN METHOD ============
    public static void main(String[] args) {
        // TODO: Get input from user
        // TODO: Create Process objects
        // TODO: Run all 4 schedulers
        // TODO: Print results for each scheduler
    }

    // ============ PROCESS CLASS ============
    static class Process {
        // TODO: Add fields: name, arrival, burst, priority, remainingTime, etc.
        // TODO: Add AG-specific fields: quantum, quantumUsed, quantumHistory
        // TODO: Add constructors, getters, setters
        // TODO: Add copy constructor for creating duplicates
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
        // TODO: Abstract method: schedule()
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
    }

    // ============ MEMBER 4 & 5: AG SCHEDULER ============
    static class AGScheduler extends Scheduler {
        // TODO: MEMBER 4 - CORE LOGIC:
        // 1. Initialize AG-specific fields for each process
        // 2. Implement 3-phase scheduling logic:
        //    - First 25% of quantum: FCFS (non-preemptive)
        //    - Next 25%: Non-preemptive Priority
        //    - Remaining 50%: Preemptive SJF
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
