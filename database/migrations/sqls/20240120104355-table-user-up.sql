CREATE TABLE public.user (
    user_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    first_name character varying(256) NOT NULL,
    last_name character varying(256) NOT NULL,
    date_of_birth date NOT NULL,
    created_date    timestamp without time zone default (now() at time zone 'utc') NOT NULL
);

insert into public.user 
(
    first_name,
    last_name,
    date_of_birth
)
values 
(
    'Benyamin',
    'Ahmed',
    '2009-03-09'
);

insert into public.user 
(
    first_name,
    last_name,
    date_of_birth
)
values 
(
    'Yusuf',
    'Ahmed',
    '2007-12-06'
);

insert into public.user 
(
    first_name,
    last_name,
    date_of_birth
)
values 
(
    'Imran',
    'Ahmed',
    '1976-07-27'
);
