namespace GymTrackerAPI.DataTypes.Requests
{
    public class ExerciseTrackingRequest
    {
        public Guid UserId { get; set; }

        public Guid ExerciseId { get; set; }

        public float PerformanceMetric { get; set; }

    }
}
