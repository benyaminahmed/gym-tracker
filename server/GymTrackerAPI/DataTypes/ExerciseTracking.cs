using Microsoft.Extensions.Configuration.UserSecrets;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GymTrackerAPI.DataTypes
{
    [Table("exercise_tracking")]
    public class ExerciseTracking
    {
        [Key]
        [Required]
        [Column("exercise_tracking_id")]
        public Guid ExerciseTrackingId { get; set; }

        [Required]
        [Column("exercise_id")]
        public Guid ExerciseId { get; set; }

        [Required]
        [Column("user_id")]
        public Guid UserId { get; set; }

        [Required]
        [Column("performance_metric")]
        public float PerformanceMetric { get; set; }

        [Required]
        [Column("created_date")]
        public DateTime CreatedDate { get; set; }
    }
}
