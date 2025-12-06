/*
The Hospital Management System starts with Patient Registration,where details like ID, name, health problem, and membership type are entered. 
Based on the health issue, the system suggests a suitable doctor. The patient selects an appointment date, and the system checks availability before booking.
Cost Calculation applies membership discounts (e.g., 10% off for CarePlus Membership) to the doctor’s fee or nurse’s hourly rate.
Payment is made via Credit Card, UPI, or Net Banking, with a receipt generated. Post-appointment, patients can provide Feedback (rating and comment).
Appointments can be Cancelled or Rescheduled, ensuring flexibility. This system streamlines hospital operations and enhances patient experience.
*/
import java.util.Random;
import java.util.Scanner;

// Base class for all staff members
class Staff {
    private String id;
    private String name;
    private String role;

    // Constructor
    public Staff(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    // Method to display staff details
    public void displayDetails() {
        System.out.println("ID: " + id + ", Name: " + name + ", Role: " + role);
    }
}

// Subclass for Doctor
class Doctor extends Staff {
    private String department;
    private double consultationFee;
    private String[] bookedDates; // Track booked dates using an array
    private Feedback[] feedbacks; // Store feedback using an array
    private int bookedDatesCount; // Track the number of booked dates
    private int feedbacksCount; // Track the number of feedback entries

    // Constructor
    public Doctor(String id, String name, String department, double consultationFee) {
        super(id, name, "Doctor");
        this.department = department;
        this.consultationFee = consultationFee;
        this.bookedDates = new String[100]; // Maximum of 100 booked dates
        this.feedbacks = new Feedback[100]; // Maximum of 100 feedback entries
        this.bookedDatesCount = 0;
        this.feedbacksCount = 0;
    }

    // Method overriding for calculating treatment cost
    public double calculateTreatmentCost(int days) {
        return consultationFee * days;
    }

    // Getter for department
    public String getDepartment() {
        return department;
    }

    // Check if the doctor is available on a specific date
    public boolean isAvailable(String date) {
        for (int i = 0; i < bookedDatesCount; i++) {
            if (bookedDates[i].equals(date)) {
                return false; // Date is already booked
            }
        }
        return true; // Date is available
    }

    // Book an appointment for the doctor
    public void bookAppointment(String date) {
        if (bookedDatesCount < bookedDates.length) {
            bookedDates[bookedDatesCount++] = date;
        } else {
            System.out.println("Cannot book more appointments. Maximum capacity reached.");
        }
    }

    // Cancel an appointment
    public void cancelAppointment(String date) {
        for (int i = 0; i < bookedDatesCount; i++) {
            if (bookedDates[i].equals(date)) {
                // Shift remaining elements to the left
                for (int j = i; j < bookedDatesCount - 1; j++) {
                    bookedDates[j] = bookedDates[j + 1];
                }
                bookedDatesCount--;
                System.out.println("Appointment on " + date + " has been cancelled.");
                return;
            }
        }
        System.out.println("No appointment found on " + date);
    }

    // Add feedback
    public void addFeedback(Feedback feedback) {
        if (feedbacksCount < feedbacks.length) {
            feedbacks[feedbacksCount++] = feedback;
        } else {
            System.out.println("Cannot add more feedback. Maximum capacity reached.");
        }
    }

    // Calculate average rating
    public double getAverageRating() {
        if (feedbacksCount == 0) return 0.0;
        double totalRating = 0.0;
        for (int i = 0; i < feedbacksCount; i++) {
            totalRating += feedbacks[i].getRating();
        }
        return totalRating / feedbacksCount;
    }

    // Display feedbacks
    public void displayFeedbacks() {
        if (feedbacksCount == 0) {
            System.out.println("No feedback available for " + getName());
        } else {
            System.out.println("Feedbacks for " + getName() + ":");
            for (int i = 0; i < feedbacksCount; i++) {
                System.out.println(feedbacks[i]);
            }
        }
    }
}

// Subclass for Nurse
class Nurse extends Staff {
    private double hourlyRate;

    // Constructor
    public Nurse(String id, String name, double hourlyRate) {
        super(id, name, "Nurse");
        this.hourlyRate = hourlyRate;
    }

    // Method overriding for calculating treatment cost
    public double calculateTreatmentCost(int hours) {
        return hourlyRate * hours;
    }
}

// Class to manage patients
class Patient {
    private String id;
    private String name;
    private String healthProblem;
    private String membershipType;

    // Constructor
    public Patient(String id, String name, String healthProblem, String membershipType) {
        this.id = id;
        this.name = name;
        this.healthProblem = healthProblem;
        this.membershipType = membershipType;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHealthProblem() {
        return healthProblem;
    }

    public String getMembershipType() {
        return membershipType;
    }

    // Method to display patient details
    public void displayDetails() {
        System.out.println("ID: " + id + ", Name: " + name + ", Health Problem: " + healthProblem + ", Membership: " + membershipType);
    }
}

// Class to manage appointments
class Appointment {
    private String appointmentId;
    private Patient patient;
    private Staff staff;
    private String appointmentDate;
    private boolean isCancelled;

    // Constructor
    public Appointment(Patient patient, Staff staff, String appointmentDate) {
        this.appointmentId = "APT" + new Random().nextInt(1000); // Random appointment ID
        this.patient = patient;
        this.staff = staff;
        this.appointmentDate = appointmentDate;
        this.isCancelled = false;
    }

    // Method to display appointment details
    public void displayDetails() {
        System.out.println("Appointment ID: " + appointmentId);
        patient.displayDetails();
        staff.displayDetails();
        System.out.println("Appointment Date: " + appointmentDate);
        System.out.println("Status: " + (isCancelled ? "Cancelled" : "Scheduled"));
    }

    // Method overloading for calculating treatment cost
    public double calculateTreatmentCost(int value) {
        double totalCost = 0;
        if (staff instanceof Doctor) {
            totalCost = ((Doctor) staff).calculateTreatmentCost(value);
        } else if (staff instanceof Nurse) {
            totalCost = ((Nurse) staff).calculateTreatmentCost(value);
        }

        // Apply membership discount
        switch (patient.getMembershipType()) {
            case "CarePlus Membership":
                totalCost *= 0.9; // 10% off
                break;
            case "Premium Patient":
                totalCost *= 0.75; // 25% off
                break;
            case "Staff/Doctor Membership":
                totalCost *= 0.5; // 50% off
                break;
        }
        return totalCost;
    }

    // Cancel appointment
    public void cancel() {
        if (staff instanceof Doctor) {
            ((Doctor) staff).cancelAppointment(appointmentDate);
        }
        this.isCancelled = true;
        System.out.println("Appointment " + appointmentId + " has been cancelled.");
    }

    // Reschedule appointment
    public void reschedule(String newDate) {
        if (staff instanceof Doctor) {
            ((Doctor) staff).cancelAppointment(appointmentDate);
            ((Doctor) staff).bookAppointment(newDate);
        }
        this.appointmentDate = newDate;
        System.out.println("Appointment " + appointmentId + " has been rescheduled to " + newDate);
    }

    // Getter for appointment ID
    public String getAppointmentId() {
        return appointmentId;
    }
}

// Class to manage feedback
class Feedback {
    private String patientId;
    private String doctorId;
    private int rating;
    private String comment;

    // Constructor
    public Feedback(String patientId, String doctorId, int rating, String comment) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters
    public int getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Patient ID: " + patientId + ", Rating: " + rating + ", Comment: " + comment;
    }
}

// Class to manage payment
class Payment {
    private String paymentId;
    private String appointmentId;
    private double amount;
    private String paymentMethod;

    // Constructor
    public Payment(String appointmentId, double amount, String paymentMethod) {
        this.paymentId = "PAY" + new Random().nextInt(1000); // Random payment ID
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    // Generate payment receipt
    public void generateReceipt() {
        System.out.println("\nPayment Receipt:");
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Amount: ₹" + amount);
        System.out.println("Payment Method: " + paymentMethod);
    }
}

// Class to manage departments and health problems
class Department {
    private String name;
    private String[] healthProblems;

    // Constructor
    public Department(String name, String[] healthProblems) {
        this.name = name;
        this.healthProblems = healthProblems;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String[] getHealthProblems() {
        return healthProblems;
    }

    // Method to display department details
    public void displayDetails() {
        System.out.println("Department: " + name);
        System.out.println("Health Problems: " + String.join(", ", healthProblems));
    }
}

// Main class to run the hospital system
public class HospitalSystem {
    private static Department[] departments = {
        new Department("Cardiology", new String[]{"Heart Failure", "Cholesterol Disorders", "Hypertension", "Stroke"}),
        new Department("Neurology", new String[]{"Epilepsy", "Parkinson's Disease", "Multiple Sclerosis", "Migraines"}),
        new Department("Oncology", new String[]{"Breast Cancer", "Lung Cancer", "Leukemia", "Lymphoma"}),
        new Department("Gastroenterology", new String[]{"Irritable Bowel Syndrome (IBS)", "Crohn's Disease", "Hepatitis", "Gastroesophageal Reflux Disease (GERD)"}),
        new Department("Pulmonology", new String[]{"Asthma", "Chronic Obstructive Pulmonary Disease (COPD)", "Pulmonary Hypertension", "Sleep Apnea"}),
        new Department("Endocrinology", new String[]{"Diabetes Mellitus", "Thyroid Disorders", "Adrenal Insufficiency", "Osteoporosis"}),
        new Department("Nephrology", new String[]{"Chronic Kidney Disease", "Glomerulonephritis", "Kidney Stones", "Hyponatremia"}),
        new Department("Rheumatology", new String[]{"Rheumatoid Arthritis", "Systemic Lupus Erythematosus", "Ankylosing Spondylitis", "Gout"}),
        new Department("Dermatology", new String[]{"Psoriasis", "Eczema", "Acne", "Vitiligo"})
    };

    private static Doctor[] doctors = {
        new Doctor("D101", "Dr. Rajesh Kumar", "Cardiology", 500),
        new Doctor("D102", "Dr. Priya Sharma", "Neurology", 600),
        new Doctor("D103", "Dr. Anil Gupta", "Oncology", 700),
        new Doctor("D104", "Dr. Sunita Singh", "Gastroenterology", 550),
        new Doctor("D105", "Dr. Vikram Patel", "Pulmonology", 650),
        new Doctor("D106", "Dr. Meera Desai", "Endocrinology", 600),
        new Doctor("D107", "Dr. Arjun Reddy", "Nephrology", 700),
        new Doctor("D108", "Dr. Kavita Joshi", "Rheumatology", 550),
        new Doctor("D109", "Dr. Ravi Verma", "Dermatology", 500)
    };

    // Method to suggest a doctor based on health problem
    public static Doctor suggestDoctor(String healthProblem) {
        for (Department department : departments) {
            for (String problem : department.getHealthProblems()) {
                if (problem.equalsIgnoreCase(healthProblem)) {
                    for (Doctor doctor : doctors) {
                        if (doctor.getDepartment().equalsIgnoreCase(department.getName())) {
                            return doctor;
                        }
                    }
                }
            }
        }
        return null;
    }

    // Method to check appointment availability
    public static boolean isAppointmentAvailable(Doctor doctor, String date) {
        return doctor.isAvailable(date);
    }

    // Main method to run the system
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input patient details
        System.out.print("Enter patient ID: ");
        String patientId = scanner.nextLine();
        System.out.print("Enter patient name: ");
        String patientName = scanner.nextLine();
        System.out.print("Enter health problem: ");
        String healthProblem = scanner.nextLine();
        System.out.print("Enter membership type (CarePlus Membership, Premium Patient, Staff/Doctor Membership): ");
        String membershipType = scanner.nextLine();

        // Suggest a doctor
        Doctor suggestedDoctor = suggestDoctor(healthProblem);
        if (suggestedDoctor == null) {
            System.out.println("No doctor found for the given health problem.");
            scanner.close();
            return;
        }
        System.out.println("Suggested Doctor: " + suggestedDoctor.getName());

        // Input appointment date
        System.out.print("Enter appointment date (dd/mm/yyyy): ");
        String appointmentDate = scanner.nextLine();

        // Check if the doctor is available on the requested date
        if (!isAppointmentAvailable(suggestedDoctor, appointmentDate)) {
            System.out.println("Doctor " + suggestedDoctor.getName() + " is not available on " + appointmentDate);
            scanner.close();
            return;
        }

        // Book the appointment
        suggestedDoctor.bookAppointment(appointmentDate);

        // Create patient and appointment
        Patient patient = new Patient(patientId, patientName, healthProblem, membershipType);
        Appointment appointment = new Appointment(patient, suggestedDoctor, appointmentDate);

        // Display appointment details
        System.out.println("\nAppointment Details:");
        appointment.displayDetails();

        // Calculate and display total cost
        System.out.print("Enter number of days/hours for treatment: ");
        int value = scanner.nextInt();
        double totalCost = appointment.calculateTreatmentCost(value);
        System.out.println("Total Cost: ₹" + totalCost);

        // Payment
        System.out.print("Enter payment method (Credit Card, UPI, Net Banking): ");
        scanner.nextLine(); // Consume newline
        String paymentMethod = scanner.nextLine();
        Payment payment = new Payment(appointment.getAppointmentId(), totalCost, paymentMethod);
        payment.generateReceipt();

        // Feedback
        System.out.print("\nWould you like to provide feedback? (yes/no): ");
        String feedbackChoice = scanner.nextLine();
        if (feedbackChoice.equalsIgnoreCase("yes")) {
            System.out.print("Enter rating (1-5): ");
            int rating = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (rating < 1 || rating > 5) {
                System.out.println("Invalid rating. Please enter a rating between 1 and 5.");
            } else {
                System.out.print("Enter comment: ");
                String comment = scanner.nextLine();
                Feedback feedback = new Feedback(patientId, suggestedDoctor.getId(), rating, comment);
                suggestedDoctor.addFeedback(feedback);
                System.out.println("Thank you for your feedback!");
            }
        }

        // Display doctor's average rating
        System.out.println("\nDoctor's Average Rating: " + suggestedDoctor.getAverageRating());

        // Cancellation and Rescheduling
        System.out.print("\nWould you like to cancel or reschedule the appointment? (cancel/reschedule/no): ");
        String action = scanner.nextLine();
        if (action.equalsIgnoreCase("cancel")) {
            appointment.cancel();
        } else if (action.equalsIgnoreCase("reschedule")) {
            System.out.print("Enter new appointment date (dd/mm/yyyy): ");
            String newDate = scanner.nextLine();
            appointment.reschedule(newDate);
        }

        scanner.close();
    }
}

/*
Enter patient ID: 2024BCY0034   // Input
Enter patient name: Agreem kumar  // Input
Enter health problem:  Acne      // Input
Enter membership type (CarePlus Membership, Premium Patient, Staff/Doctor Membership): Premium Patient       // Input
Suggested Doctor: Dr. Ravi Verma                                                                             // Output
Enter appointment date (dd/mm/yyyy): 10/08/2025                                                              // Input

Appointment Details:                   // Output
Appointment ID: APT879                 // Output
ID: 2024BCY0034, Name: , Health Problem: , Membership: Premium Patient Membership               // Output
ID: D109, Name: Dr. Ravi Verma, Role: Doctor                                                    // Output
Appointment Date: 10/08/2025                                                                    // Output
Status: Scheduled                           // Output
Enter number of days/hours for treatment: 180                      // Input
Total Cost: ₹15000.0                                               // Output
Enter payment method (Credit Card, UPI, Net Banking): UPI          // Input

Payment Receipt:              // Output
Payment ID: PAY362            // Output
Appointment ID: APT879        // Output
Amount: ₹15000.0             // Output
Payment Method: UPI          // Output

Would you like to provide feedback? (yes/no): Yes     // Input
Enter rating (1-5): 5                                 // Input
Enter comment: Experience is good                     // Input
Thank you for your feedback!                          // Output

Doctor's Average Rating: 5.0                           // Output

Would you like to cancel or reschedule the appointment? (cancel/reschedule/no): reschedule     // Input
Enter new appointment date (dd/mm/yyyy): 15/04/2025                                            // Input
Appointment on 12/04/2025 has been cancelled.                                                  // Output
Appointment APT879 has been rescheduled to 15/04/2025                                          // Output
*/