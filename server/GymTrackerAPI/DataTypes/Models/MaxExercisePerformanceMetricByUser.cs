namespace GymTrackerAPI.DataTypes.Models
{
    public class MaxExercisePerformanceMetricByUser
    {
        public Guid UserId { get; set; }

        public Guid ExerciseId { get; set; }

        public string FirstName { get; set; }

        public string LastName { get; set; }

        public string ExerciseName { get; set; }

        public float MaxPerformanceMetric { get; set; }

        public string Unit { get; set; }

        public DateTime CreatedDate { get; set; }
    }
}
