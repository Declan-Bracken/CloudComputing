SELECT year, SUM(CAST(workers_female AS FLOAT)) AS total_female_workers
FROM [dbo].[EmploymentStatistics]
WHERE major_category = 'Management, Business, and Financial'
GROUP BY year
ORDER BY year;