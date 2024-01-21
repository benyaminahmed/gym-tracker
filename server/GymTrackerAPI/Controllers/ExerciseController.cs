using Microsoft.AspNetCore.Mvc;
using GymTrackerAPI.DataTypes;
using GymTrackerAPI.Data;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

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
        public async Task<IEnumerable<Exercise>> Get()
        {
            return await _context.Exercise.ToListAsync();
        }
    }
}