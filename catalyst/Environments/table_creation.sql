-------------------------------------
--DROP TABLES
-------------------------------------
-- DROP deployments TABLES
drop table r2deployments;
-- DROP instances TABLES
drop table r2instances;
-- DROP instances SEQUENCE
drop sequence r2instances_seq;
-- DROP applications TABLES
drop table r2applications;
-- DROP applications SEQUENCE
drop sequence r2applications_seq;
-- DROP environments TABLES
drop table r2environments;
-- DROP environments SEQUENCE
drop sequence r2environments_seq;
-------------------------------------
-- CREATE NEW TABLES
-------------------------------------

-- CREATE environments TABLE
create table r2environments
(
   id int not null,
   name varchar2(30) not null,
   CONSTRAINT r2environments_pk PRIMARY KEY (id)
);
-- CREATE environments SEQUENCE
create sequence r2environments_seq start with 1 increment by 1;
-- CREATE environments TRIGGER
CREATE OR REPLACE TRIGGER r2environments_seq_trigger
BEFORE INSERT
ON r2environments
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT r2environments_seq.nextval INTO :NEW.id FROM dual;
END;
/



-------------------------------------------------------------------------------

-- CREATE applications TABLE
create table r2applications
(
   id int not null,
   name varchar2(30) not null,
   CONSTRAINT r2applications_pk PRIMARY KEY (id)
);
-- CREATE applications SEQUENCE
create sequence r2applications_seq start with 1 increment by 1;
-- CREATE applications TRIGGER
CREATE OR REPLACE TRIGGER r2applications_id_trigger
BEFORE INSERT
ON r2applications
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT r2applications_seq.nextval INTO :NEW.id FROM dual;
END;
/
-------------------------------------------------------------------------------

-- CREATE instances
create table r2instances
(
   id int not null,
   application int not null,
   environment int not null,
   link varchar2(200),
   link_public int default 0 not null,
   CONSTRAINT r2instances_pk PRIMARY KEY (id),
   CONSTRAINT r2application_fk
      FOREIGN KEY (application)
      REFERENCES r2applications(id),
   CONSTRAINT r2environment_fk
      FOREIGN KEY (environment)
      REFERENCES r2environments(id)
);
-- CREATE instances SEQUENCE
create sequence r2instances_seq start with 1 increment by 1;
-- CREATE instances TRIGGER
CREATE OR REPLACE TRIGGER r2instances_id_trigger
BEFORE INSERT
ON r2instances
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT r2instances_seq.nextval INTO :NEW.id FROM dual;
END;
/
-------------------------------------------------------------------------------

-- CREATE deployments
create table r2deployments
(
      baseline varchar2(100) not null,
      timestamp timestamp(6) not null,
      instance int not null,
      bf_job int,
      constraint r2instances_fk
         FOREIGN KEY (instance)
         REFERENCES r2instances(id)
);



------------------------------------------------
insert into r2environments (name) (select name from environments);
insert into r2applications (name) (select name from applications);

   
