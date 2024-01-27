using Microsoft.AspNetCore.Mvc;
using GymTrackerAPI.DataTypes;
using GymTrackerAPI.Data;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using GymTrackerAPI.DataTypes.Models;

namespace GymTrackerAPI.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class ExerciseTrackingController : ControllerBase
    {
        private readonly AppDbContext _context;

        public ExerciseTrackingController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet(Name = "GetExerciseTrackings")]
        public async Task<IEnumerable<ExerciseTrackingByUser>> Get()
        {
            var results = new List<ExerciseTrackingByUser>();

            var exerciseTrackings = await _context.ExerciseTracking.ToListAsync();

            var users = await _context.User.ToListAsync();

            var exercises = await _context.Exercise.ToListAsync();

            for(int i = 0; i < exerciseTrackings.Count; i++)
            {
                var et = exerciseTrackings[i];
                var user = users.First(x => x.UserId == et.UserId);
                var exercise = exercises.First(x => x.ExerciseId == et.ExerciseId);

                var record = new ExerciseTrackingByUser()
                {
                    UserId = et.UserId,
                    ExerciseId = et.ExerciseId,
                    FirstName = user.FirstName,
                    LastName = user.LastName,
                    ExerciseName = exercise.ExerciseName,
                    PerformanceMetric = et.PerformanceMetric,
                    Unit = exercise.Unit,
                    CreatedDate = et.CreatedDate
                };


                results.Add(record);
            }

            return results;
        }
    }
}