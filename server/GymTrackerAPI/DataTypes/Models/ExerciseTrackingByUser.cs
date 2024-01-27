using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace GymTrackerAPI.DataTypes.Models
{
    public class ExerciseTrackingByUser
    {

        public Guid UserId { get; set; }

        public Guid ExerciseId { get; set; }

        public string FirstName { get; set; }

        public string LastName { get; set; }

        public string ExerciseName { get; set; }

        public float PerformanceMetric { get; set; }

        public string Unit { get; set; }

        public DateTime CreatedDate { get; set; }
    }
}
