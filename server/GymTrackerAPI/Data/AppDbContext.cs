using GymTrackerAPI.DataTypes;
using GymTrackerAPI.DataTypes.Models;
using Microsoft.EntityFrameworkCore;

namespace GymTrackerAPI.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
        {
        }

        public DbSet<User> User { get; set; }

        public DbSet<Exercise> Exercise { get; set; }

        public DbSet<ExerciseTracking> ExerciseTracking { get; set; }

        public DbSet<MuscleGrouping> MuscleGrouping { get; set; }

        public DbSet<ExerciseMuscleGroup> ExerciseMuscleGroup { get; set; }
    }
}