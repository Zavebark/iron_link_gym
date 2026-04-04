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