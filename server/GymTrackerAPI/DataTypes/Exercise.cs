using Microsoft.Extensions.Configuration.UserSecrets;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GymTrackerAPI.DataTypes
{
    [Table("exercise")]
    public class Exercise
    {
        [Key]
        [Required]
        [Column("exercise_id")]
        public Guid UserId { get; set; }

        [Required]
        [MaxLength(256)]
        [Column("exercise")]
        public string ExerciseName { get; set; }

        [Required]
        [MaxLength(10)]
        [Column("unit")]
        public string Unit { get; set; }

        [Required]
        [Column("created_date")]
        public DateTime CreatedDate { get; set; }
    }
}
