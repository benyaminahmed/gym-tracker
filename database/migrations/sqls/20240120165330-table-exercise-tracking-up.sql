CREATE TABLE public.exercise_tracking (
    exercise_tracking_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    exercise_id UUID NOT NULL,
    user_id UUID NOT NULL,
    performance_metric NUMERIC(7,2) NOT NULL,
    created_date    timestamp without time zone default (now() at time zone 'utc') NOT NULL,
    CONSTRAINT exercise_tracking_user FOREIGN KEY (user_id)
        REFERENCES public.user (user_id) MATCH SIMPLE,
    CONSTRAINT exercise_tracking_exercise FOREIGN KEY (exercise_id)
        REFERENCES public.exercise (exercise_id) MATCH SIMPLE
);