using Microsoft.AspNetCore.Mvc;
using GymTrackerAPI.DataTypes;
using GymTrackerAPI.Data;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using GymTrackerAPI.DataTypes.Models;
using GymTrackerAPI.DataTypes.Requests;

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
            return await (from et in _context.ExerciseTracking
                        join user in _context.User on et.UserId equals user.UserId
                        join exercise in _context.Exercise on et.ExerciseId equals exercise.ExerciseId
                        select new ExerciseTrackingByUser
                        {
                            UserId = et.UserId,
                            ExerciseId = et.ExerciseId,
                            FirstName = user.FirstName,
                            LastName = user.LastName,
                            ExerciseName = exercise.ExerciseName,
                            PerformanceMetric = et.PerformanceMetric,
                            Unit = exercise.Unit,
                            CreatedDate = et.CreatedDate
                        }).ToListAsync(); 
        }


        [HttpPost]
        public async Task CreateExerciseTracking(ExerciseTrackingRequest request)
        {
            var et = new ExerciseTracking()
            {
                ExerciseId = request.ExerciseId,
                UserId = request.UserId,
                PerformanceMetric = request.PerformanceMetric
            };
            _context.ExerciseTracking.Add(et);
            await _context.SaveChangesAsync();
        }
    }
}