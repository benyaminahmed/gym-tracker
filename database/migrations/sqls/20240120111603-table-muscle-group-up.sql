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
    'Chest'
);

insert into public.muscle_group
(
    muscle_group
)
values 
(
    'Back'
);

insert into public.muscle_group
(
    muscle_group
)
values 
(
    'Shoulder'
);

insert into public.muscle_group
(
    muscle_group
)
values 
(
    'Arm'
);

insert into public.muscle_group
(
    muscle_group
)
values 
(
    'Abdominal'
);

insert into public.muscle_group
(
    muscle_group
)
values 
(
    'Leg'
);

insert into public.muscle_group
(
    muscle_group
)
values 
(
    'Hip'
);

insert into public.muscle_group
(
    muscle_group
)
values 
(
    'Neck'
);