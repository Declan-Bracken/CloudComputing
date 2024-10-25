SELECT year, SUM(total_earnings_male) AS total_male_earnings, SUM(total_earnings_female) AS total_female_earnings
FROM EmploymentStatistics
GROUP BY year
ORDER BY year;