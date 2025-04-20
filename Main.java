import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Expense {
    double amount;
    String category;
    String description;

    public Expense(double amount, String category, String description) {
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public String toString() {
        return "Amount: ₹" + amount + ", Category: " + category + ", Description: " + description;
    }

    public String toFileString() {
        return amount + "|" + category + "|" + description;
    }

    public static Expense fromFileString(String line) {
        String[] parts = line.split("\|");
        if (parts.length != 3) return null;
        return new Expense(Double.parseDouble(parts[0]), parts[1], parts[2]);
    }
}

public class Main {
    static final String FILE_NAME = "expenses.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Expense> expenses = loadExpensesFromFile();

        while (true) {
            System.out.println("\n=== Expense Tracker ===");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Expenses by Category");
            System.out.println("4. View Total Expenditure");
            System.out.println("5. Edit Expense");
            System.out.println("6. Delete Expense");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addExpense(scanner, expenses);
                    break;

                case 2:
                    viewExpenses(expenses);
                    break;

                case 3:
                    filterByCategory(scanner, expenses);
                    break;

                case 4:
                    showTotalExpenditure(expenses);
                    break;

                case 5:
                    editExpense(scanner, expenses);
                    break;

                case 6:
                    deleteExpense(scanner, expenses);
                    break;

                case 7:
                    System.out.println("👋 Exiting... Have a nice day!");
                    saveExpensesToFile(expenses);
                    return;

                default:
                    System.out.println("❌ Invalid option.");
            }
        }
    }

    public static void addExpense(Scanner scanner, ArrayList<Expense> expenses) {
        System.out.print("Enter amount: ₹");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        Expense newExpense = new Expense(amount, category, description);
        expenses.add(newExpense);
        saveExpenseToFile(newExpense);
        System.out.println("✅ Expense added and saved!");
    }

    public static void viewExpenses(ArrayList<Expense> expenses) {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
        } else {
            System.out.println("\n--- Expense List ---");
            for (Expense e : expenses) {
                System.out.println(e);
            }
        }
    }

    public static void filterByCategory(Scanner scanner, ArrayList<Expense> expenses) {
        System.out.print("Enter category to filter by: ");
        String category = scanner.nextLine();
        boolean found = false;

        System.out.println("\n--- Filtered Expenses ---");
        for (Expense e : expenses) {
            if (e.category.equalsIgnoreCase(category)) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No expenses found in this category.");
        }
    }

    public static void showTotalExpenditure(ArrayList<Expense> expenses) {
        double total = 0;
        for (Expense e : expenses) {
            total += e.amount;
        }
        System.out.println("\nTotal Expenditure: ₹" + total);
    }

    public static void editExpense(Scanner scanner, ArrayList<Expense> expenses) {
        System.out.print("Enter the index of the expense to edit: ");
        int index = scanner.nextInt();
        if (index >= 0 && index < expenses.size()) {
            scanner.nextLine();
            Expense e = expenses.get(index);

            System.out.println("Current Expense: " + e);
            System.out.print("Enter new amount (or press enter to keep the same): ₹");
            double newAmount = scanner.hasNextDouble() ? scanner.nextDouble() : e.amount;
            scanner.nextLine();
            System.out.print("Enter new category (or press enter to keep the same): ");
            String newCategory = scanner.nextLine().isEmpty() ? e.category : scanner.nextLine();
            System.out.print("Enter new description (or press enter to keep the same): ");
            String newDescription = scanner.nextLine().isEmpty() ? e.description : scanner.nextLine();

            expenses.set(index, new Expense(newAmount, newCategory, newDescription));
            saveExpensesToFile(expenses);
            System.out.println("✅ Expense updated!");
        } else {
            System.out.println("❌ Invalid index.");
        }
    }

    public static void deleteExpense(Scanner scanner, ArrayList<Expense> expenses) {
        System.out.print("Enter the index of the expense to delete: ");
        int index = scanner.nextInt();
        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
            saveExpensesToFile(expenses);
            System.out.println("✅ Expense deleted!");
        } else {
            System.out.println("❌ Invalid index.");
        }
    }

    public static void saveExpenseToFile(Expense expense) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(expense.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("⚠ Failed to save expense: " + e.getMessage());
        }
    }

    public static void saveExpensesToFile(ArrayList<Expense> expenses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.write(e.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("⚠ Failed to save expenses: " + e.getMessage());
        }
    }

    public static ArrayList<Expense> loadExpensesFromFile() {
        ArrayList<Expense> expenses = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) return expenses;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Expense e = Expense.fromFileString(line);
                if (e != null) {
                    expenses.add(e);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠ Error loading file: " + e.getMessage());
        }

        return expenses;
    }
}