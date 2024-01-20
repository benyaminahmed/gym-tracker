CREATE TABLE public.exercise (
    exercise_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    exercise character varying(256) NOT NULL UNIQUE,
    muscle_group_id UUID NOT NULL,
    created_date    timestamp without time zone default (now() at time zone 'utc') NOT NULL,
    CONSTRAINT exercise_muscle_group FOREIGN KEY (muscle_group_id)
        REFERENCES public.muscle_group (muscle_group_id) MATCH SIMPLE
);