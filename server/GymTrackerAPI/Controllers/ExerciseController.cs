using Microsoft.AspNetCore.Mvc;
using GymTrackerAPI.DataTypes;
using GymTrackerAPI.Data;
using Microsoft.EntityFrameworkCore;
using GymTrackerAPI.DataTypes.Models;

namespace GymTrackerAPI.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class ExerciseController : ControllerBase
    {
        private readonly AppDbContext _context;

        public ExerciseController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet(Name = "GetExercises")]
        public async Task<IEnumerable<ExerciseWithMuscleGroup>> Get()
        {
            var exercisesWithMuscleGroups = await (
                from e in _context.Exercise
                join emg in _context.ExerciseMuscleGroup on e.ExerciseId equals emg.ExerciseId into emgGroup
                from subemg in emgGroup.DefaultIfEmpty()
                join mg in _context.MuscleGrouping on subemg.MuscleGroupId equals mg.MuscleGroupId into mgGroup
                from submg in mgGroup.DefaultIfEmpty()
                select new ExerciseWithMuscleGroup
                {
                    ExerciseId = e.ExerciseId,
                    ExerciseName = e.ExerciseName,
                    Unit = e.Unit,
                    CreatedDate = e.CreatedDate,
                    MuscleGroup = submg != null ? submg.MuscleGroup : "Other"
                }).ToListAsync();

            return exercisesWithMuscleGroups;
        }

    }
}