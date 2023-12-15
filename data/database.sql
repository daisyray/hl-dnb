create schema havenlife;

CREATE TYPE havenlife.user_gender AS ENUM ('Male', 'Female');

create table havenlife.register(
    id serial primary key,
    email varchar(200) not null,
    password varchar(100) not null
);
create unique index email_password_unique on havenlife.register (email, password);

create table havenlife.users(
	id serial primary key,
	first_name varchar(120) not null,
	last_name varchar(120) not null,
	date_of_birth date not null,
	ssn varchar(25) not null, --only number accepted
	gender havenlife.user_gender not null,
	address_line1 varchar(50) not null,
	address_line2 varchar(50),
	city varchar(50) not null,
	state_abbrev varchar(2) not null,
	zip int not null,
	register_id int references havenlife.register(id),
	phone_number varchar(25) not null
);

create table havenlife.applications(
    id serial primary key,
    application_number varchar(20) not null unique,
    user_id int not null references havenlife.users(id),
    coverage_amount int not null,
    coverage_years int not null,
    is_smoker boolean not null default false,
    is_submitted boolean not null default false,
    created_dt date not null,
    updated_dt date
);

CREATE TYPE havenlife.policy_status AS ENUM ('accepted', 'rejected', 'under-review');

create table havenlife.policies(
    id serial primary key,
    created_dt date not null,
    policy_number varchar(20) not null unique,
    application_id int not null references havenlife.applications(id),
    premium_amount numeric(15, 2),
    status havenlife.policy_status not null default 'under-review',
    updated_dt date
);