CREATE TABLE public.exercise (
    exercise_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    exercise character varying(256) NOT NULL,
    muscle_group_id UUID NOT NULL,
    created_date    timestamp without time zone default (now() at time zone 'utc') NOT NULL,
    CONSTRAINT exercise_muscle_group FOREIGN KEY (muscle_group_id)
        REFERENCES public.muscle_group (muscle_group_id) MATCH SIMPLE
);

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Bench Press' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Chest';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Incline Bench Press' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Chest';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Decline Bench Press' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Chest';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Cable Pec Fly' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Chest';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Incline 30° Dumbbell Chest Press' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Chest';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Push Ups' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Chest';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Barbell Bent Over Row' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Back';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Lateral Pulldowns Wide Grip' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Back';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Seated Low Row Cable' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Back';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Straight Arm Pulldown' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Back';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Pullup' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Back';

-- Deadlift is primarily a back exercise but also involves legs and lower back.
INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Deadlift' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Back';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Dumbbell Shoulder Press' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Shoulder';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Dumbbell Squat Front Raise' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Shoulder';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Dumbbell Lateral Raises' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Shoulder';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Front Raises' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Shoulder';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Military Press' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Shoulder';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Face Pull Rope' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Shoulder';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Bicep Curls (Barbell, Dumbbell, Preacher)' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Arm';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Dumbbell Hammer Bicep Curls' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Arm';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Dumbbell Curls 21''s' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Arm';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Triceps Cable Rope' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Arm';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Dumbell Triceps French Press' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Arm';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Tricep Dips' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Arm';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Supination Narrow Grip Pull' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Arm';

-- Leg Exercises
INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Barbell Squats' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Dumbbell Walking Lunges' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';

-- Barbell Thrusters also involve shoulders.
INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Barbell Thrusters' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Dumbbell Romanian Deadlift' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Dumbbell Reverse Lunges' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Leg Curl' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Leg Extension' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Calf Raises' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Leg Press' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';

-- Hip (Glutes and Hips) Exercises
INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Hip Thrusts' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Hip';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Glute Bridge' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Hip';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Cable Kickbacks' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Hip';

-- Abdominal Exercises
INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Planks (Including Dumbbell Plank)' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Abdominal';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Russian Twists' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Abdominal';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Leg Raises' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Abdominal';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Crunches' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Abdominal';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Bicycle Crunches' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Abdominal';

-- Compound Exercises
-- Deadlifts primarily target the back but also involve legs and core.
INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Deadlifts' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Back';

-- Clean and Press is primarily a shoulder exercise.
INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Clean and Press' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Shoulder';

-- Kettlebell Swings primarily target hips/glutes.
INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Kettlebell Swings' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Hip';

-- Farmers Lift primarily targets legs.
INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Farmers Lift' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';

-- Dumbbell Thrusters primarily target shoulders and legs.
INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Dumbbell Thrusters' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Shoulder';

INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Dumbbell Thrusters' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';

-- Burpees can be categorized under legs due to the squat jump.
INSERT INTO public.exercise
(
    exercise,
    muscle_group_id
) 
SELECT 
    'Burpees' AS exercise,
    mg.muscle_group_id 
FROM 
    public.muscle_group mg
WHERE
    mg.muscle_group = 'Leg';








