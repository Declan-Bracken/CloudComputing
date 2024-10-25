SELECT SUM(CAST(total_earnings_male AS FLOAT)) AS total_male_workers_service_2015
FROM EmploymentStatistics
WHERE major_category = 'Service'
AND year = '2015';