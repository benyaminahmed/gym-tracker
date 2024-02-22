using Microsoft.Extensions.Configuration.UserSecrets;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GymTrackerAPI.DataTypes
{
    [Table("muscle_group")]
    public class MuscleGrouping
    {
        [Key]
        [Required]
        [Column("muscle_group_id")]
        public Guid MuscleGroupId { get; set; }

        [Required]
        [MaxLength(20)]
        [Column("muscle_group")]
        public string MuscleGroup { get; set; }


        [Required]
        [Column("created_date")]
        public DateTime CreatedDate { get; set; }
    }
}
