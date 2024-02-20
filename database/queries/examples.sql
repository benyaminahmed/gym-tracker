SELECT 
	e.exercise, 
	mg.muscle_group,
	e.unit
FROM 
	public.exercise e
INNER JOIN public.muscle_group mg ON e.muscle_group_id = mg.muscle_group_id;

SELECT * FROM public.muscle_group;

SELECT * FROM public.user;

SELECT * FROM public.exercise_tracking;

INSERT INTO public.exercise_tracking (exercise_id, user_id, performance_metric, created_date)
SELECT 
	e.exercise_id,
	u.user_id,
	10,
	'2021-01-01'
FROM
	public.exercise e, public.user u
WHERE
	e.exercise = 'Bench Press' AND u.first_name = 'Benyamin' 
	
	