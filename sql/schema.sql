CREATE TABLE MEMBERS (
    member_id     NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name     VARCHAR2(100)  NOT NULL,
    phone         VARCHAR2(15)   NOT NULL UNIQUE,
    email         VARCHAR2(100)  UNIQUE,
    date_joining  DATE           DEFAULT SYSDATE NOT NULL,
    medical_notes VARCHAR2(500)
);

CREATE TABLE PLANS (
    plan_id       NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    plan_name     VARCHAR2(50)   NOT NULL,
    plan_type     VARCHAR2(20)   NOT NULL
                  CHECK (plan_type IN ('Monthly', 'Quarterly', 'Annual')),
    duration_days NUMBER(4)      NOT NULL,
    price         NUMBER(8,2)    NOT NULL CHECK (price > 0),
    is_active     CHAR(1)        DEFAULT 'Y' NOT NULL CHECK (is_active IN ('Y', 'N'))
);

CREATE TABLE MEMBERSHIPS (
    subscription_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_id       NUMBER        NOT NULL,
    plan_id         NUMBER        NOT NULL,
    start_date      DATE          DEFAULT SYSDATE NOT NULL,
    end_date        DATE          NOT NULL,
    status          VARCHAR2(10)  DEFAULT 'Active' NOT NULL
                    CHECK (status IN ('Active', 'Expired', 'Pending')),
    CONSTRAINT fk_membership_member FOREIGN KEY (member_id) REFERENCES MEMBERS(member_id) ON DELETE CASCADE,
    CONSTRAINT fk_membership_plan   FOREIGN KEY (plan_id)   REFERENCES PLANS(plan_id)
);

CREATE TABLE ATTENDANCE (
    attendance_id  NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_id      NUMBER        NOT NULL,
    checkin_time   TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT fk_attendance_member FOREIGN KEY (member_id) REFERENCES MEMBERS(member_id) ON DELETE CASCADE
);

CREATE INDEX idx_memberships_member   ON MEMBERSHIPS(member_id);
CREATE INDEX idx_memberships_status   ON MEMBERSHIPS(status);
CREATE INDEX idx_memberships_end_date ON MEMBERSHIPS(end_date);
CREATE INDEX idx_attendance_member    ON ATTENDANCE(member_id);
CREATE INDEX idx_attendance_time      ON ATTENDANCE(checkin_time);

INSERT INTO PLANS (plan_name, plan_type, duration_days, price, is_active)
VALUES ('Monthly Basic', 'Monthly', 30, 799, 'Y');

INSERT INTO PLANS (plan_name, plan_type, duration_days, price, is_active)
VALUES ('Monthly Pro', 'Monthly', 30, 1199, 'Y');

INSERT INTO PLANS (plan_name, plan_type, duration_days, price, is_active)
VALUES ('Quarterly Basic', 'Quarterly', 90, 2099, 'Y');

INSERT INTO PLANS (plan_name, plan_type, duration_days, price, is_active)
VALUES ('Annual Pro', 'Annual', 365, 9999, 'Y');

COMMIT;

INSERT INTO MEMBERS (full_name, phone, email, date_joining)
VALUES ('Arjun Sharma', '9876543210', 'arjun@email.com', DATE '2024-01-15');

INSERT INTO MEMBERS (full_name, phone, email, date_joining)
VALUES ('Priya Nair', '9845012345', 'priya@email.com', DATE '2024-02-01');

INSERT INTO MEMBERS (full_name, phone, email, date_joining, medical_notes)
VALUES ('Rahul Verma', '9900112233', 'rahul@email.com', DATE '2024-03-10', 'Mild knee pain');

INSERT INTO MEMBERS (full_name, phone, email, date_joining)
VALUES ('Sneha Iyer', '9988776655', 'sneha@email.com', DATE '2024-04-05');

INSERT INTO MEMBERS (full_name, phone, email, date_joining, medical_notes)
VALUES ('Kiran Reddy', '9871234560', 'kiran@email.com', DATE '2024-06-20', 'Diabetic');

COMMIT;

INSERT INTO MEMBERSHIPS (member_id, plan_id, start_date, end_date, status)
VALUES (1, 4, DATE '2025-01-01', DATE '2026-01-01', 'Active');

INSERT INTO MEMBERSHIPS (member_id, plan_id, start_date, end_date, status)
VALUES (2, 3, DATE '2025-02-01', DATE '2025-05-02', 'Expired');

INSERT INTO MEMBERSHIPS (member_id, plan_id, start_date, end_date, status)
VALUES (3, 1, DATE '2025-03-01', DATE '2025-03-31', 'Expired');

INSERT INTO MEMBERSHIPS (member_id, plan_id, start_date, end_date, status)
VALUES (4, 4, DATE '2025-01-15', DATE '2026-01-15', 'Active');

INSERT INTO MEMBERSHIPS (member_id, plan_id, start_date, end_date, status)
VALUES (5, 2, DATE '2025-03-10', DATE '2026-03-10', 'Active');

COMMIT;

INSERT INTO ATTENDANCE (member_id, checkin_time) VALUES (1, SYSTIMESTAMP - INTERVAL '1' DAY);
INSERT INTO ATTENDANCE (member_id, checkin_time) VALUES (1, SYSTIMESTAMP - INTERVAL '3' DAY);
INSERT INTO ATTENDANCE (member_id, checkin_time) VALUES (2, SYSTIMESTAMP - INTERVAL '1' DAY);
INSERT INTO ATTENDANCE (member_id, checkin_time) VALUES (3, SYSTIMESTAMP - INTERVAL '2' DAY);
INSERT INTO ATTENDANCE (member_id, checkin_time) VALUES (4, SYSTIMESTAMP - INTERVAL '1' DAY);
INSERT INTO ATTENDANCE (member_id, checkin_time) VALUES (4, SYSTIMESTAMP - INTERVAL '6' DAY);
INSERT INTO ATTENDANCE (member_id, checkin_time) VALUES (5, SYSTIMESTAMP - INTERVAL '2' DAY);
INSERT INTO ATTENDANCE (member_id, checkin_time) VALUES (5, SYSTIMESTAMP - INTERVAL '5' DAY);

COMMIT;

SELECT * FROM MEMBERS;
SELECT * FROM PLANS;
SELECT * FROM MEMBERSHIPS;
SELECT * FROM ATTENDANCE;