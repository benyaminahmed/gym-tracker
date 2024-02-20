# Bench Press Tracking

I have pasted a comma-separated list containing values for bench press achievements over the past year. The list tracks the progress of three people, formatted as "Benyamin,Yusuf,Imran,Benyamin,Yusuf,Imran," etc. For some entries, there are no values, and these are represented with blank entries (,,) indicating no input should be made for these individuals.

## Data Format

The data for bench press achievements is as follows:

`Bench Press:10,10,,20,22.5,28,22.5,25,35,25,25,35,25,27.5,37.5,25,27.5,37.5,,30,35,20,30,,20,25,25,22.5,27.5,27.5,25,30,30,27.5,30,30,30,30,40,32.5,35,40,32.5,40,40,,,,30,40,40,25,40,40,32.5,40,40,35,40,40,25,30,30,35,40,35,35,40,42.5,37.5,42.5,30,37.5,42.5,42.5,37.5,40,45,37.5,42.5,42.5,37.5,45,45,37.5,45,47.5,37.5,45,32.5,40,45,47.5,40,47.5,47.5,40,47.5,47.5,35,,40,40,47.5,47.5,40,47.5,47.5,40,47.5,47.5,40,45,45,37.5,47.5,,40,50,45,37.5,50,45,37.5,47.5,45,40,50,47.5,40,55,42.5,42.5,55,47.5,42.5,55,47.5`

## SQL Insert Format

The SQL insert statements are generated to record each individual's bench press performance metric along with the date of achievement. The dates start at January 1, 2023, and end at February 18, 2024, with the entries spaced out equally sequentially with a gap of a few days as required. Each set of three entries represents one gym session for the three individuals: Benyamin, Yusuf, and Imran.

### Example SQL Inserts

The first few entries are converted into SQL insert statements as follows:

```sql
INSERT INTO public.exercise_tracking (exercise_id, user_id, performance_metric, created_date)
SELECT 
    e.exercise_id,
    u.user_id,
    10,
    '2023-01-01'
FROM
    public.exercise e, public.user u
WHERE
    e.exercise = 'Bench Press' AND u.first_name = 'Benyamin';

INSERT INTO public.exercise_tracking (exercise_id, user_id, performance_metric, created_date)
SELECT 
    e.exercise_id,
    u.user_id,
    10,
    '2023-01-01'
FROM
    public.exercise e, public.user u
WHERE
    e.exercise = 'Bench Press' AND u.first_name = 'Yusuf';
```

**Note**: No insert is made for Imran in cases where there is no value in the list, as indicated by ",,".