select * from havenlife.users u where first_name = 'John' ;

insert into havenlife.users (
	first_name, last_name ,ssn, date_of_birth, gender, address_line1, address_line2 , state_abbrev, zip, city, email, phone_number 
) values (
	'ram', 'ray', '99999', '1998-02-03', 'male', '138 st', 'apt 45', 'nj', 11204, 'edison', 'rita@gmail.com', '11111'
);

-- first name, lastnmae , gender= male, addressline2 is null, dob befor 2000-01-01

select id, first_name, last_name, date_of_birth  from havenlife.users u where gender = 'male' and address_line2 is null and date_of_birth < '2023-01-01';

select * from havenlife.users u ;

select * from havenlife.users u where lower(first_name) = 'john';

select * from havenlife.users u where upper(first_name) = 'JOHN'; 

select gender, count(*)  from havenlife.users u group by gender order by count(*) desc  ;

select * from havenlife.users u where gender = 'male' order by first_name; 

select * from havenlife.users u where gender = 'female';

select count(*)  from havenlife.users u where gender = 'male';

select last_name from havenlife.users u where address_line2 is null;
select id, last_name  from havenlife.users u where address_line2 is null;
select * from havenlife.users u where address_line2 is null and gender = 'male';

update havenlife.users set date_of_birth = '1967-10-12' where id = 7;

select * from havenlife.users u where lower(first_name) like 'jo%';
select * from havenlife.users u where first_name like '%o%';