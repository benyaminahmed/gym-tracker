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
                PerformanceMetric = request.PerformanceMetric,
                CreatedDate = request.CreatedDate ?? DateTime.UtcNow 
            };
            _context.ExerciseTracking.Add(et);
            await _context.SaveChangesAsync();
        }

        [HttpGet("MaxPerformance/{exerciseId}", Name = "GetMaxExercisePerformanceMetric")]
        public async Task<IEnumerable<MaxExercisePerformanceMetricByUser>> GetMaxExercisePerformanceMetric(Guid exerciseId)
        {
            // Fetch necessary data from ExerciseTracking
            var exerciseTrackings = await _context.ExerciseTracking
                .Where(x => x.ExerciseId == exerciseId)
                .Select(x => new { x.UserId, x.PerformanceMetric, x.CreatedDate })
                .ToListAsync();

            // Perform grouping and aggregation in memory
            var maxPerformancePerUser = exerciseTrackings
                .GroupBy(x => x.UserId)
                .Select(g => new
                {
                    UserId = g.Key,
                    MaxPerformance = g.Max(x => x.PerformanceMetric),
                    MostRecentCreatedDate = g.OrderByDescending(x => x.PerformanceMetric)
                                             .ThenByDescending(x => x.CreatedDate)
                                             .Select(x => x.CreatedDate)
                                             .FirstOrDefault()
                })
                .ToList();

            // Fetch users and exercise data
            var users = await _context.User.ToListAsync();
            var exercise = await _context.Exercise.FirstAsync(x => x.ExerciseId == exerciseId);

            // Construct the final result
            var results = maxPerformancePerUser.Select(et => new MaxExercisePerformanceMetricByUser
            {
                UserId = et.UserId,
                ExerciseId = exercise.ExerciseId,
                FirstName = users.FirstOrDefault(u => u.UserId == et.UserId)?.FirstName,
                LastName = users.FirstOrDefault(u => u.UserId == et.UserId)?.LastName,
                ExerciseName = exercise.ExerciseName,
                MaxPerformanceMetric = et.MaxPerformance,
                Unit = exercise.Unit,
                CreatedDate = et.MostRecentCreatedDate
            }).ToList();

            return results;
        }


    }
}

