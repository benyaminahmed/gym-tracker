using Microsoft.Extensions.Configuration.UserSecrets;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GymTrackerAPI.DataTypes
{
    [Table("exercise_muscle_group")]
    public class ExerciseMuscleGroup
    {
        [Key]
        [Required]
        [Column("exercise_muscle_group_id")]
        public Guid ExerciseMuscleGroupId { get; set; }

        [Required]
        [Column("muscle_group_id")]
        public Guid MuscleGroupId { get; set; }

        [Required]
        [Column("exercise_id")]
        public Guid ExerciseId { get; set; }

        [Required]
        [Column("created_date")]
        public DateTime CreatedDate { get; set; }
    }
}
