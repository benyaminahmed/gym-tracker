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

DO $$
DECLARE
    chest character varying(20) := 'Chest & Triceps';
    back character varying(20) := 'Back & Biceps';
    legs character varying(20) := 'Quads & Hamstrings';
    shoulders character varying(20) := 'Shoulders';
BEGIN
    -- Barbell Squats
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Barbell Squats' AND mg.muscle_group = legs;

-- Dumbbell Walking Lunges
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbbell Walking Lunges' AND mg.muscle_group = legs;

-- Barbell Thrusters
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Barbell Thrusters' AND mg.muscle_group = legs;

-- Dumbbell Romanian Deadlift
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbbell Romanian Deadlift' AND mg.muscle_group = legs;

-- Dumbbell Reverse Lunges
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbbell Reverse Lunges' AND mg.muscle_group = legs;

-- Leg Curl
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Leg Curl' AND mg.muscle_group = legs;

-- Leg Extension
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Leg Extension' AND mg.muscle_group = legs;

-- Leg Press
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Leg Press' AND mg.muscle_group = legs;

-- Hanging
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Hanging' AND mg.muscle_group = legs;

-- Pullups
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Pullups' AND mg.muscle_group = legs;

-- Dumbbell Shoulder Press
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbbell Shoulder Press' AND mg.muscle_group = shoulders;

-- Dumbbell Squat Front Raise
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbbell Squat Front Raise' AND mg.muscle_group = shoulders;

-- Dumbbell Thrusters
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbbell Thrusters' AND mg.muscle_group = shoulders;

-- Dumbell Lateral Raises
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbbell Lateral Raises' AND mg.muscle_group = shoulders;

-- Incline 30° Dumbbell Chest Press
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Incline 30° Dumbbell Chest Press' AND mg.muscle_group = chest;

-- Cable Pec Fly
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Cable Pec Fly' AND mg.muscle_group = chest;

-- Bench Press
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Bench Press' AND mg.muscle_group = chest;

-- Triceps Cable Rope
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Triceps Cable Rope' AND mg.muscle_group = chest;

-- Push Ups
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Push Ups' AND mg.muscle_group = chest;

-- Dumbell Triceps French Press
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbell Triceps French Press' AND mg.muscle_group = chest;

-- Farmers Lift
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Farmers Lift' AND mg.muscle_group = chest;

-- Barbell Bent Over Row
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Barbell Bent Over Row' AND mg.muscle_group = back;

-- Dumbbell Hammer Bicep Curls
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbbell Hammer Bicep Curls' AND mg.muscle_group = back;

-- Lateral Pulldowns Wide Grip
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Lateral Pulldowns Wide Grip' AND mg.muscle_group = back;

-- Dumbbell Plank
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbbell Plank' AND mg.muscle_group = back;

-- Dumbbell Curls 21's
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbbell Curls 21''s' AND mg.muscle_group = back;

-- Supination Narrow Grip Pull
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Supination Narrow Grip Pull' AND mg.muscle_group = back;

-- Straight Arm Pulldown
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Straight Arm Pulldown' AND mg.muscle_group = back;

-- Seated Low Row Cable
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Seated Low Row Cable' AND mg.muscle_group = back;

-- Face Pull Rope
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Face Pull Rope' AND mg.muscle_group = back;

-- Dumbbell Shrugs
INSERT INTO public.exercise_muscle_group (exercise_id, muscle_group_id)
    SELECT e.exercise_id, mg.muscle_group_id
    FROM exercise e, muscle_group mg 
    WHERE e.exercise = 'Dumbbell Shrugs' AND mg.muscle_group = back;

END $$;
