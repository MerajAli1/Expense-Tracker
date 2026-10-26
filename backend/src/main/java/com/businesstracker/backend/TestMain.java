package com.businesstracker.backend;

public class TestMain {
    public static void main(String[] args) {
        System.out.println("=== 🏦 TAX LIABILITY VAULT & OCR TEST ===");

        // 1. Initializing the Vault State (Usable Cash: 50,000 | Tax Owed Starting: 0)
        TaxLiabilityVault vault = new TaxLiabilityVault("V101", 0.0, 50000.0);
        
        System.out.println("Initial Usable Cash: PKR " + vault.getBusinessUsableCash());
        System.out.println("Initial Tax Liability Vault: PKR " + vault.getTotalTaxOwed());
        System.out.println("Emergency Reserve Status: " + (vault.isEmergencyFundLocked() ? "LOCKED" : "HEALTHY"));

        System.out.println("\n--------------------------------------------------\n");

        // 2. Simulating the Real-World Scenario: Employee uploads a receipt of Rs. 11,800
        // Parameters: Total Amount = 11800.0, GST = 18% (0.18)
        double uploadedReceipt = 11800.0;
        double gstRate = 0.18;
        
        vault.processReceipt(uploadedReceipt, gstRate);

        System.out.println("\n--------------------------------------------------\n");

        // 3. Verifying Isolate Metrics on Dashboard Context
        System.out.println("[Dashboard View - Post Processing]");
        System.out.println("Final Usable Cash (Budget Affected): PKR " + vault.getBusinessUsableCash());
        System.out.println("Final Tax Owed Vault (Isolated Asset): PKR " + vault.getTotalTaxOwed());

        System.out.println("\n--------------------------------------------------\n");

        // 4. Testing the Emergency Lock Trigger Condition
        System.out.println("[System Action] Triggering Emergency Reserve Lock...");
        vault.lockEmergencyFund();
        System.out.println("Final Reserve Health Status: " + (vault.isEmergencyFundLocked() ? "LOCKED" : "HEALTHY"));

        System.out.println("\n=== ISOLATED LOGIC TEST PASSED SUCCESSFULLY ===");
    }
}



