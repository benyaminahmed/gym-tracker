CREATE TABLE public.exercise_muscle_group (
    exercise_muscle_group_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    exercise_id UUID NOT NULL,
    muscle_group_id UUID NOT NULL,
    created_date    timestamp without time zone default (now() at time zone 'utc') NOT NULL,
    CONSTRAINT exercise_muscle_group_muscle_group FOREIGN KEY (muscle_group_id)
        REFERENCES public.muscle_group (muscle_group_id) MATCH SIMPLE,
    CONSTRAINT exercise_muscle_group_exercise FOREIGN KEY (exercise_id)
        REFERENCES public.exercise (exercise_id) MATCH SIMPLE
);

INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
SELECT e.exercise_id, mg.muscle_group_id
FROM exercise e, muscle_group mg 
WHERE e.exercise = 'Bench Press' AND mg.muscle_group = 'Chest';