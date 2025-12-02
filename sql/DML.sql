delete from admin;
delete from member;
delete from trainer;
delete from trainer_avail;
delete from room;
delete from session;
delete from registers;
delete from health_metric;
delete from fitness_goal;
delete from bill;

insert into admin (password, admin_name) values
('admin1', 'manager1'),
('admin2', 'manager2'),
('admin3', 'manager3');

insert into member (email_id, password, name, is_active, date_of_birth) values
('email1', 'password1', 'name1', true, '1978-01-10'),
('email2', 'password2', 'name2', true, '1978-02-10'),
('email3', 'password3', 'name3', true, '1978-03-10');

insert into trainer (password, trainer_name, tr_specialization, rate_per_hour, admin_id) values
('password1', 'trainer1', 'yoga', 100.00, 1),
('password2', 'trainer2', 'pilates', 100.00, 2),
('password3', 'trainer3', 'dance', 100.00, 3);

insert into trainer_avail (trainer_id, start_timestamp, end_timestamp) values
(1, '2026-01-01 08:00', '2026-01-01 09:00'),
(1, '2030-01-01 08:00', '2030-01-01 09:00');


insert into room (capacity) values 
(10),
(10),
(10);

insert into session (trainer_id, room_id, capacity, num_participants, start_timestamp, end_timestamp, admin_id) values
(1, 1, 10, 9, '2000-12-01 08:00', '2000-12-01 09:00', 1),
(1, 1, 10, 9, '2025-12-31 08:00', '2025-12-31 09:00', 1),
(2, 2, 10, 9, '2026-01-01 08:00', '2026-01-01 09:00', 1);

insert into registers (session_id, email_id) values
(1, 'email1');

insert into health_metric (email_id, hm_timestamp, hm_type, hm_measurement) values
('email1', '2025-11-26 08:00', 'weight', 77),
('email1', '2025-11-28 08:00', 'weight', 76),
('email1', '2025-11-29 08:00', 'weight', 75),
('email2', '2025-11-26 08:00', 'weight', 57),
('email2', '2025-11-28 08:00', 'weight', 56),
('email2', '2025-11-29 08:00', 'weight', 55);

insert into fitness_goal (email_id, fg_timestamp, fg_type, fg_target) values
('email1', '2025-11-26 08:00', 'weight', 72),
('email1', '2025-11-28 08:00', 'weight', 71),
('email1', '2025-11-29 08:00', 'weight', 70),
('email2', '2025-11-26 08:00', 'weight', 52),
('email2', '2025-11-28 08:00', 'weight', 51),
('email2', '2025-11-29 08:00', 'weight', 50);

insert into bill (session_id, email_id,	amount, bill_timestamp, paid, admin_id) values
(1, 'email1', 100, '2025-11-26 08:00', TRUE, 1);
