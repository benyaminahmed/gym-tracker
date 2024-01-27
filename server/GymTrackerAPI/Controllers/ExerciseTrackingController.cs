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
            var results = new List<ExerciseTrackingByUser>();

            var exerciseTrackings = await _context.ExerciseTracking.ToListAsync();

            var users = await _context.User.ToListAsync();

            var exercises = await _context.Exercise.ToListAsync();

            for (int i = 0; i < exerciseTrackings.Count; i++)
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
        /*
        [HttpPost]
        public async Task<ActionResult<TodoItem>> PostTodoItem(TodoItem todoItem)
        {
            _context.TodoItems.Add(todoItem);
            await _context.SaveChangesAsync();

            //    return CreatedAtAction("GetTodoItem", new { id = todoItem.Id }, todoItem);
            return CreatedAtAction(nameof(GetTodoItem), new { id = todoItem.Id }, todoItem);
        }*/

        [HttpPost]
        public async void CreateExerciseTracking(ExerciseTrackingRequest request)
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