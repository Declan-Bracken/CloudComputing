SELECT SUM(total_earnings_female) AS total_female_engineer_earnings
FROM EmploymentStatistics
WHERE occupation LIKE '%engineers%'
AND year = '2016';