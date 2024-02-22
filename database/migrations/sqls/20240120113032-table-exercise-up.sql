CREATE TABLE public.exercise (
    exercise_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    exercise character varying(256) NOT NULL UNIQUE,
    unit character varying(10) CHECK (unit IN('kg', 'seconds', 'reps')) NOT NULL,
    created_date    timestamp without time zone default (now() at time zone 'utc') NOT NULL
);


-- Chest Exercises
INSERT INTO public.exercise (exercise, unit) VALUES ('Bench Press', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Decline Bench Press', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Cable Pec Fly', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Incline 30° Dumbbell Chest Press', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Push Ups', 'reps');

-- Back Exercises
INSERT INTO public.exercise (exercise, unit) VALUES ('Barbell Bent Over Row', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Lateral Pulldowns Wide Grip', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Seated Low Row Cable', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Straight Arm Pulldown', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Pullups', 'reps');
INSERT INTO public.exercise (exercise, unit) VALUES ('Deadlift', 'kg');

-- Shoulder Exercises
INSERT INTO public.exercise (exercise, unit) VALUES ('Dumbbell Shoulder Press', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Dumbbell Squat Front Raise', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Dumbbell Lateral Raises', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Front Raises', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Military Press', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Face Pull Rope', 'kg');

-- Arm Exercises
INSERT INTO public.exercise (exercise, unit) VALUES ('Bicep Curls', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Dumbbell Hammer Bicep Curls', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Dumbbell Curls 21''s', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Triceps Cable Rope', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Dumbbell Triceps French Press', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Tricep Dips', 'reps');
INSERT INTO public.exercise (exercise, unit) VALUES ('Supination Narrow Grip Pull', 'kg');

-- Leg Exercises
INSERT INTO public.exercise (exercise, unit) VALUES ('Barbell Squats', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Dumbbell Walking Lunges', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Barbell Thrusters', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Dumbbell Romanian Deadlift', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Dumbbell Reverse Lunges', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Leg Curl', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Leg Extension', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Calf Raises', 'reps');
INSERT INTO public.exercise (exercise, unit) VALUES ('Leg Press', 'kg');

-- Hip Exercises
INSERT INTO public.exercise (exercise, unit) VALUES ('Hip Thrusts', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Glute Bridge', 'kg');
INSERT INTO public.exercise (exercise, unit) VALUES ('Cable Kickbacks', 'kg');

-- Abdominal Exercises
INSERT INTO public.exercise (exercise, unit) VALUES ('Planks', 'seconds');
INSERT INTO public.exercise (exercise, unit) VALUES ('Russian Twists', 'reps');
INSERT INTO public.exercise (exercise, unit) VALUES ('Leg Raises', 'reps');
INSERT INTO public.exercise (exercise, unit) VALUES ('Crunches', 'reps');
INSERT INTO public.exercise (exercise, unit) VALUES ('Bicycle Crunches', 'reps');

-- Additional Exercises
INSERT INTO public.exercise (exercise, unit) VALUES ('Hanging', 'seconds');
