SELECT SUM(CAST(workers_female AS FLOAT)) AS total_female_workers_management_2015
FROM EmploymentStatistics
WHERE minor_category = 'Management'
AND year = '2015';