// COMP 3005 Final Project
// Natalia Fomenko, 100909652
//

// Import the libraries
import java.sql.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;


// Define global variables
private Connection connection;// to connect to Postgres database
private final Scanner scanner = new Scanner(System.in); // to get user input

// Set up the connection with Postgres database
// If the connection is not null, perform the control flow
 void main() {
    // 	Set up the connection with Postgres database
    String url = "jdbc:postgresql://localhost:5432/Final"; // port 5432
    String user = "postgres";
    String password = "admin"; // my password
    try {
        // upload the driver
        Class.forName("org.postgresql.Driver");

        // make the connection
        connection = DriverManager.getConnection(url, user, password);

        if (connection != null) {
            // If the connection is not null, perform the control flow
            System.out.println("Connected to the database");
            controlFlow(); // perform the Control Flow (see below)
            connection.close();
            scanner.close();
        } else {
            // Otherwise, print an error
            System.out.println("Failed to connect to the database");
        } // end if-else
    } // end try
    catch (Exception e) {
        System.out.println("ERROR: main");
    } // end catch
} // end main

/*
*/
public void controlFlow () {

    int choice = 100; // user choice

    while(choice != 0) {
        // Print the menu
        System.out.println("USER TYPES:");
        System.out.println("(1) Member");
        System.out.println("(2) Trainer");
        System.out.println("(3) Admin");
        System.out.println("(0) Exit");

        System.out.println("Please enter your user type:");

        choice = getValidInt(); // will ask for user input until they enter a valid integer

        // Depending on the user choice, perform one of these options
        switch (choice) {
            case 0:
                // (0) Exit
                System.out.println("Goodbye");
                break;

            case 1:
                // Member
                member_handler();
                break;

            case 2:
                // Trainer
                trainer_handler();
                break;

            case 3:
                // Admin
                admin_handler();
                break;

            default:
                // Invalid user type
                System.out.println("Invalid user type");
                break;
        } // end switch
    } // end while
} // end control flow

public void member_handler() {
    // Ask for user choice
    System.out.println("MEMBER FUNCTIONALITY");
    System.out.println("(1) New member registration");
    System.out.println("(2) Update member personal details");
    System.out.println("(3) Input a new health metric");
    System.out.println("(4) Input a new fitness goal");
    System.out.println("(5) View upcoming sessions");
    System.out.println("(6) Register in a session (PT/Group)");
    System.out.println("(0) Exit");

    System.out.println("Please choose one of the options above: ");

    int choice = getValidInt(); // will ask for user input until they enter a valid integer

    // Depending on the user choice, perform one of these options
    switch (choice) {
        case 0:
            // (0) Exit
            System.out.println("Goodbye");
            break;

        case 1:
            // (1) New member registration
            add_new_member();
            break;

        case 2:
            // (2) Update member personal details
            update_member_profile();
            break;

        case 3:
            // (3) Input a new health metric
            add_health_metric();
            break;

        case 4:
            // (4) Input a new fitness goal
            add_fitness_goal();
            break;

        case 5:
            // (5) View upcoming sessions
            view_upcoming_sessions();
            break;

        case 6:
            // (6) Register in a session (PT/Group)
            member_session_registration();
            break;

        default:
            // ERROR: Invalid member choice
            System.out.println("ERROR: Invalid member choice");
            break;
    } // end switch
} // end member_handler

public void trainer_handler() {
    // Ask for user choice
    System.out.println("TRAINER FUNCTIONALITY:");
    System.out.println("(1) Set trainer availability");
    System.out.println("(2) View assigned sessions (PT/Group)");
    System.out.println("(3) Search a member by name (case-insensitive) and view current goal and last metric.");
    System.out.println("(0) Exit");

    System.out.println("Please choose one of the options above: ");

    int choice = getValidInt();

    // Depending on the user choice, perform one of these options
    switch (choice) {
        case 0:
            // (0) Exit
            System.out.println("Goodbye");
            break;

        case 1:
            // (1) Set trainer availability
            add_availability();
            break;

        case 2:
            // (2) View assigned sessions
            view_assigned_sessions();
            break;

        case 3:
            // (3) Member lookup
            member_lookup();
            break;

        default:
            // Invalid trainer choice
            System.out.println("ERROR: Invalid trainer choice");
            break;
    } // end switch
} // end trainer_handler

public void admin_handler() {
    // Ask for user choice
    System.out.println("ADMIN FUNCTIONALITY:");
    System.out.println("(1) Add a new session");
    System.out.println("(2) Update an existing session");
    System.out.println("(3) Generate a bill");
    System.out.println("(0) Exit");

    System.out.println("Please choose one of the options above: ");

    int choice =  getValidInt(); // will ask for user input until they enter a valid integer

    // Depending on the user choice, perform one of these options
    switch (choice) {
        case 0:
            // (0) Exit
            System.out.println("Goodbye");
            break;

        case 1:
            // (1) Add a new session
            add_session();
            break;

        case 2:
            // (2) Update an existing session
            update_session();
            break;

        case 3:
            // (2) Generate a bill
            generate_bill();
            break;

        default:
            // Invalid admin choice
            System.out.println("Invalid admin choice");
            break;
    } // end switch
} // end admin_handler

//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
// Member functionality
// Add a new member with a unique email and basic profile info
public void add_new_member() {
    // Get user input - member email ID
    System.out.println("Please enter member email ID: ");
    String email_id = scanner.nextLine();

    // Get user input - member password
    System.out.println("Please enter member password: ");
    String password = scanner.nextLine();

    // Get user input - member name
    System.out.println("Please enter member name: ");
    String name = scanner.nextLine();

    // Get user input - date of birth
    System.out.println("Please enter the date of birth (yyyy-mm-dd): ");
    String inputstring = scanner.nextLine();
    java.sql.Date date_of_birth = String2Date(inputstring); // will ask for user input until they enter a valid date

    String insertQuery = "INSERT INTO member (email_id, password, name, is_active, date_of_birth) VALUES (?, ?, ?, ?, ?)";

    try {
        // Create the prepared statement
        java.sql.PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setString(1, email_id);
        statement.setString(2, password);
        statement.setString(3, name);
        statement.setBoolean(4, true);
        statement.setDate(5, date_of_birth);

        statement.executeUpdate();

        System.out.println("New member was successfully added to the database.");
        System.out.println();
    } // end try
    catch (Exception e) {
        System.out.println("ERROR: Invalid member profile information.");
    } // end catch
}

// Update member profile
public void update_member_profile() {
    // Get user input - email ID
    System.out.print("Please enter your email ID: ");
    String email_id = scanner.nextLine();

    // Get user input - password
    System.out.print("Please enter the password: ");
    String password = scanner.nextLine();

    // Get user input - member name
    System.out.print("Please enter the name: ");
    String name = scanner.nextLine();

    // Get user input - is active
    System.out.print("Is this member active (yes/no): ");
    boolean is_active = getValidBoolean();

    // Get user input - date of birth
    System.out.println("Please enter the date of birth (yyyy-mm-dd): ");
    String inputstring = scanner.nextLine();
    java.sql.Date date_of_birth = String2Date(inputstring);

    String updateQuery = "UPDATE member SET password=?, name=?, is_active=?, date_of_birth=? WHERE email_id=?";

    try {
        // Create the prepared statement
        java.sql.PreparedStatement statement = connection.prepareStatement(updateQuery);

        statement.setString(1, password);
        statement.setString(2, name);
        statement.setBoolean(3, is_active);
        statement.setDate(4, date_of_birth);
        statement.setString(5, email_id);

        statement.executeUpdate();

        System.out.println("The member profile was successfully updated in the database.");
        System.out.println();

    } // end try
    catch (Exception e) {
        System.out.println("Incorrect member information.");
    } // end catch
}

// Add a new health metric
public void add_health_metric() {
    // Get user input - member email ID
    System.out.println("Please enter your email ID: ");
    String email_id = scanner.nextLine();

    // Get user input - health metric timestamp
    System.out.println("Enter health metric timestamp (yyyy-MM-dd HH:mm): ");
    String inputstring = scanner.nextLine();
    Timestamp hm_timestamp = String2Timestamp(inputstring);

    // Get user input - health metric type
    System.out.println("Please enter health metric type (String): ");
    String  hm_type = scanner.nextLine();

    // Get user input - health metric measurement
    System.out.println("Please enter health metric measurement (Decimal): ");
    double hm_measurement = getValidDouble();

    String insertQuery = "INSERT INTO health_metric (email_id, hm_timestamp, hm_type, hm_measurement) " +
            "VALUES (?, ?, ?, ?) ;";

    try {
        // Create the prepared statement
        java.sql.PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setString(1, email_id);
        statement.setTimestamp(2, hm_timestamp);
        statement.setString(3, hm_type);
        statement.setDouble(4, hm_measurement);

        statement.executeUpdate();

        System.out.println("Health metric was successfully added.");
        System.out.println();
    } // end try
    catch (Exception e) {
        System.out.println("ERROR: add member.");
    } // end catch
}

public void add_fitness_goal() {
    // Get user input - member email ID
    System.out.println("Please enter your email ID: ");
    String email_id = scanner.nextLine();

    // Get user input - fitness goal timestamp
    System.out.println("Enter fitness goal timestamp (yyyy-MM-dd HH:mm): ");
    String inputstring = scanner.nextLine();
    Timestamp fg_timestamp = String2Timestamp(inputstring);

    // Get user input - fitness goal type
    System.out.println("Please enter fitness goal type (String): ");
    String fg_type = scanner.nextLine();

    // Get user input - fitness goal target
    System.out.println("Please enter fitness goal target (Decimal): ");
    String decimalString = scanner.nextLine();
    BigDecimal fg_target = new BigDecimal(decimalString);

    String insertQuery = "INSERT INTO fitness_goal(email_id, fg_timestamp, fg_type, fg_target) " +
            "VALUES (?, ?, ?, ?); ";

    try {

        // Create the prepared statement
        java.sql.PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setString(1, email_id);
        statement.setTimestamp(2, fg_timestamp);
        statement.setString(3, fg_type);
        statement.setBigDecimal(4, fg_target);

        // Statement execute update
        statement.executeUpdate();

        System.out.println("Fitness goal was successfully added.");
        System.out.println();

    } // end try
    catch (Exception e) {
        System.out.println("Invalid fitness goal entry.");
    } // end catch
}

// Member views all upcoming sessions
public void view_upcoming_sessions() {
    String CREATE_VIEW_SQL = """
            CREATE OR REPLACE VIEW upcoming_sessions AS
            SELECT
                session.session_id,
                trainer.trainer_name,
                trainer.tr_specialization,
                session.room_id,
                session.start_timestamp,
                session.end_timestamp
            FROM session
            JOIN trainer ON session.trainer_id = trainer.trainer_id
            WHERE session.start_timestamp > NOW();
            """;

    try {
        PreparedStatement statement = connection.prepareStatement(CREATE_VIEW_SQL);
        statement.executeUpdate();

        Statement statement1 = connection.createStatement();
        String selectViewSQL = "SELECT * FROM upcoming_sessions;";

        ResultSet rs = statement1.executeQuery(selectViewSQL);
        System.out.println("\n--- VIEW OF SESSIONS AVAILABLE ---");
        while (rs.next()) {
            // Retrieve by column name or index
            int session_id1 = rs.getInt("session_id");
            String trainer_name1 = rs.getString("trainer_name");
            String tr_specialization1 = rs.getString("tr_specialization");
            int room_id1 = rs.getInt("room_id");

            DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            LocalDateTime localDateTime1 = rs.getObject("start_timestamp", LocalDateTime.class);
            String formattedTimestamp1 = localDateTime1.format(FORMATTER);

            LocalDateTime localDateTime2 = rs.getObject("end_timestamp", LocalDateTime.class);
            String formattedTimestamp2 = localDateTime2.format(FORMATTER);

            // Print the results
            System.out.println("---UPCOMING SESSIONS:---");
            System.out.println("-------------------------------------");
            System.out.printf("Session ID: %d, Trainer Name: %s, Trainer Specialization: %s, " +
                            "Room ID: %d, Start Time %s, End Time %s\n",
                    session_id1, trainer_name1, tr_specialization1,
                    room_id1,
                    formattedTimestamp1, formattedTimestamp2);
        }
        System.out.println("-------------------------------------");
    }
    catch(SQLException e){
        System.err.println("ERROR: Could not print the upcoming sessions");
    }
}

// Member registers in a session
public void member_session_registration() {

    increase_num_participants_trigger(); // trigger to increase num_participants in the session upon member registration

    // Get user input - member email ID
    System.out.println("Please enter member email ID: ");
    String email_id = scanner.nextLine();

    // Get user input - session ID
    System.out.println("Please enter session ID: ");
    int session_id = getValidInt();

    // Check if the session is already full
    boolean session_full = check_session_full(session_id);

    if (session_full) {
        System.out.println("Cannot register. The session is full.");
        System.out.println();
        return;
    }

    String insertQuery = "INSERT INTO registers (session_id, email_id) VALUES (?, ?);";

    try {
        java.sql.PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, session_id);
        statement.setString(2, email_id);
        statement.executeUpdate();

        System.out.println("The member successfully registered in the session.");
        System.out.println();

    } catch (Exception e) {
        System.out.println("ERROR: The session cannot be scheduled");
    }
}

// Trigger to increase num_participants in the session table upon inserting a new row in the registers table
public void increase_num_participants_trigger() {
    // SQL to create the trigger function (PL/pgSQL)
    String createFunctionSQL = """
            CREATE OR REPLACE FUNCTION increase_num_participants() RETURNS TRIGGER AS $$
            BEGIN
                UPDATE session SET num_participants = num_participants + 1
                WHERE session_id = NEW.session_id;
                RETURN NULL;
            END;
            $$ LANGUAGE plpgsql;
           """;

    String createTriggerSQL = """
            CREATE OR REPLACE TRIGGER increase_num_participants
            AFTER INSERT ON registers
            FOR EACH ROW
            EXECUTE FUNCTION increase_num_participants();
            """;

    try {
        Statement stmt = connection.createStatement();

        // Set auto-commit to true for DDL statements to execute immediately
        connection.setAutoCommit(true);

        // Execute the function creation SQL
        stmt.executeUpdate(createFunctionSQL);
        // System.out.println("Trigger function created/replaced successfully.");

        // Execute the trigger creation SQL
        stmt.executeUpdate(createTriggerSQL);
        // System.out.println("Trigger created successfully.");

    } catch (SQLException e) {
        System.out.println("ERROR: Trigger problems.");
    }
}

//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
// Trainer functionality
public void add_availability() {
    // Get user input - trainer ID
    System.out.print("Please enter trainer id: ");
    int trainer_id = getValidInt(); // will ask for user input until valid integer is entered

    // Get user input - start timestamp
    System.out.println("Please enter the start timestamp (yyyy-MM-dd HH:mm)");
    String input_string = scanner.nextLine();
    Timestamp start_timestamp = String2Timestamp(input_string); // will ask for user input until valid timestamp is entered

    // Get user input - end timestamp
    System.out.println("Please enter the end timestamp (yyyy-MM-dd HH:mm)");
    String input_string2 = scanner.nextLine();
    Timestamp end_timestamp = String2Timestamp(input_string2); // will ask for user input until valid timestamp is entered

    // Check Availability
    boolean avail_exists = check_trainer_avail(trainer_id, start_timestamp, end_timestamp);
    if (avail_exists) { // Exit if timeslots overlap
        System.out.println("Timeslot overlap is not allowed.");
        System.out.println();
        return;
    }

    // Add availability
    String insertQuery = """
            INSERT INTO trainer_avail
            (trainer_id, start_timestamp, end_timestamp)
            VALUES (?, ?, ?)
            """;
    try {
        PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
        insertStmt.setInt(1, trainer_id);
        insertStmt.setTimestamp(2, start_timestamp);
        insertStmt.setTimestamp(3, end_timestamp);

        int rowsAffected = insertStmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Trainer availability was added successfully.");
            System.out.println();
        } else {
            System.out.println("Trainer availability could not be added.");
            System.out.println();
        }
    } catch (SQLException e) {
        System.err.println("Trainer availability could not be added.");
    }
}

// Trainer views his assigned sessions
public void view_assigned_sessions() {
    // Get user input - trainer ID
    System.out.println("Please enter your trainer ID");
    int trainer_id1 = getValidInt();

    String CREATE_VIEW_SQL = """
            CREATE OR REPLACE VIEW assigned_sessions AS
            SELECT
                room_id,
                capacity,
                num_participants,
                start_timestamp,
                end_timestamp
            FROM session
            """;

    CREATE_VIEW_SQL = CREATE_VIEW_SQL + " WHERE trainer_id = "+ trainer_id1 + ";";

    try {
        PreparedStatement statement = connection.prepareStatement(CREATE_VIEW_SQL);
        statement.executeUpdate();

        Statement statement1 = connection.createStatement();
        String selectViewSQL = "SELECT * FROM assigned_sessions";

        ResultSet rs = statement1.executeQuery(selectViewSQL);

        System.out.println("\n--- VIEW ASSIGNED SESSIONS ---");
        while (rs.next()) {
            // Retrieve by column name or index
            int room_id1 = rs.getInt("room_id");
            int capacity1 = rs.getInt("capacity");

            int num_participants1 = rs.getInt("num_participants");

            DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            LocalDateTime localDateTime = rs.getObject("start_timestamp", LocalDateTime.class);
            String formattedTimestamp = localDateTime.format(FORMATTER);

            LocalDateTime localDateTime2 = rs.getObject("end_timestamp", LocalDateTime.class);
            String formattedTimestamp2 = localDateTime2.format(FORMATTER);

            // Print the results
            System.out.printf("Room: %d, Capacity: %d, Num_participants: %d, Start time: %s, End time: %s\n", room_id1, capacity1, num_participants1, formattedTimestamp, formattedTimestamp2);
            }
            System.out.println("-------------------------------------");
    }
    catch(SQLException e){
        System.err.println("The assigned sessions could not be viewed.");
    }
}

// Trainer looks up a member by the member's name
public void member_lookup() {
    try {
        Statement statement = connection.createStatement();
        String createIndexSQL = "CREATE INDEX IF NOT EXISTS index_name ON member (name)";

        // Execute the SQL statement
        statement.executeUpdate(createIndexSQL);

        // System.out.println("Index was created successfully.");

        statement.close();

    } catch (SQLException e) {
        System.out.println("ERROR: Index problems.");
    }

    // get user input - member name
    System.out.print("Please enter member name: ");
    String member_name = scanner.nextLine();

    String CREATE_VIEW_SQL = "CREATE OR REPLACE VIEW member_lookup AS " +
            "SELECT " +
            "    member.name, " +
            "    fitness_goal.fg_type, " +
            "    fitness_goal.fg_target," +
            "    health_metric.hm_type, " +
            "    health_metric.hm_measurement " +
            "FROM member " +
            "INNER JOIN fitness_goal " +
            "ON member.email_id = fitness_goal.email_id " +
            "AND (fitness_goal.email_id, fitness_goal.fg_timestamp) IN ( " +
                "SELECT fitness_goal.email_id, MAX(fitness_goal.fg_timestamp) " +
                "FROM fitness_goal " +
                "GROUP BY fitness_goal.email_id) " +
            "INNER JOIN health_metric " +
            "ON member.email_id = health_metric.email_id " +
            "AND (health_metric.email_id, health_metric.hm_timestamp) IN ( " +
                "SELECT health_metric.email_id, MAX(health_metric.hm_timestamp) " +
                "FROM health_metric " +
                "GROUP BY health_metric.email_id) " +
            "WHERE LOWER(member.name) = LOWER('" + member_name + "');";

    try {
        Statement statement = connection.createStatement();
        statement.executeUpdate(CREATE_VIEW_SQL);

        Statement statement1 = connection.createStatement();
        String selectViewSQL = "SELECT * FROM member_lookup";

        ResultSet rs = statement1.executeQuery(selectViewSQL);
        System.out.println("\n--- VIEW MEMBER INFORMATION ---");
        while (rs.next()) {

            // Retrieve by column name or index
            String member_name1 = rs.getString(1);
            String fg_type = rs.getString(2);
            String fg_target = rs.getString(3);
            String hm_type = rs.getString(4);
            String hm_measurement = rs.getString(5);

            // Print the results
            System.out.printf("Name: %s, FG TYPE: %s, FG TARGET: %s, HM Type: %s, HM MEASURE: %S\n", member_name1, fg_type,fg_target, hm_type, hm_measurement);
        }
        System.out.println("-------------------------------------");
    }
    catch(SQLException e){
        System.out.println("ERROR: Could not generate member lookup.");
    }

}

//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
// Admin functionality
public void add_session() {
    // Get user input - trainer_id
    System.out.print("Please enter trainer id: ");
    int trainer_id = getValidInt(); // will ask for user input until a valid int is entered

    // Get user input - room_id
    System.out.print("Please enter room id: ");
    int room_id = getValidInt(); // will ask for user input until a valid int is entered

    // Get user input - session capacity
    System.out.print("Please enter session capacity: ");
    int capacity = getValidInt(); // will ask for user input until a valid int is entered

    // Get user input - start timestamp
    System.out.println("Please enter start timestamp (yyyy-MM-dd HH:mm)");
    String input_string = scanner.nextLine();
    Timestamp start_timestamp = String2Timestamp(input_string); // will ask for user input until a valid timestamp is entered

    // get user input - end timestamp
    System.out.println("Please enter end timestamp (yyyy-MM-dd HH:mm)");
    String input_string1 = scanner.nextLine();
    Timestamp end_timestamp = String2Timestamp(input_string1); // will ask for user input until a valid timestamp is entered

    // get user input - admin id
    System.out.print("Please enter your admin id: ");
    int admin_id = getValidInt(); // will ask for user input until a valid int is entered

    // Check trainer availability
    boolean trainer_available = check_trainer_avail(trainer_id, start_timestamp, end_timestamp);
     if (!trainer_available) {
         System.out.println("The trainer is not available during this timeslot.");
         System.out.println();
         return;
     }

    // Check room availability
    boolean room_available = check_room_available(room_id, start_timestamp, end_timestamp);
    if (!room_available) {
        System.out.println("The room is not available during this timeslot.");
        System.out.println();
        return;
    }

    // Check that room capacity is greater than or equal to session capacity
    boolean room_sufficient_capacity = check_room_capacity_greater_than_session_capacity(room_id, capacity);
    if (!room_sufficient_capacity) {
        System.out.println("The room capacity is insufficient.");
        System.out.println();
        return;
    }

    remove_trainer_avail_trigger();

    String insertQuery = "INSERT INTO session(trainer_id, room_id, capacity, start_timestamp, end_timestamp, admin_id) VALUES (?, ?, ?, ?, ?,?)";

    try {
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, trainer_id);
        statement.setInt(2, room_id);
        statement.setInt(3, capacity);
        statement.setTimestamp(4, start_timestamp);
        statement.setTimestamp(5, end_timestamp);
        statement.setInt(6, admin_id);

        statement.executeUpdate();
        statement.close();

        System.out.println("The session was successfully created.");
        System.out.println();

    } catch (SQLException e) {
        System.err.println("ERROR: Could not add new session.");
    }
}

// Admin updates a session
// Allows the admin to manually change some fields of the session, if needed
// All the checks were performed when the session was created
// If checks are needed, admin can delete the session and then create a new session
public void update_session() {
    // Get user input - session ID
    System.out.print("Please enter session id: ");
    int session_id = getValidInt();

    // Get user input - trainer ID
    System.out.print("Please enter new trainer id: ");
    int trainer_id = getValidInt();

    // Get user input - room ID
    System.out.print("Please enter new room id: ");
    int room_id = getValidInt();

    // Get user input - class capacity
    System.out.print("Please enter new session capacity: ");
    int capacity = getValidInt();

    // Get user input - start timestamp
    System.out.print("Please enter new start timestamp: ");
    String input_string = scanner.nextLine();
    Timestamp start_timestamp = String2Timestamp(input_string);

    // Get user input - end timestamp
    System.out.print("Please enter new end timestamp: ");
    String input_string2 = scanner.nextLine();
    Timestamp end_timestamp = String2Timestamp(input_string2);

    // Get user input - admin id
    System.out.print("Please enter your admin id: ");
    int admin_id = getValidInt();

    String updateQuery = "UPDATE session SET trainer_id = ?, room_id = ?, capacity = ?, start_timestamp = ?, end_timestamp = ?, admin_id = ? " +
            "WHERE session_id = ?";

    try {
        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setInt(1, trainer_id);
        statement.setInt(2, room_id);
        statement.setInt(3, capacity);
        statement.setTimestamp(4, start_timestamp);
        statement.setTimestamp(5, end_timestamp);
        statement.setInt(6, admin_id);
        statement.setInt(7, session_id);

        int rowsUpdated = statement.executeUpdate();
        // statement.close();

        if (rowsUpdated > 0) {
            System.out.println("The session was updated successfully.");
            System.out.println();
        } else {
            System.out.println("The session could not be updated.");
            System.out.println();
        }

    } catch (SQLException e) {
        System.err.println("ERROR: The session could not be updated.");
    }
}

// Admin generates a bill for a member who attended a session
public void generate_bill() {
    // Get user input - session ID
    System.out.print("Please enter session id: ");
    int session_id = getValidInt();

    // Get user input - member email ID
    System.out.print("Please enter member's email: ");
    String email_id = scanner.nextLine();

    // Get user input - amount
    System.out.print("Please enter amount: ");
    double amount = getValidDouble();

    // Get user input - invoice date
    System.out.print("Please enter the invoice timestamp (YYYY-MM-DD HH:MM): ");
    String inputstring = scanner.nextLine();
    Timestamp bill_timestamp = String2Timestamp(inputstring);

    // Get user input - paid
    System.out.print("Was this bill paid already(yes/no): ");
    boolean paid = getValidBoolean(); // converts yes to true and no to false

    // Get user input - admin ID
    System.out.print("Please enter your admin id: ");
    int admin_id = getValidInt();

    String insertQuery = "INSERT INTO bill (session_id, email_id, amount, bill_timestamp, paid, admin_id) VALUES (?, ?, ?, ?, ?, ?) ";
    try {
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, session_id);
        statement.setString(2, email_id);
        statement.setDouble(3, amount);
        statement.setTimestamp(4, bill_timestamp);
        statement.setBoolean(5, paid);
        statement.setInt(6, admin_id);

        statement.executeUpdate();

        System.err.println("Bill was successfully generated.");
        System.err.println();

    } catch (SQLException e) {
        System.err.println("ERROR: Could not generate a bill");
    }
}

public Timestamp String2Timestamp(String userInput) {
    LocalDateTime localDateTime = null;
    boolean isValid = false;

    do {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try {
            // Attempt to parse the input string
            localDateTime = LocalDateTime.parse(userInput, FORMATTER);
            isValid = true; // Input is valid, exit the loop
        } catch (DateTimeParseException e) {
            // Catch the exception if the format is incorrect and re-prompt
            System.out.println("Invalid format.");
        }
        if (!isValid) {
            System.out.println("Enter a timestamp (yyyy-MM-dd HH:mm)");
            userInput = scanner.nextLine();
        }

    } while (!isValid);

    return Timestamp.valueOf(localDateTime);
}

public boolean check_trainer_avail(int trainer_id, Timestamp start_timestamp, Timestamp end_timestamp) {
    String checkQuery = "SELECT COUNT(*) FROM trainer_avail " +
            "WHERE trainer_id = ? AND (? <= start_timestamp) AND (end_timestamp <= ?) ";

    try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
        checkStmt.setInt(1, trainer_id);
        checkStmt.setTimestamp(2, start_timestamp);
        checkStmt.setTimestamp(3, end_timestamp);

        ResultSet rs = checkStmt.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            return true;
        }
    }
    catch (SQLException e) {
        System.err.println("Could not check trainer availability");
    }
    return false;
}

public int getValidInt(){
    int userInput = 0;
    boolean isValid = false;

    while (!isValid) {
        try {
            userInput = scanner.nextInt();
            scanner.nextLine(); // consume the leftover newline
            isValid = true; // input was successfully read as an integer
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter an integer: ");
            scanner.nextLine(); // consume the invalid input
        }
    }
    return userInput;
}

public boolean check_room_available(int room_id, Timestamp start_timestamp, Timestamp end_timestamp) {
    String checkQuery = "SELECT COUNT(*) FROM session " +
            "WHERE room_id = ? AND (? <= start_timestamp) AND (end_timestamp <= ?) "; // room is booked

    try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
        checkStmt.setInt(1, room_id);
        checkStmt.setTimestamp(2, start_timestamp);
        checkStmt.setTimestamp(3, end_timestamp);

        ResultSet rs = checkStmt.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            return false;
        }
    }
    catch (SQLException e) {
        System.err.println("Check room availability failed");
    }
    return true;
}


public boolean check_room_capacity_greater_than_session_capacity(int room_id, int session_capacity) {
    String str = String.valueOf(session_capacity);
    String checkQuery = "SELECT COUNT(*) FROM room WHERE room_id = ? AND (capacity >= " + str + ");";

    try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
        checkStmt.setInt(1, room_id);

        ResultSet rs = checkStmt.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            return true;
        }
    }
    catch (SQLException e) {
        System.err.println("Could not compare room capacity and session capacity");
    }

    return false;
}

        public void remove_trainer_avail_trigger() {
            // SQL to create the trigger function (PL/pgSQL)
            String createFunctionSQL = """
            CREATE OR REPLACE FUNCTION remove_trainer_avail() RETURNS TRIGGER AS $$
            BEGIN
                DELETE FROM trainer_avail
                WHERE trainer_id = NEW.trainer_id
                AND NEW.start_timestamp <= trainer_avail.start_timestamp AND trainer_avail.end_timestamp <= NEW.end_timestamp;
                RETURN NULL;
            END;
            $$ LANGUAGE plpgsql;
            """;

            String createTriggerSQL = """
            CREATE OR REPLACE TRIGGER remove_trainer_avail
            AFTER INSERT ON session
            FOR EACH ROW
            EXECUTE FUNCTION remove_trainer_avail();
            """;

            try {
                Statement stmt = connection.createStatement();

                // Set auto-commit to true for DDL statements to execute immediately
                connection.setAutoCommit(true);

                // Execute the function creation SQL
                stmt.executeUpdate(createFunctionSQL);
                // System.out.println("Trigger function created/replaced successfully");

                // Execute the trigger creation SQL
                stmt.executeUpdate(createTriggerSQL);
                // System.out.println("Trigger created successfully");

            } catch (SQLException e) {
                System.out.println("Remove trainer availability trigger problem");
            }
        }

        public boolean check_session_full(int session_id) {
            String checkQuery = "SELECT COUNT(*) FROM session WHERE session_id = ? AND (num_participants >= capacity);";

            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, session_id);

                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
            catch (SQLException e) {
                System.err.println("Could not check if session is full");
            }

            return false;
        }

        public double getValidDouble(){
            double userInput = 0.0;
            boolean isValid = false;

            while (!isValid) {
                try {
                    userInput = scanner.nextDouble();
                    scanner.nextLine(); // consume the leftover newline
                    isValid = true; // Input was successfully read as a double
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a double: ");
                    scanner.nextLine(); // Consume the invalid input to prevent an infinite loop
                }
            }

            return userInput;
        }

public java.sql.Date String2Date(String userInput) {
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDate = null;
    boolean isValid = false;
    do {
        try {
            // Attempt to parse the input string into a LocalDate object
            localDate = LocalDate.parse(userInput, FORMATTER);
            isValid = true; // If parsing succeeds, the input is valid
        } catch (DateTimeParseException e) {
            // If parsing fails, catch the exception and inform the user
            System.out.println("Invalid date format");
        }
        if(!isValid) {
            System.out.println("Please enter a valid date (YYYY-MM-DD)");
            userInput = scanner.nextLine();
        }
    } while (!isValid);
    return java.sql.Date.valueOf(localDate);
}

public boolean getValidBoolean() {
    String userInput;
    boolean isValid = false;
    boolean booleanValue = false;

    while (!isValid) {
        try {
            userInput = scanner.nextLine().trim().toLowerCase(); // Read input, trim whitespace, and convert to lowercase

            if (userInput.equals("yes")) {
                isValid = true;
                booleanValue = true;
            } else if (userInput.equals("no")) {
                isValid = true;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input type. Please enter 'yes' or 'no'.");
            scanner.next(); // Consume the invalid input to prevent an infinite loop
        }
    }
    return booleanValue;
}