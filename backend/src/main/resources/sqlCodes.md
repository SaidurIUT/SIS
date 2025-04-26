## Triggers


1. Student ID Generator Trigger
    This trigger automatically generates a student ID based on your specified format when a new student is inserted:

````
CREATE OR REPLACE FUNCTION generate_student_id()
RETURNS TRIGGER AS $$
DECLARE
    batch_code VARCHAR(2);
    dept_code VARCHAR(1);
    course_code VARCHAR(1);
    section_code VARCHAR(1);
    max_roll INT;
    formatted_roll VARCHAR(2);
BEGIN
    -- Extract batch from user data (assuming it's stored temporarily)
    batch_code := NEW.batch;
    
    -- Get department code
    SELECT CAST(code AS VARCHAR(1)) INTO dept_code FROM departments WHERE id = NEW.department_id;
    
    -- Get course code
    SELECT CAST(code AS VARCHAR(1)) INTO course_code FROM courses WHERE id = NEW.course_id;
    
    -- Section code
    section_code := CAST(NEW.section AS VARCHAR(1));
    
    -- Find max roll number for this batch/dept/course/section combination
    SELECT COALESCE(MAX(roll), 0) INTO max_roll 
    FROM student_info 
    WHERE batch = batch_code 
    AND department_id = NEW.department_id 
    AND course_id = NEW.course_id 
    AND section = NEW.section;
    
    -- Increment roll
    max_roll := max_roll + 1;
    
    -- Format roll with leading zero if needed
    IF max_roll < 10 THEN
        formatted_roll := '0' || CAST(max_roll AS VARCHAR);
    ELSE
        formatted_roll := CAST(max_roll AS VARCHAR);
    END IF;
    
    -- Set the student ID
    NEW.student_id := batch_code || '00' || dept_code || course_code || section_code || formatted_roll;
    NEW.roll := max_roll;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_student_insert
BEFORE INSERT ON student_info
FOR EACH ROW
EXECUTE FUNCTION generate_student_id();

````

2. Validate Subject Registration Trigger

This trigger checks if a student is eligible to register for a subject based on their current semester:

```
CREATE OR REPLACE FUNCTION validate_subject_registration()
RETURNS TRIGGER AS $$
DECLARE
    subject_semester INT;
    student_semester INT;
BEGIN
    -- Get the subject's intended semester
    SELECT semester INTO subject_semester FROM subjects WHERE id = NEW.subject_id;
    
    -- Get student's current semester
    SELECT current_semester INTO student_semester FROM student_info WHERE student_id = NEW.student_id;
    
    -- Check if student is eligible to take this subject
    IF student_semester < subject_semester THEN
        RAISE EXCEPTION 'Student cannot register for a higher semester subject';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_subject_registration
BEFORE INSERT ON subject_registrations
FOR EACH ROW
EXECUTE FUNCTION validate_subject_registration();

```

3. Automatic User Role Assignment Trigger
   This trigger automatically assigns the appropriate role when a user is created:

````
CREATE OR REPLACE FUNCTION assign_user_role()
RETURNS TRIGGER AS $$
DECLARE
    role_id INT;
BEGIN
    -- Check which type of info entity was created for this user
    IF EXISTS (SELECT 1 FROM student_info WHERE user_id = NEW.id) THEN
        -- Find student role ID
        SELECT id INTO role_id FROM roles WHERE name = 'ROLE_STUDENT';
    ELSIF EXISTS (SELECT 1 FROM teacher_info WHERE user_id = NEW.id) THEN
        -- Find teacher role ID
        SELECT id INTO role_id FROM roles WHERE name = 'ROLE_TEACHER';
    ELSIF EXISTS (SELECT 1 FROM admin_info WHERE user_id = NEW.id) THEN
        -- Find admin role ID
        SELECT id INTO role_id FROM roles WHERE name = 'ROLE_ADMIN';
    END IF;
    
    -- If a role was found, assign it to the user
    IF role_id IS NOT NULL THEN
        INSERT INTO user_role (user, role) VALUES (NEW.id, role_id);
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_user_info_insert
AFTER INSERT ON users
FOR EACH ROW
EXECUTE FUNCTION assign_user_role();

````

4. Subject Code Validation Trigger
   This trigger enforces the correct subject code format:

```` 
CREATE OR REPLACE FUNCTION validate_subject_code()
RETURNS TRIGGER AS $$
DECLARE
    dept_code VARCHAR;
    dept_name VARCHAR;
BEGIN
    -- Get department code and name
    SELECT code, name INTO dept_code, dept_name FROM departments WHERE id = NEW.department_id;
    
    -- Check if subject code follows the correct format
    IF NEW.code NOT LIKE dept_name || ' ' || dept_code || '%' THEN
        RAISE EXCEPTION 'Subject code must follow format: [DeptName] [DeptCode][Semester][ID]';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_subject_insert
BEFORE INSERT ON subjects
FOR EACH ROW
EXECUTE FUNCTION validate_subject_code();
````

5. Update Current Session/Semester Trigger
   This trigger ensures only one session and semester can be current at a time:

````
-- For Educational Sessions
CREATE OR REPLACE FUNCTION update_current_session()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.is_current = TRUE THEN
        UPDATE educational_sessions SET is_current = FALSE WHERE id != NEW.id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_session_update
BEFORE UPDATE ON educational_sessions
FOR EACH ROW
WHEN (OLD.is_current IS DISTINCT FROM NEW.is_current)
EXECUTE FUNCTION update_current_session();

-- For Semesters
CREATE OR REPLACE FUNCTION update_current_semester()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.is_current = TRUE THEN
        UPDATE semesters SET is_current = FALSE WHERE id != NEW.id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_semester_update
BEFORE UPDATE ON semesters
FOR EACH ROW
WHEN (OLD.is_current IS DISTINCT FROM NEW.is_current)
EXECUTE FUNCTION update_current_semester();
````