DROP TABLE IF EXISTS bill CASCADE;
DROP TABLE IF EXISTS admin CASCADE;
DROP TABLE IF EXISTS trainer CASCADE;
DROP TABLE IF EXISTS trainer_avail CASCADE;
DROP TABLE IF EXISTS room CASCADE;
DROP TABLE IF EXISTS session CASCADE;
DROP TABLE IF EXISTS registers CASCADE;
DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS health_metric CASCADE;
DROP TABLE IF EXISTS fitness_goal CASCADE;

create table admin
	(admin_id			serial,
	password			varchar(50),
	admin_name			varchar(50),
	primary key (admin_id)   
	);

create table member
	(email_id varchar(50)		unique not null,
	password			varchar(50) not null,
	name				varchar(50) not null,
	is_active			boolean not null default true,
	date_of_birth			date,
	primary key (email_id)
	);

create table trainer
	(trainer_id			serial,
	password			varchar(50),
	trainer_name			varchar(50) not null,
	tr_specialization		varchar(50) not null,
	rate_per_hour			decimal not null,
	admin_id			integer not null,
	primary key (trainer_id),
	foreign key (admin_id) references admin (admin_id)
	);

create table trainer_avail
	(avail_id			serial, 
	trainer_id			integer not null, 
	start_timestamp			timestamp not null,
	end_timestamp			timestamp not null, 
	primary key (avail_id),
	foreign key (trainer_id) references trainer (trainer_id),
	check (start_timestamp < end_timestamp)
	);

create table room
	(room_id			serial, 
	capacity 			integer not null,
	primary key (room_id)
	);

create table session
	(session_id			serial, 
	trainer_id			integer not null,
	room_id				integer not null, 
	capacity			integer not null default 10,
	num_participants 		integer not null default 0,
	start_timestamp			timestamp not null,
	end_timestamp			timestamp not null,
	admin_id			integer not null,
	primary key (session_id),
	foreign key (trainer_id) references trainer (trainer_id),
	foreign key (admin_id) references admin (admin_id),
	foreign key (room_id) references room (room_id)
	);

create table registers
	(session_id			integer not null,
	email_id			varchar(50) not null, 
	primary key(session_id, email_id),
	foreign key (email_id) references member (email_id),
	foreign key (session_id) references session (session_id)
	);

create table health_metric
	(hm_id				serial, 
	email_id			varchar(50),
	hm_timestamp			timestamp, 
	hm_type				varchar(50),
	hm_measurement			decimal,
	primary key (hm_id),
	foreign key (email_id) references member (email_id)
	);

create table fitness_goal
	(fg_id				serial,
	email_id			varchar(50),
	fg_timestamp			timestamp,
	fg_type				varchar(50),
	fg_target			varchar(50),
	primary key (fg_id),
	foreign key (email_id) references member (email_id)
	);


create table bill
	(bill_id			serial,
	session_id			integer not null,
	email_id			varchar(50) not null,
	amount				decimal not null,	
	bill_timestamp			timestamp not null default now(),
	paid				boolean not null default false, 
	admin_id			integer not null,      
	primary key (bill_id),
	foreign key (admin_id) references admin (admin_id),
	foreign key (email_id) references member (email_id),
	foreign key (session_id) references session (session_id)
	);


/*
VIEW UPCOMING SESSIONS (MEMBER FUNCTIONALITY)
*/
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

/*
VIEW ASSIGNED SESSIONS (TRAINER FUNCTIONALITY)
*/
CREATE OR REPLACE VIEW assigned_sessions AS
SELECT
	room_id,
	capacity,
	num_participants,
	start_timestamp,
	end_timestamp
FROM session;


/*
Trigger to remove trainer availability when a session is created by admin
*/
CREATE OR REPLACE FUNCTION remove_trainer_avail() RETURNS TRIGGER AS $$
	BEGIN
		DELETE FROM trainer_avail
		WHERE trainer_id = NEW.trainer_id
		AND NEW.start_timestamp <= trainer_avail.start_timestamp AND trainer_avail.end_timestamp <= NEW.end_timestamp;
		RETURN NULL;
	END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER remove_trainer_avail
AFTER INSERT ON session
FOR EACH ROW
EXECUTE FUNCTION remove_trainer_avail();

/*
Trigger to increase num_participants in a session when a new member was added to registers table
*/
CREATE OR REPLACE FUNCTION increase_num_participants() RETURNS TRIGGER AS $$
BEGIN
	UPDATE session SET num_participants = num_participants + 1
	WHERE session_id = NEW.session_id;
	RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER increase_num_participants
AFTER INSERT ON registers
FOR EACH ROW
EXECUTE FUNCTION increase_num_participants();

/*
Index to search a member by name
*/
CREATE INDEX IF NOT EXISTS index_name ON member (name);

