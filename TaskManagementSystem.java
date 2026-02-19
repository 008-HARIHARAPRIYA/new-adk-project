import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TaskManagementSystem {
    private static List<Task> tasks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int nextId = 1;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   TASK MANAGEMENT");
        System.out.println("========================================\n");

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewAllTasks();
                    break;
                case 3:
                    viewTasksByPriority();
                    break;
                case 4:
                    markTaskComplete();
                    break;
                case 5:
                    deleteTask();
                    break;
                case 6:
                    searchTasks();
                    break;
                case 7:
                    viewStatistics();
                    break;
                case 8:
                    running = false;
                    System.out.println("\nThank you for using Task Management System!");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.\n");
            }
        }
        scanner.close();
    }

    /**
     * Displays the main menu options
     */
    private static void displayMenu() {
        System.out.println("========================================");
        System.out.println("1. Add New Tasks");
        System.out.println("2. View All Tasks");
        System.out.println("3. View Tasks by Priority");
        System.out.println("4. Mark Task as Complete");
        System.out.println("5. Delete Task");
        System.out.println("6. Search Tasks");
        System.out.println("7. View Statistics");
        System.out.println("8. Exit");
        System.out.println("========================================");
    }

    /**
     * Adds a new task to the system
     */
    private static void addTask() {
        System.out.println("\n--- Add New Task ---");
        
        scanner.nextLine(); // Clear buffer
        System.out.print("Task Title: ");
        String title = scanner.nextLine();

        System.out.print("Task Description: ");
        String description = scanner.nextLine();

        System.out.print("Due Date (DD-MM-YYYY): ");
        String dueDateStr = scanner.nextLine();
        LocalDate dueDate = parseDate(dueDateStr);

        System.out.println("\nPriority Levels:");
        System.out.println("1. HIGH");
        System.out.println("2. MEDIUM");
        System.out.println("3. LOW");
        int priorityChoice = getIntInput("Select Priority: ");
        Priority priority = Priority.fromInt(priorityChoice);

        Task task = new Task(nextId++, title, description, dueDate, priority);
        tasks.add(task);

        System.out.println("\nTask added successfully!\n");
    }

   
    private static void viewAllTasks() {
        System.out.println("\n--- All Tasks ---");
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.\n");
            return;
        }

        for (Task task : tasks) {
            System.out.println(task);
            System.out.println("----------------------------------------");
        }
        System.out.println();
    }

    /**
     * Views tasks filtered by priority level
     */
    private static void viewTasksByPriority() {
        System.out.println("\n--- Filter by Priority ---");
        System.out.println("1. HIGH");
        System.out.println("2. MEDIUM");
        System.out.println("3. LOW");
        int priorityChoice = getIntInput("Select Priority: ");
        Priority priority = Priority.fromInt(priorityChoice);

        System.out.println("\n--- " + priority + " Priority Tasks ---");
        boolean found = false;
        for (Task task : tasks) {
            if (task.getPriority() == priority) {
                System.out.println(task);
                System.out.println("----------------------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No tasks found with " + priority + " priority.\n");
        }
        System.out.println();
    }

    /**
     * Marks a task as complete
     */
    private static void markTaskComplete() {
        System.out.println("\n--- Mark Task Complete ---");
        viewAllTasks();
        
        int taskId = getIntInput("Enter Task ID to mark complete: ");
        Task task = findTaskById(taskId);

        if (task != null) {
            task.markComplete();
            System.out.println("\nTask marked as complete!\n");
        } else {
            System.out.println("\nTask not found.\n");
        }
    }

    /**
     * Deletes a task from the system
     */
    private static void deleteTask() {
        System.out.println("\n--- Delete Task ---");
        viewAllTasks();
        
        int taskId = getIntInput("Enter Task ID to delete: ");
        Task task = findTaskById(taskId);

        if (task != null) {
            tasks.remove(task);
            System.out.println("\nTask deleted successfully!\n");
        } else {
            System.out.println("\nTask not found.\n");
        }
    }

    /**
     * Searches tasks by keyword in title or description
     */
    private static void searchTasks() {
        System.out.println("\n--- Search Tasks ---");
        scanner.nextLine(); // Clear buffer
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine().toLowerCase();

        System.out.println("\n--- Search Results ---");
        boolean found = false;
        for (Task task : tasks) {
            if (task.getTitle().toLowerCase().contains(keyword) ||
                task.getDescription().toLowerCase().contains(keyword)) {
                System.out.println(task);
                System.out.println("----------------------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No tasks found matching '" + keyword + "'.\n");
        }
        System.out.println();
    }

    /**
     * Displays task statistics
     */
    private static void viewStatistics() {
        System.out.println("\n--- Task Statistics ---");
        
        int total = tasks.size();
        int completed = 0;
        int pending = 0;
        int overdue = 0;
        int high = 0, medium = 0, low = 0;

        LocalDate today = LocalDate.now();

        for (Task task : tasks) {
            if (task.isCompleted()) {
                completed++;
            } else {
                pending++;
                if (task.getDueDate().isBefore(today)) {
                    overdue++;
                }
            }

            switch (task.getPriority()) {
                case HIGH:
                    high++;
                    break;
                case MEDIUM:
                    medium++;
                    break;
                case LOW:
                    low++;
                    break;
            }
        }

        System.out.println("Total Tasks: " + total);
        System.out.println("Completed: " + completed);
        System.out.println("Pending: " + pending);
        System.out.println("Overdue: " + overdue);
        System.out.println("\nBy Priority:");
        System.out.println("  HIGH: " + high);
        System.out.println("  MEDIUM: " + medium);
        System.out.println("  LOW: " + low);
        System.out.println();
    }

    /**
     * Finds a task by its ID
     */
    private static Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    /**
     * Parses a date string to LocalDate
     */
    private static LocalDate parseDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            System.out.println("Invalid date format. Using today's date.");
            return LocalDate.now();
        }
    }

    /**
     * Gets integer input from user with validation
     */
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        return scanner.nextInt();
    }
}

/**
 * Task class representing a single task
 */
class Task {
    private int id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private boolean completed;
    private LocalDate createdDate;

    public Task(int id, String title, String description, LocalDate dueDate, Priority priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = false;
        this.createdDate = LocalDate.now();
    }

    public void markComplete() {
        this.completed = true;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public Priority getPriority() { return priority; }
    public boolean isCompleted() { return completed; }
    public LocalDate getCreatedDate() { return createdDate; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String status = completed ? "[COMPLETED]" : "[PENDING]";
        
        return String.format(
            "ID: %d\n" +
            "Title: %s\n" +
            "Description: %s\n" +
            "Due Date: %s\n" +
            "Priority: %s\n" +
            "Status: %s\n" +
            "Created: %s",
            id, title, description, 
            dueDate.format(formatter), 
            priority, 
            status,
            createdDate.format(formatter)
        );
    }
}

/**
 * Enum for task priority levels
 */
enum Priority {
    HIGH, MEDIUM, LOW;

    public static Priority fromInt(int choice) {
        switch (choice) {
            case 1: return HIGH;
            case 2: return MEDIUM;
            case 3: return LOW;
            default: return MEDIUM;
        }
    }
}