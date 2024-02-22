CREATE TABLE public.muscle_group (
    muscle_group_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    muscle_group character varying(20) NOT NULL UNIQUE,
    created_date    timestamp without time zone default (now() at time zone 'utc') NOT NULL
);

insert into public.muscle_group
(
    muscle_group
)
values 
(
    'Chest & Triceps'
);

insert into public.muscle_group
(
    muscle_group
)
values 
(
    'Back & Biceps'
);

insert into public.muscle_group
(
    muscle_group
)
values 
(
    'Shoulders'
);

insert into public.muscle_group
(
    muscle_group
)
values 
(
    'Quads & Hamstrings'
);
