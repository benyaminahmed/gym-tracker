SELECT 
	e.exercise, 
	mg.muscle_group,
	e.unit
FROM 
	public.exercise e
INNER JOIN public.muscle_group mg ON e.muscle_group_id = mg.muscle_group_id;

SELECT * FROM public.muscle_group;