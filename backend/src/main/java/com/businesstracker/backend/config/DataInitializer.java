package com.businesstracker.backend.config;

import com.businesstracker.backend.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ExpenseRepository expenseRepository;
    private final TaxVaultRepository taxVaultRepository;
    private final SavingsPipelineRepository savingsPipelineRepository;
    private final UserDetailRepository userDetailRepository;

    public DataInitializer(ExpenseRepository expenseRepository,
                           TaxVaultRepository taxVaultRepository,
                           SavingsPipelineRepository savingsPipelineRepository,
                           UserDetailRepository userDetailRepository) {
        this.expenseRepository = expenseRepository;
        this.taxVaultRepository = taxVaultRepository;
        this.savingsPipelineRepository = savingsPipelineRepository;
        this.userDetailRepository = userDetailRepository;
    }

    @Override
    public void run(String... args) {
        seedIfEmpty();
    }

    public void seedIfEmpty() {
        if (expenseRepository.count() == 0) {
            seedAll();
        }
    }

    public void seedAll() {
        // 1. Seed User Details
        if (userDetailRepository.count() == 0) {
            Userdetail defaultUser = new Userdetail("DEFAULT_USER", "Business Manager", 185000.0, 95000.0);
            defaultUser.setEmail("finance@mybusiness.com");
            defaultUser.setCurrency("PKR");
            userDetailRepository.save(defaultUser);
        }

        // 2. Seed Tax Liability Vault
        if (taxVaultRepository.count() == 0) {
            TaxLiabilityVault vault = new TaxLiabilityVault("DEFAULT_VAULT", 4500.0, 75000.0);
            taxVaultRepository.save(vault);
        }

        // 3. Seed Expenses
        LocalDate today = LocalDate.now();
        BaseExpense e1 = new BaseExpense(null, "Commercial Office Rent", 45000.0, "Rent",
                "Monthly commercial office space lease", today.minusDays(2), "Bank Transfer", false, 0.0);

        BaseExpense e2 = new BaseExpense(null, "Commercial Electricity Bill", 14500.0, "Utility",
                "Electricity charges for operational floor", today.minusDays(5), "Online", true, 0.18);

        BaseExpense e3 = new BaseExpense(null, "Facebook & Google Ad Campaign", 25000.0, "Marketing",
                "Q4 Lead Generation Digital Campaign", today.minusDays(8), "Credit Card", true, 0.18);

        BaseExpense e4 = new BaseExpense(null, "Dedicated Fiber Internet", 6500.0, "Utility",
                "Monthly enterprise fiber broadband", today.minusDays(12), "Online", false, 0.0);

        BaseExpense e5 = new BaseExpense(null, "Office Stationery & Printer Ink", 4800.0, "Supplies",
                "Bulk paper and cartridge refills", today.minusDays(15), "Cash", true, 0.18);

        BaseExpense e6 = new BaseExpense(null, "Client Meeting & Networking Lunch", 8200.0, "Food & Entertainment",
                "Executive lunch with retail partners", today.minusDays(18), "Credit Card", false, 0.0);

        BaseExpense e7 = new BaseExpense(null, "HVAC Maintenance & Servicing", 5500.0, "Maintenance",
                "Quarterly AC servicing and filter replacement", today.minusDays(22), "Cash", false, 0.0);

        BaseExpense e8 = new BaseExpense(null, "Client Site Travel & Cab Allowance", 7800.0, "Travel",
                "Fuel and cab charges for onsite audits", today.minusDays(25), "Cash", false, 0.0);

        expenseRepository.saveAll(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8));

        // 4. Seed Savings Pipelines
        if (savingsPipelineRepository.count() == 0) {
            SavingsPipelines sp1 = new SavingsPipelines("TXN-SRV-901", "Emergency Server Hardware Replacement",
                    20000.0, today.minusDays(6));

            SavingsPipelines sp2 = new SavingsPipelines("TXN-INV-802", "Bulk Raw Inventory Purchase",
                    35000.0, today.minusDays(25));
            sp2.returnMoney();

            savingsPipelineRepository.saveAll(Arrays.asList(sp1, sp2));
        }

        System.out.println("✨ [Expense Tracker] Initial demo data seeded successfully into MongoDB.");
    }

    public void clearAll() {
        expenseRepository.deleteAll();
        taxVaultRepository.deleteAll();
        savingsPipelineRepository.deleteAll();
        userDetailRepository.deleteAll();
        System.out.println("🧹 [Expense Tracker] All collections cleared.");
    }
}
