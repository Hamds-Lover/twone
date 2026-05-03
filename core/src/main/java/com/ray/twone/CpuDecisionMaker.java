package com.ray.twone;

public class CpuDecisionMaker {
    /**
     * Determines whether the CPU should hit based on:
     * - CPU's current hand value
     * - Player's total visible cards (all except the hidden first card)
     * 
     * Three‑tier strategy:
     * - Visible total ≤ 6 → very conservative (stand at 10)
     * - Visible total 7–9 → normal (stand at 14)
     * - Visible total ≥ 10 → aggressive (stand at 17)
     */
    public boolean shouldHit(int cpuHandValue, int playerVisibleTotal) {
        int threshold;
        
        if (playerVisibleTotal <= 6) {
            threshold = 10;
        } else if (playerVisibleTotal <= 9) { // 7,8,9
            threshold = 14;
        } else { // 10 or more
            threshold = 17;
        }
        
        return cpuHandValue < threshold;
    }
}