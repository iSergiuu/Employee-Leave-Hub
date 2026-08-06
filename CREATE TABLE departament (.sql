CREATE TABLE departament (
    dept_id Number GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    departament_name VARCHAR2(100) Not NULL,
    manager_id NUMBER,
    max_absent_employees NUMBER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE employee (
    empl_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    role VARCHAR2(20) CHECK (role IN ('User', 'Dept_resp', 'Admin')) Not NULL,
    dept_id NUMERIC,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_dept_id FOREIGN KEY (dept_id) REFERENCES departament(dept_id)
);

ALTER TABLE departament
ADD CONSTRAINT fk_dept_manager FOREIGN KEY (manager_id) REFERENCES employee(empl_id);

CREATE TABLE LEAVE_TYPE (
    leave_type_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    code VARCHAR2(20) CHECK (code IN ('CO', 'CM', 'FP', 'SPECIAL')) NOT NULL,
    requires_attachment BOOLEAN NOT NULL,
    paid BOOLEAN NOT NULL
);

CREATE TABLE leave_balance (
    balance_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    empl_id NUMBER NOT NULL,
    leave_type_id NUMBER NOT NULL,
    year NUMBER(4) NOT NULL,
    total_allpcated NUMBER NOT NULL,
    days_used NUMBER DEFAULT 0 NOT NULL,
    CONSTRAINT fk_balance_emp FOREIGN KEY (empl_id) REFERENCES employee(empl_id),
    CONSTRAINT fk_balance_type FOREIGN KEY (leave_type_id) REFERENCES leave_type(leave_type_id),
    CONSTRAINT uq_emp_type_year UNIQUE (empl_id, leave_type_id, year)
);

CREATE TABLE public_holiday (
    holiday_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    holiday_date DATE NOT NULL UNIQUE,
    description VARCHAR2(200)
);

CREATE TABLE leave_request (
    leave_request_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    empl_id NUMBER NOT NULL,
    leave_type_id NUMBER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    working_days NUMBER NOT NULL,
    status VARCHAR2(20) CHECK (status IN ('DRAFT', 'PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_req_emp FOREIGN KEY (empl_id) REFERENCES employee(empl_id),
    CONSTRAINT fk_req_type FOREIGN KEY (leave_type_id) REFERENCES leave_type(leave_type_id),
    CONSTRAINT chk_dates CHECK (end_date >= start_date)
);

CREATE TABLE leave_workflow (
    workflow_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    leave_request_id NUMBER NOT NULL,
    empl_id NUMBER NOT NULL,
    old_status VARCHAR2(20),
    current_status VARCHAR2(20) NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comments VARCHAR2(500),
    CONSTRAINT fk_wf_req FOREIGN KEY (leave_request_id) REFERENCES leave_request(leave_request_id),
    CONSTRAINT fk_wf_emp FOREIGN KEY (empl_id) REFERENCES employee(empl_id)
);

CREATE TABLE attachment (
    attachment_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    leave_request_id NUMBER NOT NULL,
    file_name VARCHAR2(255) NOT NULL,
    file_path VARCHAR2(500) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_att_req FOREIGN KEY (leave_request_id) REFERENCES leave_request(leave_request_id)
);

CREATE OR REPLACE TRIGGER trg_department_updated_at
BEFORE UPDATE ON departament
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE OR REPLACE TRIGGER trg_employee_updated_at
BEFORE UPDATE ON employee
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

RENAME departament TO department;
ALTER TABLE department RENAME COLUMN departament_name TO department_name;

ALTER TABLE leave_balance RENAME COLUMN total_allpcated TO total_allocated;


INSERT INTO department (department_name, max_absent_employees) 
VALUES ('IT & Software Development', 2);

INSERT INTO department (department_name, max_absent_employees) 
VALUES ('Human Resources', 1);

INSERT INTO employee (name, email, role, dept_id) 
VALUES ('Sergiu', 'sergiu@leavehub.com', 'Admin', 1);

INSERT INTO employee (name, email, role, dept_id) 
VALUES ('Andrei Popescu', 'andrei.popescu@leavehub.com', 'User', 1);

INSERT INTO leave_type (name, code, requires_attachment, paid) 
VALUES ('Concediu de Odihna', 'CO', FALSE, TRUE);

INSERT INTO leave_type (name, code, requires_attachment, paid) 
VALUES ('Concediu Medical', 'CM', TRUE, TRUE);

INSERT INTO public_holiday (holiday_date, description) 
VALUES (TO_DATE('2026-11-30', 'YYYY-MM-DD'), 'Sfantul Andrei');

INSERT INTO public_holiday (holiday_date, description) 
VALUES (TO_DATE('2026-12-01', 'YYYY-MM-DD'), 'Ziua Nationala a Romaniei');

INSERT INTO leave_balance (empl_id, leave_type_id, year, total_allocated, days_used) 
VALUES (1, 1, 2026, 21, 0);

INSERT INTO leave_balance (empl_id, leave_type_id, year, total_allocated, days_used) 
VALUES (2, 1, 2026, 21, 5);

COMMIT;

ALTER TABLE employee ADD password_hash VARCHAR2(255);
ALTER TABLE employee ADD user_role VARCHAR2(50) DEFAULT 'EMPLOYEE';

select * from department;

SELECT search_condition 
FROM user_constraints 
WHERE constraint_name = 'SYS_C008596';

ALTER TABLE employee DROP COLUMN user_role;

INSERT INTO leave_balance (empl_id, leave_type_id, year, total_allocated, days_used) 
VALUES (43, 1, 2026, 21, 0);

INSERT INTO leave_balance (empl_id, leave_type_id, year, total_allocated, days_used) 
VALUES (43, 2, 2026, 10, 0);

COMMIT;

ALTER TABLE employee ADD deleted_at TIMESTAMP;
ALTER TABLE department ADD deleted_at TIMESTAMP;
ALTER TABLE public_holiday ADD deleted_at TIMESTAMP;

COMMIT;