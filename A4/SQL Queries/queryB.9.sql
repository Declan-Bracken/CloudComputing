SELECT year,
	SUM(workers_female * full_time_female)/100 AS total_female_full_time,
	SUM(workers_female * part_time_female)/100 AS total_female_part_time,
	SUM(workers_male * full_time_male)/100 AS total_male_full_time,
	SUM(workers_male * part_time_male)/100 AS total_male_part_time
FROM EmploymentStatistics
GROUP BY year
ORDER BY year;