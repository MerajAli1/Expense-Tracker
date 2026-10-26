# Spring Boot for Node.js & Express.js Developers
*A practical guide to understanding this Expense Tracker Spring Boot codebase using Express & Mongoose concepts.*

---

## 1. The Big Picture: Express.js vs. Spring Boot

In an **Express.js** application, you typically have:
1. **Dependencies** defined in `package.json`.
2. **Environment configuration** in `.env` or `config.js`.
3. **Application initialization** in `server.js` or `app.js` with `app.listen(PORT)`.
4. **Database Schemas & Models** using Mongoose (`mongoose.Schema`, `mongoose.model`).
5. **Route Handlers** using `express.Router()` (`router.get()`, `router.post()`).
6. **Middlewares** like `cors()`, body-parser, and global error handlers.

Spring Boot organizes the exact same functionality into structured Java classes:

| Feature | Node.js / Express.js | This Spring Boot Codebase |
| :--- | :--- | :--- |
| **Dependencies** | `package.json` | [`pom.xml`](file:///d:/Expense_Tracker/backend/pom.xml) |
| **Environment / Config** | `.env` or `config.js` | [`application.properties`](file:///d:/Expense_Tracker/backend/src/main/resources/application.properties) |
| **Server Startup** | `app.listen(8081)` in `server.js` | [`BackendApplication.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/BackendApplication.java) |
| **Database Schema** | `new mongoose.Schema(...)` | Java classes with `@Document` (e.g. [`BaseExpense.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/BaseExpense.java)) |
| **Database Queries** | `Expense.find()`, `Expense.create()` | Repositories extending `MongoRepository` (e.g. [`ExpenseRepository.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/ExpenseRepository.java)) |
| **Business Logic** | Helper files or Service functions | Services with `@Service` (e.g. [`ExpenseService.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/service/ExpenseService.java)) |
| **Routes & Endpoints** | `router.get('/api/expenses', ...)` | Controllers with `@RestController` and `@GetMapping` (e.g. [`ExpenseController.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/controller/ExpenseController.java)) |
| **CORS Middleware** | `app.use(cors())` | [`CorsConfig.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/config/CorsConfig.java) |
| **Error Middleware** | `app.use((err, req, res, next) => ...)` | [`GlobalExceptionHandler.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/exception/GlobalExceptionHandler.java) |
| **Database Seeding** | `node seed.js` | [`DataInitializer.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/config/DataInitializer.java) |

---

## 2. Request Lifecycle: What Happens When an API is Called?

When your React frontend sends a request like `GET http://localhost:8081/api/expenses`:

```text
Browser / React Frontend
        │
        ▼ (HTTP Request: GET /api/expenses)
┌────────────────────────────────────────────────────────┐
│ 1. Controller: ExpenseController.java                  │
│    - Matches the URL path (@GetMapping)                │
│    - Extracts query params (?category=..., ?search=...)│
└───────────────────────┬────────────────────────────────┘
                        │
                        ▼
┌────────────────────────────────────────────────────────┐
│ 2. Service: ExpenseService.java                        │
│    - Contains business rules & data calculations       │
│    - Prepares queries and calls the repository         │
└───────────────────────┬────────────────────────────────┘
                        │
                        ▼
┌────────────────────────────────────────────────────────┐
│ 3. Repository: ExpenseRepository.java                  │
│    - Spring Data executes the MongoDB query            │
└───────────────────────┬────────────────────────────────┘
                        │
                        ▼
┌────────────────────────────────────────────────────────┐
│ 4. MongoDB Database (BusinessTrackerDB)                │
│    - Fetches or updates records in the "expenses" coll │
└───────────────────────┬────────────────────────────────┘
                        │
                        ▼
Response Object is returned -> Spring Boot converts it to JSON automatically -> React receives JSON
```

---

## 3. Code Walkthrough by Folder

All source code is located in:
`backend/src/main/java/com/businesstracker/backend/`

### 1. Models (Like Mongoose Schemas)
In Express, you write:
```javascript
const expenseSchema = new mongoose.Schema({
  title: String,
  baseAmount: Number,
  category: String,
  taxable: Boolean,
  date: Date
});
module.exports = mongoose.model('Expense', expenseSchema);
```

In Spring Boot, we write standard Java classes decorated with `@Document(collection = "expenses")`:
* [`BaseExpense.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/BaseExpense.java): Defines an Expense document (id, title, baseAmount, category, description, date, paymentMethod, tax calculations, createdAt).
* [`TaxableExpense.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/TaxableExpense.java): Subclass of BaseExpense demonstrating OOP inheritance with tax rate calculations.
* [`TaxLiabilityVault.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/TaxLiabilityVault.java): Document in collection `tax_vault` tracking usable cash, tax liabilities, and emergency fund status.
* [`SavingsPipelines.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/SavingsPipelines.java): Document in collection `savings_pipeline` tracking money borrowed from and returned to savings.
* [`Userdetail.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/Userdetail.java): Document in collection `user_details` holding business balance, savings balance, and user info.

---

### 2. Repositories (Like Mongoose Model Queries)
In Express/Mongoose:
```javascript
const allExpenses = await Expense.find().sort({ date: -1 });
const byCat = await Expense.find({ category: 'Rent' });
```

In Spring Boot, you define a repository interface extending `MongoRepository`:
```java
@Repository
public interface ExpenseRepository extends MongoRepository<BaseExpense, String> {
    List<BaseExpense> findAllByOrderByDateDesc();
    List<BaseExpense> findByCategoryIgnoreCaseOrderByDateDesc(String category);
}
```
**Magic of Spring Data:** You just declare the method name! Spring analyzes the method name (e.g. `findByCategoryIgnoreCase`) and writes the underlying MongoDB query for you.

---

### 3. Services (Your Business Logic Layer)
In Express, you often place helper functions or controller business logic directly in route files or a separate service file.
* [`ExpenseService.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/service/ExpenseService.java):
  * Handles CRUD logic for expenses.
  * Calculates dashboard statistics (total expenses, net base, total tax, monthly spending breakdown, category breakdown for charts).
* [`TaxVaultService.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/service/TaxVaultService.java):
  * Takes receipt total and GST percentage, computes base cost and isolated tax liability, deducts base cost from usable cash, adds tax to total tax owed, and automatically records an expense in MongoDB.
* [`SavingsPipelineService.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/service/SavingsPipelineService.java):
  * Manages borrowing money from savings into the business pool and marking debts as returned.
* [`UserDetailService.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/service/UserDetailService.java):
  * Manages user balance updates.

---

### 4. Controllers (Your Express Route Handlers)
In Express:
```javascript
router.get('/api/expenses', async (req, res) => {
  const data = await expenseService.getAll(req.query.category);
  res.json({ success: true, message: 'Retrieved', data });
});
```

In Spring Boot ([`ExpenseController.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/controller/ExpenseController.java)):
```java
@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BaseExpense>>> getAllExpenses(
            @RequestParam(required = false) String category) {
        List<BaseExpense> expenses = expenseService.getAllExpenses(category, null, null, null);
        return ResponseEntity.ok(ApiResponse.ok("Expenses retrieved", expenses));
    }
}
```
* `@RestController`: Tells Spring Boot to return JSON (like `res.json()`).
* `@RequestMapping("/api/expenses")`: Base path for routes (like `app.use('/api/expenses', router)`).
* `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: Map directly to HTTP verbs.
* `@RequestParam`: Reads query parameters like `?category=Rent` (`req.query.category`).
* `@PathVariable`: Reads route parameters like `/api/expenses/:id` (`req.params.id`).
* `@RequestBody`: Reads JSON request body (`req.body`).

---

### 5. DTOs (Data Transfer Objects)
Located in `backend/src/main/java/com/businesstracker/backend/dto/`:
* In Express, you frequently send ad-hoc JavaScript objects `{ success: true, data: ... }`.
* In Java, we create typed DTOs:
  * [`ApiResponse.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/dto/ApiResponse.java): Wrapper ensuring every single API response always returns `{ success: true, message: "...", data: {...}, timestamp: "..." }`.
  * [`ExpenseSummaryDTO.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/dto/ExpenseSummaryDTO.java): Holds aggregated dashboard data for charts and metric cards.
  * [`ReceiptProcessRequest.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/dto/ReceiptProcessRequest.java) & [`ReceiptProcessResponse.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/dto/ReceiptProcessResponse.java): Strongly-typed request/response for the OCR/tax isolating feature.

---

### 6. Config & Seed Data
Located in `backend/src/main/java/com/businesstracker/backend/config/`:
* [`CorsConfig.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/config/CorsConfig.java): Equivalent to Express `app.use(cors())`. Allows your frontend (running on `http://localhost:5173` or `http://localhost:3000`) to send GET, POST, PUT, DELETE requests without getting blocked by the browser.
* [`DataInitializer.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/config/DataInitializer.java): Implements `CommandLineRunner`. Runs automatically on server startup. If your MongoDB database is empty, it seeds 8 realistic expenses, a default user, and a vault so your frontend has live data immediately.

---

### 7. Exception Handling
Located in `backend/src/main/java/com/businesstracker/backend/exception/`:
* [`GlobalExceptionHandler.java`](file:///d:/Expense_Tracker/backend/src/main/java/com/businesstracker/backend/exception/GlobalExceptionHandler.java): Equivalent to Express error middleware `app.use((err, req, res, next) => res.status(500).json(...))`.
  * If a record is not found, it automatically returns HTTP 404 with JSON:
    `{ "success": false, "message": "Expense not found with ID: xyz" }`.
  * If validation fails (e.g. negative amount), it returns HTTP 400 with field-by-field error details.

---

## 4. How to Add a New Feature (Cheat Sheet)

If you want to add a new entity in the future (e.g., `Income` or `Category`):

1. **Create the Model**: Create `Income.java` with `@Document(collection = "incomes")` and fields.
2. **Create the Repository**: Create `IncomeRepository.java` extending `MongoRepository<Income, String>`.
3. **Create the Service**: Create `IncomeService.java` with `@Service` to handle queries and business logic.
4. **Create the Controller**: Create `IncomeController.java` with `@RestController` and `@RequestMapping("/api/incomes")` to expose your endpoints.
