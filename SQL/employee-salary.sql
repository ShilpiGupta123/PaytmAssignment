select e.name, s1.salary from employee e inner join salary s1 on (e.id=s.emp_id)
WHERE s1.salary=(SELECT DISTINCT(s2.salary) from salary s2 ORDER BY s2.salary desc LIMIT 0,1);



update salary s set s.salary = 5000 where s.emp_id in (select e.id from employee e where (YEAR(CURDATE()) - YEAR(e.dob)) > 30);
